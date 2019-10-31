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

package com.kunminx.puremusic.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.kunminx.puremusic.data.bean.AlbumResult;
import com.kunminx.player.dto.MusicAlbum;
import com.kunminx.puremusic.data.bean.SingerImg;
import com.kunminx.puremusic.data.bean.SongInfo;
import com.kunminx.puremusic.data.bean.SongResult;

/**
 * Create by KunMinX at 19/10/29
 */
public interface IRemoteRequest {

    void getSongsResult(MutableLiveData<SongResult.DataBean.SongsResult> liveData, String keyword);

    void getAlbumsResult(MutableLiveData<AlbumResult.DataBean.AlbumsResult> liveData, String keyword);

    void getSingerImg(MutableLiveData<SingerImg.SingerResult> liveData, String singerName);

    void getSongInfo(MutableLiveData<SongInfo.DataBean> liveData, String albumMid);

    void getSongUrl(MutableLiveData<String> liveData, String songMid);

    void getFreeMusic(MutableLiveData<MusicAlbum> liveData);

}
