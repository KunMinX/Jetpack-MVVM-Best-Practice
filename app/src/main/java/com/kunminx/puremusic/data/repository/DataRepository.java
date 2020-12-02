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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.data.response.ResponseStatus;
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
public class DataRepository {

    private static final DataRepository S_REQUEST_MANAGER = new DataRepository();

    private DataRepository() {
    }

    public static DataRepository getInstance() {
        return S_REQUEST_MANAGER;
    }

    /**
     * TODO: 建议在 DataRepository 使用 DataResult 而非 LiveData 来返回结果：
     * liveData 是专用于页面开发的、用于解决生命周期安全问题的组件，
     * 有时候数据并非一定是通过 liveData 来分发给页面，也可能是通过别的组件去通知给非页面的东西，
     * 这时候 repo 方法中内定通过 liveData 分发就不太合适，不如一开始就规定不在数据层通过 liveData 返回结果。
     * <p>
     * 如果这样说还不理解的话，详见《如何让同事爱上架构模式、少写 bug 多注释》篇的解析
     * https://xiaozhuanlan.com/topic/8204519736
     *
     * @param result
     */
    public void getFreeMusic(DataResult.Result<TestAlbum> result) {

        Gson gson = new Gson();
        Type type = new TypeToken<TestAlbum>() {
        }.getType();
        TestAlbum testAlbum = gson.fromJson(Utils.getApp().getString(R.string.free_music_json), type);

        result.onResult(new DataResult<>(testAlbum, new ResponseStatus()));
    }

    public void getLibraryInfo(DataResult.Result<List<LibraryInfo>> result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<LibraryInfo>>() {
        }.getType();
        List<LibraryInfo> list = gson.fromJson(Utils.getApp().getString(R.string.library_json), type);

        result.onResult(new DataResult<>(list, new ResponseStatus()));
    }

    /**
     * TODO：模拟下载任务:
     * 可分别用于 普通的请求，和可跟随页面生命周期叫停的请求，
     * 具体可见 ViewModel 和 UseCase 中的使用。
     *
     * @param result 从 Request-ViewModel 或 UseCase 注入 LiveData，用于 控制流程、回传进度、回传文件
     */
    public void downloadFile(DownloadFile downloadFile, DataResult.Result<DownloadFile> result) {

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //模拟下载，假设下载一个文件要 10秒、每 100 毫秒下载 1% 并通知 UI 层
                if (downloadFile.getProgress() < 100) {
                    downloadFile.setProgress(downloadFile.getProgress() + 1);
                    Log.d("TAG", "下载进度 " + downloadFile.getProgress() + "%");
                } else {
                    timer.cancel();
                }
                if (downloadFile.isForgive()) {
                    timer.cancel();
                    downloadFile.setProgress(0);
                    downloadFile.setForgive(false);
                    return;
                }
                result.onResult(new DataResult<>(downloadFile, new ResponseStatus()));
            }
        };

        timer.schedule(task, 100, 100);
    }

    /**
     * TODO 模拟登录的网络请求
     *
     * @param user   ui 层填写的用户信息
     * @param result 模拟网络请求返回的 token
     */
    public void login(User user, DataResult.Result<String> result) {

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //TODO 模拟登录，假装花费了 2000 毫秒去提交用户信息，结果遭遇网络状况不良。
                //这时候可以通过 NetworkState 去通知 UI 层做出变化。

                String response = "";

                ResponseStatus responseStatus = new ResponseStatus("404", false);

                result.onResult(new DataResult<>(response, responseStatus));
            }
        };

        timer.schedule(task, 2000);
    }

}
