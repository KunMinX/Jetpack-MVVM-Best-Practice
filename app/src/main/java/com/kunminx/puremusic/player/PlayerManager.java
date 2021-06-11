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

package com.kunminx.puremusic.player;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;

import com.danikula.videocache.HttpProxyCacheServer;
import com.kunminx.player.PlayerController;
import com.kunminx.player.bean.dto.ChangeMusic;
import com.kunminx.player.bean.dto.PlayingMusic;
import com.kunminx.player.contract.ICacheProxy;
import com.kunminx.player.contract.IPlayController;
import com.kunminx.player.contract.IServiceNotifier;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.player.helper.PlayerFileNameGenerator;
import com.kunminx.puremusic.player.notification.PlayerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 19/10/31
 */
public class PlayerManager implements IPlayController<TestAlbum, TestAlbum.TestMusic> {

    private static PlayerManager sManager = new PlayerManager();

    private PlayerController<TestAlbum, TestAlbum.TestMusic> mController;

    private Context mContext;

    private PlayerManager() {
        mController = new PlayerController<>();
    }

    public static PlayerManager getInstance() {
        return sManager;
    }

    private HttpProxyCacheServer mProxy;

    public void init(Context context) {
        init(context, null, null);
    }

    @Override
    public void init(Context context, IServiceNotifier iServiceNotifier, ICacheProxy iCacheProxy) {
        mContext = context.getApplicationContext();

        mProxy = new HttpProxyCacheServer.Builder(context.getApplicationContext())
                .fileNameGenerator(new PlayerFileNameGenerator())
                .maxCacheSize(2147483648L) // 2GB
                .build();

        //添加额外的音乐格式
        List<String> extraFormats = new ArrayList<>();
        extraFormats.add(".flac");
        extraFormats.add(".ape");

        mController.init(mContext, extraFormats, startOrStop -> {
            Intent intent = new Intent(mContext, PlayerService.class);
            if (startOrStop) {
                mContext.startService(intent);
            } else {
                mContext.stopService(intent);
            }
        }, url -> mProxy.getProxyUrl(url));
    }

    @Override
    public void loadAlbum(TestAlbum musicAlbum) {
        mController.loadAlbum(musicAlbum);
    }

    @Override
    public void loadAlbum(TestAlbum musicAlbum, int playIndex) {
        mController.loadAlbum(musicAlbum, playIndex);
    }

    @Override
    public void playAudio() {
        mController.playAudio();
    }

    @Override
    public void playAudio(int albumIndex) {
        mController.playAudio(albumIndex);
    }

    @Override
    public void playNext() {
        mController.playNext();
    }

    @Override
    public void playPrevious() {
        mController.playPrevious();
    }

    @Override
    public void playAgain() {
        mController.playAgain();
    }

    @Override
    public void pauseAudio() {
        mController.pauseAudio();
    }

    @Override
    public void resumeAudio() {
        mController.resumeAudio();
    }

    @Override
    public void clear() {
        mController.clear();
    }

    @Override
    public void changeMode() {
        mController.changeMode();
    }

    @Override
    public boolean isPlaying() {
        return mController.isPlaying();
    }

    @Override
    public boolean isPaused() {
        return mController.isPaused();
    }

    @Override
    public boolean isInit() {
        return mController.isInit();
    }

    @Override
    public void requestLastPlayingInfo() {
        mController.requestLastPlayingInfo();
    }

    @Override
    public void setSeek(int progress) {
        mController.setSeek(progress);
    }

    @Override
    public String getTrackTime(int progress) {
        return mController.getTrackTime(progress);
    }

    @Override
    public TestAlbum getAlbum() {
        return mController.getAlbum();
    }

    @Override
    public List<TestAlbum.TestMusic> getAlbumMusics() {
        return mController.getAlbumMusics();
    }

    @Override
    public void setChangingPlayingMusic(boolean changingPlayingMusic) {
        mController.setChangingPlayingMusic(changingPlayingMusic);
    }

    @Override
    public int getAlbumIndex() {
        return mController.getAlbumIndex();
    }

    public LiveData<ChangeMusic> getChangeMusicEvent() {
        return mController.getChangeMusicEvent();
    }

    public LiveData<PlayingMusic> getPlayingMusicEvent() {
        return mController.getPlayingMusicEvent();
    }

    public LiveData<Boolean> getPauseEvent() {
        return mController.getPauseEvent();
    }

    @Override
    public LiveData<Enum> getPlayModeEvent() {
        return mController.getPlayModeEvent();
    }

    @Override
    public Enum getRepeatMode() {
        return mController.getRepeatMode();
    }

    @Override
    public void togglePlay() {
        mController.togglePlay();
    }

    @Override
    public TestAlbum.TestMusic getCurrentPlayingMusic() {
        return mController.getCurrentPlayingMusic();
    }
}
