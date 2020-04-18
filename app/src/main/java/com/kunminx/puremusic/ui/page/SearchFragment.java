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

package com.kunminx.puremusic.ui.page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.puremusic.R;
import com.kunminx.puremusic.bridge.request.DownloadViewModel;
import com.kunminx.puremusic.bridge.state.SearchViewModel;
import com.kunminx.puremusic.ui.base.BaseFragment;
import com.kunminx.puremusic.ui.base.DataBindingConfig;
import com.kunminx.puremusic.ui.helper.DrawerCoordinateHelper;

/**
 * Create by KunMinX at 19/10/29
 */
public class SearchFragment extends BaseFragment {

    private SearchViewModel mSearchViewModel;
    private DownloadViewModel mDownloadViewModel;

    @Override
    protected void initViewModel() {
        mDownloadViewModel = getActivityViewModel(DownloadViewModel.class);
        mSearchViewModel = getFragmentViewModel(SearchViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(DrawerCoordinateHelper.getInstance());

        //TODO tip1：绑定跟随视图控制器生命周期的、可叫停的、单独放在 UseCase 中处理的业务
        getLifecycle().addObserver(mDownloadViewModel.getCanBeStoppedUseCase());
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO 2020.4.18:
        // 将 DataBinding 实例限制于 base 页面中，不上升为类成员，更不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图刷新的安全性将和基于函数式编程的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供最少必要的绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_search, mSearchViewModel, new ClickProxy());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDownloadViewModel.getDownloadFileLiveData().observe(getViewLifecycleOwner(), downloadFile -> {
            mSearchViewModel.progress.set(downloadFile.getProgress());
        });

        mDownloadViewModel.getDownloadFileCanBeStoppedLiveData().observe(getViewLifecycleOwner(), downloadFile -> {
            mSearchViewModel.progress.set(downloadFile.getProgress());
        });
    }

    public class ClickProxy extends DataBindingConfig.ClickProxy {

        public void back() {
            nav().navigateUp();
        }

        public void testNav() {
            String u = "https://xiaozhuanlan.com/topic/5860149732";
            Uri uri = Uri.parse(u);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        public void subscribe() {
            String u = "https://xiaozhuanlan.com/kunminx";
            Uri uri = Uri.parse(u);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        public void testDownload() {
            mDownloadViewModel.requestDownloadFile();
        }

        //TODO tip2: 在 UseCase 中 执行可跟随生命周期中止的下载任务

        public void testLifecycleDownload() {
            mDownloadViewModel.requestCanBeStoppedDownloadFile();
        }
    }
}
