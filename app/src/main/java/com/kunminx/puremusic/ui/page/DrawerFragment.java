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
import androidx.recyclerview.widget.RecyclerView;

import com.kunminx.architecture.ui.adapter.SimpleBaseBindingAdapter;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.bridge.request.InfoRequestViewModel;
import com.kunminx.puremusic.bridge.state.DrawerViewModel;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.databinding.AdapterLibraryBinding;
import com.kunminx.puremusic.ui.base.BaseFragment;
import com.kunminx.puremusic.ui.base.DataBindingConfig;

/**
 * Create by KunMinX at 19/10/29
 */
public class DrawerFragment extends BaseFragment {

    private DrawerViewModel mDrawerViewModel;
    private InfoRequestViewModel mInfoRequestViewModel;
    private SimpleBaseBindingAdapter<LibraryInfo, AdapterLibraryBinding> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInfoRequestViewModel = getFragmentViewModel(InfoRequestViewModel.class);
        mDrawerViewModel = getFragmentViewModel(DrawerViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO 2020.4.18:
        // 将 DataBinding 实例限制于 base 页面中，不上升为类成员，更不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图刷新的安全性将和基于函数式编程的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供最少必要的绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_drawer, mDrawerViewModel, new ClickProxy());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new SimpleBaseBindingAdapter<LibraryInfo, AdapterLibraryBinding>(getContext(), R.layout.adapter_library) {
            @Override
            protected void onSimpleBindItem(AdapterLibraryBinding binding, LibraryInfo item, RecyclerView.ViewHolder holder) {
                binding.setInfo(item);
                binding.getRoot().setOnClickListener(v -> {
                    Uri uri = Uri.parse(item.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                });
            }
        };

        mBinding.rv.setAdapter(mAdapter);

        mInfoRequestViewModel.getLibraryLiveData().observe(getViewLifecycleOwner(), libraryInfos -> {
            mInitDataCame = true;
            if (mAnimationLoaded && libraryInfos != null) {
                //noinspection unchecked
                mAdapter.setList(libraryInfos);
                mAdapter.notifyDataSetChanged();
            }
        });

        mInfoRequestViewModel.requestLibraryInfo();
    }

    @Override
    public void loadInitData() {
        super.loadInitData();
        if (mInfoRequestViewModel.getLibraryLiveData().getValue() != null) {
            //noinspection unchecked
            mAdapter.setList(mInfoRequestViewModel.getLibraryLiveData().getValue());
            mAdapter.notifyDataSetChanged();
        }
    }

    public class ClickProxy extends BaseFragment.ClickProxy {

        public void logoClick() {
            String u = "https://github.com/KunMinX/Jetpack-MVVM-Best-Practice";
            Uri uri = Uri.parse(u);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

}
