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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.kunminx.architecture.ui.adapter.SimpleBaseBindingAdapter;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.bridge.request.InfoRequestViewModel;
import com.kunminx.puremusic.bridge.state.DrawerViewModel;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.databinding.AdapterLibraryBinding;
import com.kunminx.puremusic.databinding.FragmentDrawerBinding;
import com.kunminx.puremusic.ui.base.BaseFragment;

/**
 * Create by KunMinX at 19/10/29
 */
public class DrawerFragment extends BaseFragment {

    private FragmentDrawerBinding mBinding;
    private DrawerViewModel mDrawerViewModel;
    private InfoRequestViewModel mInfoRequestViewModel;
    private SimpleBaseBindingAdapter<LibraryInfo, AdapterLibraryBinding> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInfoRequestViewModel = ViewModelProviders.of(this).get(InfoRequestViewModel.class);
        mDrawerViewModel = ViewModelProviders.of(this).get(DrawerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        mBinding = FragmentDrawerBinding.bind(view);
        mBinding.setVm(mDrawerViewModel);
        mBinding.setClick(new ClickProxy());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO tip1：绑定跟随视图控制器生命周期的、可叫停的、单独放在 UseCase 中处理的业务
        getLifecycle().addObserver(mInfoRequestViewModel.getTestUseCase());

        mAdapter = new SimpleBaseBindingAdapter<LibraryInfo, AdapterLibraryBinding>(getContext(), R.layout.adapter_library) {
            @Override
            protected void onSimpleBindItem(AdapterLibraryBinding binding, LibraryInfo item, RecyclerView.ViewHolder holder) {
                binding.tvTitle.setText(item.getTitle());
                binding.tvSummary.setText(item.getSummary());
                binding.getRoot().setOnClickListener(v -> {
                    Uri uri = Uri.parse(item.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                });
            }
        };

        mBinding.rv.setAdapter(mAdapter);

        mInfoRequestViewModel.getLibraryLiveData().observe(this, libraryInfos -> {
            mInitDataCame = true;
            if (mAnimationLoaded && libraryInfos != null) {
                mAdapter.setList(libraryInfos);
                mAdapter.notifyDataSetChanged();
            }
        });

        mInfoRequestViewModel.requestLibraryInfo();

        mInfoRequestViewModel.getTestXXX().observe(this, s -> {
            //TODO tip3：暂无实际功能，仅演示 UseCase 流程

            //接收来自 可感知生命周期的 UseCase 处理的结果
        });

        //TODO tip2：暂无实际功能，仅演示 UseCase 流程
        mInfoRequestViewModel.requestTestXXX();
    }

    @Override
    public void loadInitData() {
        super.loadInitData();
        if (mInfoRequestViewModel.getLibraryLiveData().getValue() != null) {
            mAdapter.setList(mInfoRequestViewModel.getLibraryLiveData().getValue());
            mAdapter.notifyDataSetChanged();
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
