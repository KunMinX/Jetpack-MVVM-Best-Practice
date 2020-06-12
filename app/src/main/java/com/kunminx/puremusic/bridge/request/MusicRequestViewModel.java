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

package com.kunminx.puremusic.bridge.request;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kunminx.puremusic.data.bean.AlbumResult;
import com.kunminx.player.dto.MusicAlbum;
import com.kunminx.puremusic.data.bean.SingerImg;
import com.kunminx.puremusic.data.bean.SongInfo;
import com.kunminx.puremusic.data.bean.SongResult;
import com.kunminx.puremusic.data.repository.HttpRequestManager;

/**
 * 音乐资源  RequestViewModel
 * <p>
 * TODO tip：RequestViewModel 通常按业务划分
 * 一个项目中通常存在多个 RequestViewModel
 * 对 Jetpack ViewModel 的知识点感兴趣可详见 https://xiaozhuanlan.com/topic/6257931840
 * <p>
 * Create by KunMinX at 19/10/29
 */
public class MusicRequestViewModel extends ViewModel {

    private MutableLiveData<SongResult.DataBean.SongsResult> songResultLiveData;

    private MutableLiveData<AlbumResult.DataBean.AlbumsResult> albumResultLiveData;

    private MutableLiveData<SingerImg.SingerResult> singleImgLiveData;

    private MutableLiveData<SongInfo.DataBean> songInfoLiveData;

    private MutableLiveData<String> songUrlLiveData;

    private MutableLiveData<MusicAlbum> freeMusicLiveData;

    public MutableLiveData<SongResult.DataBean.SongsResult> getSongResultLiveData() {
        if (songResultLiveData == null) {
            songResultLiveData = new MutableLiveData<>();
        }
        return songResultLiveData;
    }

    public MutableLiveData<AlbumResult.DataBean.AlbumsResult> getAlbumResultLiveData() {
        if (albumResultLiveData == null) {
            albumResultLiveData = new MutableLiveData<>();
        }
        return albumResultLiveData;
    }

    public MutableLiveData<SingerImg.SingerResult> getSingleImgLiveData() {
        if (singleImgLiveData == null) {
            singleImgLiveData = new MutableLiveData<>();
        }
        return singleImgLiveData;
    }

    public MutableLiveData<SongInfo.DataBean> getSongInfoLiveData() {
        if (songInfoLiveData == null) {
            songInfoLiveData = new MutableLiveData<>();
        }
        return songInfoLiveData;
    }

    public MutableLiveData<String> getSongUrlLiveData() {
        if (songUrlLiveData == null) {
            songUrlLiveData = new MutableLiveData<>();
        }
        return songUrlLiveData;
    }

    public MutableLiveData<MusicAlbum> getFreeMusicLiveData() {
        if (freeMusicLiveData == null) {
            freeMusicLiveData = new MutableLiveData<>();
        }
        return freeMusicLiveData;
    }

    public void requestSongsResult(String keyword) {
        HttpRequestManager.getInstance().getSongsResult(getSongResultLiveData(), keyword);
    }

    public void requestAlbumsResult(String keyword) {
        HttpRequestManager.getInstance().getAlbumsResult(getAlbumResultLiveData(), keyword);
    }

    public void requestSingerImg(String singerName) {
        HttpRequestManager.getInstance().getSingerImg(getSingleImgLiveData(), singerName);
    }

    public void requestSongInfo(String albumMid) {
        HttpRequestManager.getInstance().getSongInfo(getSongInfoLiveData(), albumMid);
    }

    public void requestSongUrl(String songMid) {
        HttpRequestManager.getInstance().getSongUrl(getSongUrlLiveData(), songMid);
    }

    public void requestFreeMusic() {
        HttpRequestManager.getInstance().getFreeMusic(getFreeMusicLiveData());
    }

}
