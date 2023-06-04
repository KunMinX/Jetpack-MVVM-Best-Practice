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
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.config.Const;
import com.kunminx.puremusic.domain.request.InfoRequester;
import com.kunminx.puremusic.ui.page.adapter.DrawerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 19/10/29
 */
public class DrawerFragment extends BaseFragment {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 result-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，作用域仅限于本页面，承担对本页面 "各控件属性" 来说的 "唯一可信源"，
    // result-ViewModel 职责仅限于 "消息分发" 场景承担 "可信源"，作用域依 "数据请求" 或 "跨页通信" 消息分发范围而定

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private DrawerStates mStates;
    private InfoRequester mInfoRequester;

    @Override
    protected void initViewModel() {
        mStates = getFragmentScopeViewModel(DrawerStates.class);
        mInfoRequester = getFragmentScopeViewModel(InfoRequester.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_drawer, BR.vm, mStates)
            .addBindingParam(BR.click, new ClickProxy())
            .addBindingParam(BR.adapter, new DrawerAdapter(getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO tip 3: 从 PublishSubject 接收回推的数据，并在回调中响应数据的变化，
        // 也即通过 BehaviorSubject 通知控件属性重新渲染，并为其兜住最后一次状态，

        //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805

        mInfoRequester.getLibraryResult().observe(getViewLifecycleOwner(), dataResult -> {
            if (!dataResult.getResponseStatus().isSuccess()) return;
            if (dataResult.getResult() != null) mStates.list.set(dataResult.getResult());
        });

        mInfoRequester.requestLibraryInfo();
    }

    public class ClickProxy {
        public void logoClick() {
            openUrlInBrowser(Const.PROJECT_LINK);
        }
    }

    //TODO tip 5：基于单一职责原则，抽取 Jetpack ViewModel "状态保存和恢复" 的能力作为 StateHolder，
    // 并使用去除防抖设计的 ObservableField 子类 State 来承担 BehaviorSubject，作为所绑定控件的 "可信数据源"，
    // 用于在收到来自 PublishSubject 的结果回推后，响应结果数据的变化，也即通知控件属性重新渲染，并为其兜住最后一次状态，

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805

    public static class DrawerStates extends StateHolder {
        public final State<List<LibraryInfo>> list = new State<>(new ArrayList<>());
    }
}
