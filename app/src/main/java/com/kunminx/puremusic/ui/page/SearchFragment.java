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

package com.kunminx.puremusic.ui.page;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.ui.helper.DrawerCoordinateHelper;
import com.kunminx.puremusic.ui.state.MainActivityViewModel;
import com.kunminx.puremusic.ui.state.SearchViewModel;

/**
 * Create by KunMinX at 19/10/29
 */
public class SearchFragment extends BaseFragment {

    //TODO tip 1：每个页面都要单独配备一个 state-ViewModel，职责仅限于 "状态托管和恢复"，
    //callback-ViewModel 则是用于在 "跨页面通信" 的场景下，承担 "唯一可信源"，

    //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/8204519736

    private SearchViewModel mState;
    private MainActivityViewModel mActivityScopeState;

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(SearchViewModel.class);
        mActivityScopeState = getActivityScopeViewModel(MainActivityViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip 1: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_search, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(DrawerCoordinateHelper.getInstance());

        //TODO tip 2：绑定跟随视图控制器生命周期的、可叫停的、单独放在 UseCase 中处理的业务
        getLifecycle().addObserver(mState.downloadRequest.getCanBeStoppedUseCase());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO tip 3：将 request 作为 state-ViewModel 的成员暴露给 Activity/Fragment，
        // 如此便于语义的明确，以及实现多个 request 在 state-ViewModel 中的组合和复用。

        //如果这样说还不理解的话，详见《如何让同事爱上架构模式、少写 bug 多注释》的解析
        //https://xiaozhuanlan.com/topic/8204519736

        mActivityScopeState.downloadRequest.getDownloadFileLiveData()
                .observe(getViewLifecycleOwner(), dataResult -> {
                    if (dataResult.getResponseStatus().isSuccess()) {
                        mState.progress.set(dataResult.getResult().getProgress());
                    }
                });

        mState.downloadRequest.getDownloadFileCanBeStoppedLiveData()
                .observe(getViewLifecycleOwner(), dataResult -> {
                    if (dataResult.getResponseStatus().isSuccess()) {
                        mState.progress_cancelable.set(dataResult.getResult().getProgress());
                    }
                });
    }

    public class ClickProxy {

        public void back() {
            nav().navigateUp();
        }

        public void testNav() {
            openUrlInBrowser(getString(R.string.article_navigation));
        }

        public void subscribe() {
            openUrlInBrowser(getString(R.string.article_navigation));
        }

        public void testDownload() {
            mActivityScopeState.downloadRequest.requestDownloadFile();
        }

        //TODO tip 4: 在 UseCase 中 执行可跟随生命周期中止的下载任务

        public void testLifecycleDownload() {
            mState.downloadRequest.requestCanBeStoppedDownloadFile();
        }
    }
}
