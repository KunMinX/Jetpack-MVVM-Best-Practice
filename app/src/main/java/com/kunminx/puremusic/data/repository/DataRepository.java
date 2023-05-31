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

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.data.response.ResponseStatus;
import com.kunminx.architecture.data.response.ResultSource;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.api.APIs;
import com.kunminx.puremusic.data.api.AccountService;
import com.kunminx.puremusic.data.bean.DownloadState;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.data.bean.User;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private final Retrofit retrofit;

    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build();
        retrofit = new Retrofit.Builder()
            .baseUrl(APIs.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    /**
     * TODO: 建议在 DataRepository 使用 DataResult 而非 LiveData 来返回结果：
     * liveData 是专用于页面开发的、用于解决生命周期安全问题的组件，
     * 有时数据并非一定是通过 liveData 来分发给页面，也可能是通过别的组件去通知给非页面，
     * 这时 repo 方法中内定通过 liveData 分发就不太合适，不如一开始就规定不在数据层通过 liveData 返回结果。
     * <p>
     * 如这样说还不理解，详见《这是一份 “架构模式” 自驾攻略》篇的解析
     * https://xiaozhuanlan.com/topic/8204519736
     *
     * @param result result
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
     * 具体可见 Result-ViewModel 和 UseCase 中的使用。
     *
     * @param result 从 Result-ViewModel 或 UseCase 注入 LiveData，用于 控制流程、回传进度、回传文件
     */
    @SuppressLint("CheckResult")
    public void downloadFile(DataResult.Result<DownloadState> result) {
        final DownloadState[] originState = {new DownloadState()};
        Observable.interval(100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(aLong -> {
                DownloadState newState = new DownloadState();
                if (originState[0].isForgive || originState[0].progress == 100) {
                    return;
                }

                //模拟下载，假设下载一个文件要 10秒、每 100 毫秒下载 1% 并通知 UI 层
                if (originState[0].progress < 100) {
                    newState = new DownloadState(false, originState[0].progress + 1, null);
                    originState[0] = newState;
                    Log.d("---", "下载进度 " + originState[0].progress + "%");
                }

                result.onResult(new DataResult<>(newState, new ResponseStatus()));
                Log.d("---", "回推状态");
            });
    }

    /**
     * TODO 模拟登录的网络请求
     *
     * @param user   ui 层填写的用户信息
     */
    public DataResult<String> login(User user) {

        // 使用 retrofit 或任意你喜欢的库实现网络请求。此处以 retrofit 写个简单例子，
        // 并且如使用 rxjava，还可额外依赖 RxJavaCallAdapterFactory 来简化编写，具体自行网上查阅，此处不做累述，

        Call<String> call= retrofit.create(AccountService.class).login(user.getName(), user.getPassword());
        Response<String> response;
        try {
            response = call.execute();
            ResponseStatus responseStatus = new ResponseStatus(
                String.valueOf(response.code()), response.isSuccessful(), ResultSource.NETWORK);
            return new DataResult<>(response.body(), responseStatus);
        } catch (IOException e) {
            return new DataResult<>(null,
                new ResponseStatus(e.getMessage(), false, ResultSource.NETWORK));
        }
    }
}
