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

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.bean.DownloadFile;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.bean.TestAlbum;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/**
 * Create by KunMinX at 19/10/29
 */
public class HttpRequestManager implements ILocalRequest, IRemoteRequest {

    private static final HttpRequestManager S_REQUEST_MANAGER = new HttpRequestManager();
    private MutableLiveData<String> responseCodeLiveData;

    private HttpRequestManager() {
    }

    public static HttpRequestManager getInstance() {
        return S_REQUEST_MANAGER;
    }

    public MutableLiveData<String> getResponseCodeLiveData() {
        if (responseCodeLiveData == null) {
            responseCodeLiveData = new MutableLiveData<>();
        }
        return responseCodeLiveData;
    }

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

    /**
     * TODO：模拟下载任务:
     * 可分别用于 普通的请求，和可跟随页面生命周期叫停的请求，
     * 具体可见 ViewModel 和 UseCase 中的使用。
     *
     * @param liveData 从 Request-ViewModel 或 UseCase 注入 LiveData，用于 控制流程、回传进度、回传文件
     */
    @Override
    public void downloadFile(MutableLiveData<DownloadFile> liveData) {

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //模拟下载，假设下载一个文件要 10秒、每 100 毫秒下载 1% 并通知 UI 层

                DownloadFile downloadFile = liveData.getValue();
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
                    return;
                }
                liveData.postValue(downloadFile);
                downloadFile(liveData);
            }
        };

        timer.schedule(task, 100);
    }

}
