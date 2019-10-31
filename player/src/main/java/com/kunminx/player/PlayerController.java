/*
 * Copyright 2018-2019 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.player;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.danikula.videocache.HttpProxyCacheServer;
import com.kunminx.player.config.Configs;
import com.kunminx.player.dto.ChangeMusic;
import com.kunminx.player.dto.FreeMusic;
import com.kunminx.player.dto.MusicAlbum;
import com.kunminx.player.dto.PlayingMusic;
import com.kunminx.player.helper.MediaPlayerHelper;
import com.kunminx.player.helper.PlayerFileNameGenerator;
import com.kunminx.player.utils.NetworkUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.List;

/**
 * Create by KunMinX at 18/9/25
 */
public class PlayerController<M extends MusicAlbum, F extends FreeMusic> {

    private PlayingInfoManager<M, F> mPlayingInfoManager = new PlayingInfoManager<>();
    private boolean mIsPaused;
    private boolean mIsChangingPlayingMusic;

    private HttpProxyCacheServer proxy;

    private MutableLiveData<ChangeMusic> changeMusicLiveData = new MutableLiveData<>();
    private MutableLiveData<PlayingMusic> playingMusicLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> pauseLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> startService = new MutableLiveData<>();

    private PlayingMusic mCurrentPlay = new PlayingMusic("00:00", "00:00");
    private ChangeMusic mChangeMusic = new ChangeMusic();

    public void init(Context context) {

        Configs.CACHE_PATH = context.getApplicationContext().getCacheDir().getAbsolutePath();

        mPlayingInfoManager.init(context.getApplicationContext());

        proxy = new HttpProxyCacheServer.Builder(context.getApplicationContext())
                .fileNameGenerator(new PlayerFileNameGenerator())
                .maxCacheSize(2147483648L) // 2GB
                .build();
    }

    public boolean isInited() {
        return mPlayingInfoManager.isInited();
    }

    public void resetAlbum(Context context, M musicAlbum, int albumIndex) {
        mPlayingInfoManager.setMusicAlbum(musicAlbum);
        mPlayingInfoManager.setAlbumIndex(albumIndex);
        setChangingPlayingMusic(context, true);
        playAudio(context);
    }

    public boolean isPlaying() {
        return MediaPlayerHelper.getInstance().getMediaPlayer().isPlaying();
    }

    public boolean isPaused() {
        return mIsPaused;
    }

    /**
     * @param albumIndex 从 album 进来的一定是 album 列表的 index
     */
    public void playAudio(Context context, int albumIndex) {
        if (isPlaying() && albumIndex == mPlayingInfoManager.getAlbumIndex()) {
            return;
        }

        mPlayingInfoManager.setAlbumIndex(albumIndex);
        setChangingPlayingMusic(context, true);
        playAudio(context);
    }


    public void playAudio(Context context) {
        if (mIsChangingPlayingMusic) {
            MediaPlayerHelper.getInstance().getMediaPlayer().stop();
            getUrlAndPlay(context);
        } else if (mIsPaused) {
            resumeAudio();
        }
    }

    private void getUrlAndPlay(Context context) {
        String url = null;
        F freeMusic = null;
        freeMusic = mPlayingInfoManager.getCurrentPlayingMusic();
        url = freeMusic.getUrl();

        if (TextUtils.isEmpty(url)) {
            pauseAudio();
        } else {

            if ((url.contains("http:") || url.contains("ftp:") || url.contains("https:"))
                    && NetworkUtils.isConnected(context)) {
                MediaPlayerHelper.getInstance().play(proxy.getProxyUrl(url));
//                MediaPlayerHelper.getInstance().play(url);

            } else if (url.contains("storage")) {
                MediaPlayerHelper.getInstance().play(url);

            } else {
                MediaPlayerHelper.getInstance().playAsset(context, url);
            }
            afterPlay(context);
        }
    }

    private void afterPlay(Context context) {
        setChangingPlayingMusic(context, false);
        bindProgressListener(context);
        mIsPaused = false;
        pauseLiveData.setValue(mIsPaused);
        startService.setValue(true);
    }

