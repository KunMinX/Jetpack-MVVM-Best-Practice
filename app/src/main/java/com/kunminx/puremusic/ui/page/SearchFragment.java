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
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.architecture.ui.state.State;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.bean.DownloadState;
import com.kunminx.puremusic.data.config.Const;
import com.kunminx.puremusic.domain.event.DownloadEvent;
import com.kunminx.puremusic.domain.message.DrawerCoordinateManager;
import com.kunminx.puremusic.domain.request.DownloadRequester;

/**
 * Create by KunMinX at 19/10/29
 */
public class SearchFragment extends BaseFragment {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 event-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，
    // event-ViewModel 职责仅限于 "消息分发" 场景承担 "唯一可信源"。

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private SearchStates mStates;
    private DownloadRequester mDownloadRequester;
    private DownloadRequester mGlobalDownloadRequester;

    @Override
    protected void initViewModel() {
        mStates = getFragmentScopeViewModel(SearchStates.class);
        mDownloadRequester = getFragmentScopeViewModel(DownloadRequester.class);
        mGlobalDownloadRequester = getActivityScopeViewModel(DownloadRequester.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_search, BR.vm, mStates)
            .addBindingParam(BR.click, new ClickProxy());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(DrawerCoordinateManager.getInstance());

        //TODO tip 3：绑定跟随视图控制器生命周期、可叫停、单独放在 UseCase 中处理的业务
        getLifecycle().addObserver(mDownloadRequester);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO tip 8: 此处演示使用 "唯一可信源" MVI-Dispatcher input-output 接口完成数据请求响应

        //如这么说无体会，详见《领域层设计》篇拆解 https://juejin.cn/post/7117498113983512589

        mDownloadRequester.output(this, downloadEvent -> {
            if (downloadEvent.eventId == DownloadEvent.EVENT_DOWNLOAD) {
                DownloadState state = downloadEvent.result.downloadState;
                mStates.progress_cancelable.set(state.progress);
                mStates.enableDownload.set(state.progress == 100 || state.progress == 0);
            }
        });

        mGlobalDownloadRequester.output(this, downloadEvent -> {
            if (downloadEvent.eventId == DownloadEvent.EVENT_DOWNLOAD_GLOBAL) {
                DownloadState state = downloadEvent.result.downloadState;
                mStates.progress.set(state.progress);
                mStates.enableGlobalDownload.set(state.progress == 100 || state.progress == 0);
            }
        });
    }

    // TODO tip 4：此处通过 DataBinding 规避 setOnClickListener 时存在的 View 实例 Null 安全一致性问题，

    // 也即，有视图就绑定，无就无绑定，总之 不会因不一致性造成 View 实例 Null 安全问题。
    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

    public class ClickProxy {

        public void back() {
            nav().navigateUp();
        }

        public void testNav() {
            openUrlInBrowser(Const.COLUMN_LINK);
        }

        public void subscribe() {
            openUrlInBrowser(Const.COLUMN_LINK);
        }

        //TODO tip 8: 此处演示使用 "唯一可信源" MVI-Dispatcher input-output 接口完成数据请求响应

        //如这么说无体会，详见《领域层设计》篇拆解 https://juejin.cn/post/7117498113983512589

        public void testDownload() {
            mGlobalDownloadRequester.input(new DownloadEvent(DownloadEvent.EVENT_DOWNLOAD_GLOBAL));
        }

        //TODO tip 5: 在 UseCase 中 执行可跟随生命周期中止的下载任务

        public void testLifecycleDownload() {
            mDownloadRequester.input(new DownloadEvent(DownloadEvent.EVENT_DOWNLOAD));
        }
    }

    //TODO tip 6：每个页面都需单独准备一个 state-ViewModel，托管 DataBinding 绑定的 State，
    // 此外，state-ViewModel 职责仅限于状态托管和保存恢复，不建议在此处理 UI 逻辑，
    // UI 逻辑只适合在 Activity/Fragment 等视图控制器中完成，是 “数据驱动” 一部分，将来升级到 Jetpack Compose 更是如此。

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

    public static class SearchStates extends StateHolder {

        //TODO tip 7：此处我们使用 "去除防抖特性" 的 ObservableField 子类 State，用以代替 MutableLiveData，

        //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

        public final State<Integer> progress = new State<>(0);

        public final State<Integer> progress_cancelable = new State<>(0);

        public final State<Boolean> enableDownload = new State<>(true);

        public final State<Boolean> enableGlobalDownload = new State<>(true);
    }
}
