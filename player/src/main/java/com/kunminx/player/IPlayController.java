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

import androidx.lifecycle.MutableLiveData;

import com.kunminx.player.dto.ChangeMusic;
import com.kunminx.player.dto.FreeMusic;
import com.kunminx.player.dto.MusicAlbum;
import com.kunminx.player.dto.PlayingMusic;

import java.util.List;

/**
 * Create by KunMinX at 18/9/24
 */
public interface IPlayController<M extends MusicAlbum, F extends FreeMusic> {

    //程序启动时就初始化
    void init(Context context);

    //切换专辑时。只在从新专辑进入播放页面时切换。
    void resetAlbum(M musicAlbum, int playIndex);

    void playAudio();

    void playAudio(int albumIndex);

    //手动点击，和自动next，都调用这个
    void playNext();

    void playPrevious();

    void playAgain();

    void pauseAudio();

    void resumeAudio();

    void clear();

    //切换循环模式
    int changeMode();

    boolean isPlaying();

    boolean isPaused();

    boolean isInited();

    void requestLastPlayingInfo();

    void requestAlbumCover(String coverUrl, String musicId);

    void setSeek(int progress);

    String getTrackTime(int progress);

    M getAlbum();

    List<F> getAlbumMusics();

    void setChangingPlayingMusic(boolean changingPlayingMusic);

    int getAlbumIndex();

    MutableLiveData<ChangeMusic> getChangeMusicLiveData();

    MutableLiveData<PlayingMusic> getPlayingMusicLiveData();

    MutableLiveData<Boolean> getPauseLiveData();

    MutableLiveData<Boolean> getStartService();

    int getRepeatMode();

    void togglePlay();

    F getCurrentPlayingMusic();
}
