/*
 * Copyright 2018-present KunMinX
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

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.architecture.data.repository.DataResult;
import com.kunminx.architecture.domain.manager.NetState;
import com.kunminx.architecture.domain.manager.NetworkStateManager;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.bean.DownloadFile;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.data.bean.User;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Create by KunMinX at 19/10/29
 */
public class DataRepository implements ILocalSource, IRemoteSource {

    private static final DataRepository S_REQUEST_MANAGER = new DataRepository();
    private MutableLiveData<String> responseCodeLiveData;

    private DataRepository() {
    }

    public static DataRepository getInstance() {
        return S_REQUEST_MANAGER;
    }

    public MutableLiveData<String> getResponseCodeLiveData() {
        if (responseCodeLiveData == null) {
            responseCodeLiveData = new MutableLiveData<>();
        }
        return responseCodeLiveData;
    }

    @Override
    public void getFreeMusic(DataResult<TestAlbum> result) {

        Gson gson = new Gson();
        Type type = new TypeToken<TestAlbum>() {
        }.getType();
        TestAlbum testAlbum = gson.fromJson(Utils.getApp().getString(R.string.free_music_json), type);

        result.setResult(testAlbum, new NetState());
    }

    @Override
    public void getLibraryInfo(DataResult<List<LibraryInfo>> result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<LibraryInfo>>() {
        }.getType();
        List<LibraryInfo> list = gson.fromJson(Utils.getApp().getString(R.string.library_json), type);

        result.setResult(list, new NetState());
    }

    /**
     * TODO：模拟下载任务:
     * 可分别用于 普通的请求，和可跟随页面生命周期叫停的请求，
     * 具体可见 ViewModel 和 UseCase 中的使用。
     *
     * @param result 从 Request-ViewModel 或 UseCase 注入 LiveData，用于 控制流程、回传进度、回传文件
     */
    @Override
    public void downloadFile(DataResult<DownloadFile> result) {

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //模拟下载，假设下载一个文件要 10秒、每 100 毫秒下载 1% 并通知 UI 层

                DownloadFile downloadFile = result.getResult();
                if (downloadFile == null) {
                    downloadFile = new DownloadFile();
                }
                if (downloadFile.getProgress() < 100) {
                    downloadFile.setProgress(downloadFile.getProgress() + 1);
                    Log.d("TAG", "下载进度 " + downloadFile.getProgress() + "%");
                } else {
                    timer.cancel();
                    downloadFile.setProgress(0);
                    return;
                }
                if (downloadFile.isForgive()) {
                    timer.cancel();
                    downloadFile.setProgress(0);
                    downloadFile.setForgive(false);
                    return;
                }
                result.setResult(downloadFile, new NetState());
                downloadFile(result);
            }
        };

        timer.schedule(task, 100);
    }

    /**
     * TODO 模拟登录的网络请求
     *
     * @param user   ui 层填写的用户信息
     * @param result 模拟网络请求返回的 token
     */
    @Override
    public void login(User user, DataResult<String> result) {

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //TODO 模拟登录，假装花费了 2000 毫秒去提交用户信息，结果遭遇网络状况不良。
                //这时候可以通过 NetworkState 去通知 UI 层做出变化。

                NetState netState = new NetState();
                netState.setSuccess(false);
                netState.setResponseCode("404");

                if (netState.isSuccess()) {
                    //TODO 否则，网络状况好的情况下，可向 UI 层回传来自网络请求响应的 token 等其他信息
                    result.setResult("token:xxxxxxxxxxxx", netState);
                } else {
                    result.setResult("", netState);
                }
            }
        };

        timer.schedule(task, 2000);
    }

}
