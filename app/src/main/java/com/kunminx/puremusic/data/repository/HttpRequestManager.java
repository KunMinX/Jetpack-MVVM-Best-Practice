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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.api.APIs;
import com.kunminx.architecture.data.convert.JsonCallback;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Create by KunMinX at 19/10/29
 */
public class HttpRequestManager implements IRemoteRequest {

    private static HttpRequestManager sRequestManager = new HttpRequestManager();

    private HttpRequestManager() {
    }

    public static HttpRequestManager getInstance() {
        return sRequestManager;
    }

    private MutableLiveData<String> responseCodeLiveData;

    public MutableLiveData<String> getResponseCodeLiveData() {
        if (responseCodeLiveData == null) {
            responseCodeLiveData = new MutableLiveData<>();
        }
        return responseCodeLiveData;
    }


    /*@Override
    public void getSongsResult(MutableLiveData<SongResult.DataBean.SongsResult> liveData, String keyword) {

        //TODO 分页后续改为 paging 来处理，
        String url = String.format(APIs.SEARCH_SONG, 1, 20, keyword);

        OkGo.<SongResult>get(url)
                .execute(new JsonCallback<SongResult>() {
                    @Override
                    public void onSuccess(Response<SongResult> response) {
                        super.onSuccess(response);
                        liveData.setValue(response.body().getData().getSong());
                    }
                });
    }

    @Override
    public void getAlbumsResult(MutableLiveData<AlbumResult.DataBean.AlbumsResult> liveData, String keyword) {

        //TODO 分页后续改为 paging 来处理，
        String url = String.format(APIs.SEARCH_ALBUM, 1, 20, keyword);

        OkGo.<AlbumResult>get(url)
                .execute(new JsonCallback<AlbumResult>() {
                    @Override
                    public void onSuccess(Response<AlbumResult> response) {
                        super.onSuccess(response);
                        liveData.setValue(response.body().getData().getAlbum());
                    }
                });
    }

    @Override
    public void getSingerImg(MutableLiveData<SingerImg.SingerResult> liveData, String singerName) {

        String url = String.format(APIs.SINGLE_IMG, singerName);

        OkGo.<SingerImg>post(url)
                .headers(APIs.HEADER_KEY_OF_USER_AGENT, APIs.HEADER_VALUE_OF_USER_AGENT)
                .execute(new JsonCallback<SingerImg>() {
                    @Override
                    public void onSuccess(Response<SingerImg> response) {
                        super.onSuccess(response);
                        liveData.setValue(response.body().getResult());
                    }
                });
    }

    @Override
    public void getSongInfo(MutableLiveData<SongInfo.DataBean> liveData, String albumMid) {

        String url = String.format(APIs.ALBUM_DETAIL, albumMid);

        OkGo.<SongInfo>get(url)
                .execute(new JsonCallback<SongInfo>() {
                    @Override
                    public void onSuccess(Response<SongInfo> response) {
                        super.onSuccess(response);
                        liveData.setValue(response.body().getData());
                    }
                });
    }

    @Override
    public void getSongUrl(MutableLiveData<String> liveData, String songMid) {

        String url = APIs.SONG_URL.replace("replaceHere", songMid);

        OkGo.<SongUrl>get(url)
                .execute(new JsonCallback<SongUrl>() {
                    @Override
                    public void onSuccess(Response<SongUrl> response) {
                        super.onSuccess(response);
                        if (response.body() != null
                                && response.body().getReq_0() != null
                                && response.body().getReq_0().getData() != null) {

                            SongUrl.Req0Bean.DataBean.MidurlinfoBean midurlinfoBean =
                                    response.body().getReq_0().getData().getMidurlinfo().get(0);
                            String baseUrl = response.body().getReq_0().getData().getSip().get(0);

                            liveData.setValue(baseUrl + midurlinfoBean.getPurl());
                        }
                    }
                });
    }*/

    @Override
    public void getFreeMusic(MutableLiveData<TestAlbum> liveData) {

        Gson gson = new Gson();
        Type type = new TypeToken<TestAlbum>() {
        }.getType();
        TestAlbum testAlbum = gson.fromJson(Utils.getApp().getString(R.string.free_music_json), type);

        liveData.setValue(testAlbum);
    }

    @Override
    public void getLibraryInfo(MutableLiveData<List<LibraryInfo>> liveData) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<LibraryInfo>>() {
        }.getType();
        List<LibraryInfo> list = gson.fromJson(Utils.getApp().getString(R.string.library_json), type);

        liveData.setValue(list);
    }
}