    private void bindProgressListener(Context context) {
        MediaPlayerHelper.getInstance().setProgressInterval(1000).setMediaPlayerHelperCallBack(
                (state, mediaPlayerHelper, args) -> {
                    if (state == MediaPlayerHelper.CallBackState.PROGRESS) {
                        int position = mediaPlayerHelper.getMediaPlayer().getCurrentPosition();
                        int duration = mediaPlayerHelper.getMediaPlayer().getDuration();
                        mCurrentPlay.setNowTime(calculateTime(position / 1000));
                        mCurrentPlay.setAllTime(calculateTime(duration / 1000));
                        mCurrentPlay.setDuration(duration);
                        mCurrentPlay.setPlayerPosition(position);
                        playingMusicLiveData.setValue(mCurrentPlay);
                        if (mCurrentPlay.getAllTime().equals(mCurrentPlay.getNowTime())
                                //容许两秒内的误差，有的内容它就是会差那么 1 秒
                                || duration / 1000 - position / 1000 < 2) {
                            if (getRepeatMode() == PlayingInfoManager.ONE_LOOP) {
                                playAgain(context);
                                //联网或有离线的情况才自动切歌，否则提示断网
                            } else if (NetworkUtils.isConnected(context)) {
                                playNext(context);
                            } else {
                                Toast.makeText(context, context.getString(R.string.network_unconnected), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void requestLastPlayingInfo() {
        playingMusicLiveData.setValue(mCurrentPlay);
        changeMusicLiveData.setValue(mChangeMusic);
        pauseLiveData.setValue(mIsPaused);
    }

    public void requestAlbumCover(String coverUrl, String musicId) {
        OkGo.<File>get(coverUrl)
                .execute(new FileCallback(Configs.MUSIC_DOWNLOAD_PATH, musicId + ".jpg") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        startService.setValue(true);
                    }
                });
    }

    public void setSeek(int progress) {
        MediaPlayerHelper.getInstance().getMediaPlayer().seekTo(progress);
    }

    public String getTrackTime(int progress) {
        return calculateTime(progress / 1000);
    }

    private String calculateTime(int time) {
        int minute;
        int second;
        if (time >= 60) {
            minute = time / 60;
            second = time % 60;
            return (minute < 10 ? "0" + minute : "" + minute) + (second < 10 ? ":0" + second : ":" + second);
        } else {
            second = time;
            if (second < 10) {
                return "00:0" + second;
            }
            return "00:" + second;
        }
    }


    public void playNext(Context context) {
        mPlayingInfoManager.countNextIndex();
        setChangingPlayingMusic(context, true);
        playAudio(context);
    }


    public void playPrevious(Context context) {
        mPlayingInfoManager.countPreviousIndex();
        setChangingPlayingMusic(context, true);
        playAudio(context);
    }


    public void playAgain(Context context) {
        setChangingPlayingMusic(context, true);
        playAudio(context);
    }


    public void pauseAudio() {
        MediaPlayerHelper.getInstance().getMediaPlayer().pause();
        mIsPaused = true;
        pauseLiveData.setValue(mIsPaused);
        startService.setValue(true);
    }


    public void resumeAudio() {
        MediaPlayerHelper.getInstance().getMediaPlayer().start();
        mIsPaused = false;
        pauseLiveData.setValue(mIsPaused);
        startService.setValue(true);
    }


    public void clear(Context context) {
        MediaPlayerHelper.getInstance().getMediaPlayer().stop();
        MediaPlayerHelper.getInstance().getMediaPlayer().reset();
//        MediaPlayerHelper.getInstance().getMediaPlayer().release();
        mPlayingInfoManager.clear(context);
        pauseLiveData.setValue(true);
        //这里设为true是因为可能通知栏清除后，还可能在页面中点击播放
        resetIsChangingPlayingChapter(context);
        MediaPlayerHelper.getInstance().setProgressInterval(1000).setMediaPlayerHelperCallBack(null);
        startService.setValue(true);
    }

    public void resetIsChangingPlayingChapter(Context context) {
        mIsChangingPlayingMusic = true;
        setChangingPlayingMusic(context, true);
    }

    public int changeMode() {
        return mPlayingInfoManager.changeMode();
    }

    public M getAlbum() {
        return mPlayingInfoManager.getMusicAlbum();
    }

    //播放列表展示用
    public List<F> getAlbumMusics() {
        return mPlayingInfoManager.getOriginPlayingList();
    }

    public void setChangingPlayingMusic(Context context, boolean changingPlayingMusic) {
        mIsChangingPlayingMusic = changingPlayingMusic;
        if (mIsChangingPlayingMusic) {
            mChangeMusic.setBaseInfo(mPlayingInfoManager.getMusicAlbum(), getCurrentPlayingMusic());
            changeMusicLiveData.setValue(mChangeMusic);
            mCurrentPlay.setBaseInfo(mPlayingInfoManager.getMusicAlbum(), getCurrentPlayingMusic());
            mPlayingInfoManager.saveRecords(context);
        }
    }

    public int getAlbumIndex() {
        return mPlayingInfoManager.getAlbumIndex();
    }

    public MutableLiveData<ChangeMusic> getChangeMusicLiveData() {
        return changeMusicLiveData;
    }

    public MutableLiveData<PlayingMusic> getPlayingMusicLiveData() {
        return playingMusicLiveData;
    }

    public MutableLiveData<Boolean> getPauseLiveData() {
        return pauseLiveData;
    }

    public int getRepeatMode() {
        return mPlayingInfoManager.getRepeatMode();
    }

    public void togglePlay(Context context) {
        if (isPlaying()) {
            pauseAudio();
        } else {
            playAudio(context);
        }
    }

    public F getCurrentPlayingMusic() {
        return mPlayingInfoManager.getCurrentPlayingMusic();
    }

    public MutableLiveData<Boolean> getStartService() {
        return startService;
    }

}
