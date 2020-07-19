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

import android.content.Intent;
import android.net.Uri;
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

    private DrawerViewModel mDrawerViewModel;

    @Override
    protected void initViewModel() {
        mDrawerViewModel = getFragmentViewModel(DrawerViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图刷新的安全性将和基于函数式编程的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_drawer, BR.vm, mDrawerViewModel)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.adapter, new DrawerAdapter(getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO 将 request 作为 ViewModel 的成员暴露给 Activity/Fragment，
        // 如此便于语义的明确，以及实现多个 request 在 ViewModel 中的组合和复用。

        mDrawerViewModel.infoRequest.getLibraryLiveData().observe(getViewLifecycleOwner(), libraryInfos -> {
            if (mAnimationLoaded && libraryInfos != null) {
                mDrawerViewModel.list.setValue(libraryInfos);
            }
        });

        mDrawerViewModel.infoRequest.requestLibraryInfo();
    }

    @Override
    public void loadInitData() {
        super.loadInitData();
        if (mDrawerViewModel.infoRequest.getLibraryLiveData().getValue() != null) {
            mDrawerViewModel.list.setValue(mDrawerViewModel.infoRequest.getLibraryLiveData().getValue());
        }
    }

    public class ClickProxy {

        public void logoClick() {
            String u = "https://github.com/KunMinX/Jetpack-MVVM-Best-Practice";
            Uri uri = Uri.parse(u);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

}
