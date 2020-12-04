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
import com.kunminx.puremusic.ui.page.adapter.DrawerAdapter;
import com.kunminx.puremusic.ui.state.DrawerViewModel;

/**
 * Create by KunMinX at 19/10/29
 */
public class DrawerFragment extends BaseFragment {

    //TODO tip 1：每个页面都要单独配备一个 state-ViewModel，职责仅限于 "状态托管和恢复"，
    //callback-ViewModel 则是用于在 "跨页面通信" 的场景下，承担 "唯一可信源"，

    //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/8204519736

    private DrawerViewModel mState;

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(DrawerViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip 1: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_drawer, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.adapter, new DrawerAdapter(getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO tip 2：将 request 作为 state-ViewModel 的成员暴露给 Activity/Fragment，
        // 如此便于语义的明确，以及实现多个 request 在 state-ViewModel 中的组合和复用。

        //如果这样说还不理解的话，详见《如何让同事爱上架构模式、少写 bug 多注释》的解析
        //https://xiaozhuanlan.com/topic/8204519736

        mState.infoRequest.getLibraryLiveData().observe(getViewLifecycleOwner(), dataResult -> {
            if (!dataResult.getResponseStatus().isSuccess()) return;

            if (mAnimationLoaded && dataResult.getResult() != null) {

                //TODO tip 3："唯一可信源"的理念仅适用于"跨域通信"的场景，
                // state-ViewModel 与"跨域通信"的场景无关，其所持有的 LiveData 仅用于"无防抖加持"的视图状态绑定用途
                // （也即它是用于在不适合防抖加持的场景下替代"自带防抖特性的 ObservableField"），
                // 因而此处 LiveData 可以直接在页面内 setValue：所通知的目标不包含其他页面的状态，而是当前页内部的状态。

                // 如果这样说还不理解的话，详见《LiveData》篇和《DataBinding》篇的解析
                // https://xiaozhuanlan.com/topic/0168753249、https://xiaozhuanlan.com/topic/9816742350

                mState.list.setValue(dataResult.getResult());
            }
        });

        if (mState.infoRequest.getLibraryLiveData().getValue() == null) {
            mState.infoRequest.requestLibraryInfo();
        }
    }

    @Override
    public void loadInitData() {
        super.loadInitData();
        if (mState.infoRequest.getLibraryLiveData().getValue() != null
                && mState.infoRequest.getLibraryLiveData().getValue().getResult() != null) {
            mState.list.setValue(mState.infoRequest.getLibraryLiveData().getValue().getResult());
        }
    }

    public class ClickProxy {

        public void logoClick() {
            openUrlInBrowser(getString(R.string.github_project));
        }
    }

}
