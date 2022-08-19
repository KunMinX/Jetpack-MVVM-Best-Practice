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

import com.danikula.videocache.HttpProxyCacheServer;
import com.kunminx.player.PlayerController;
import com.kunminx.player.PlayingInfoManager;
import com.kunminx.player.contract.ICacheProxy;
import com.kunminx.player.contract.IPlayController;
import com.kunminx.player.contract.IServiceNotifier;
import com.kunminx.player.domain.PlayerInfoDispatcher;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.player.helper.PlayerFileNameGenerator;
import com.kunminx.puremusic.player.notification.PlayerService;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.List;

/**
 * Create by KunMinX at 19/10/31
 */
public class PlayerManager implements IPlayController<TestAlbum, TestAlbum.TestMusic, TestAlbum.TestArtist> {

    private static final PlayerManager sManager = new PlayerManager();

    private final PlayerController<TestAlbum, TestAlbum.TestMusic, TestAlbum.TestArtist> mController;

    private PlayerManager() {
        mController = new PlayerController<>();
    }

    public static PlayerManager getInstance() {
        return sManager;
    }

  public void init(Context context) {
    init(context, null, null);
  }

    @Override
    public void init(Context context, IServiceNotifier iServiceNotifier, ICacheProxy iCacheProxy) {
        Context context1 = context.getApplicationContext();

    HttpProxyCacheServer proxy = new HttpProxyCacheServer.Builder(context1)
      .fileNameGenerator(new PlayerFileNameGenerator())
      .maxCacheSize(2147483648L)
      .build();

    mController.init(context1, startOrStop -> {
      Intent intent = new Intent(context1, PlayerService.class);
      if (startOrStop) context1.startService(intent);
      else context1.stopService(intent);
    }, proxy::getProxyUrl);
  }

    @Override
    public void loadAlbum(TestAlbum musicAlbum) {
        TestAlbum album = mController.getAlbum();
        if (album == null || !album.getAlbumId().equals(musicAlbum.getAlbumId())) {
            mController.loadAlbum(musicAlbum);
        }
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
    public PlayerInfoDispatcher getDispatcher() {
        return mController.getDispatcher();
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

    @Override
    public Enum<PlayingInfoManager.RepeatMode> getRepeatMode() {
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

    public MaterialDrawableBuilder.IconValue getModeIcon(Enum<PlayingInfoManager.RepeatMode> mode) {
        if (mode == PlayingInfoManager.RepeatMode.LIST_CYCLE) {
            return MaterialDrawableBuilder.IconValue.REPEAT;
        } else if (mode == PlayingInfoManager.RepeatMode.SINGLE_CYCLE) {
            return MaterialDrawableBuilder.IconValue.REPEAT_ONCE;
        } else {
            return MaterialDrawableBuilder.IconValue.SHUFFLE;
        }
    }

    public MaterialDrawableBuilder.IconValue getModeIcon() {
        return getModeIcon(getRepeatMode());
    }
}
