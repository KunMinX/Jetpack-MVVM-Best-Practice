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

import com.kunminx.puremusic.R;
import com.kunminx.puremusic.bridge.state.SearchViewModel;
import com.kunminx.puremusic.databinding.FragmentSearchBinding;
import com.kunminx.puremusic.ui.base.BaseFragment;
import com.kunminx.puremusic.ui.helper.DrawerCoordinateHelper;

/**
 * Create by KunMinX at 19/10/29
 */
public class SearchFragment extends BaseFragment {

    private FragmentSearchBinding mBinding;
    private SearchViewModel mSearchViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        getLifecycle().addObserver(DrawerCoordinateHelper.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mBinding = FragmentSearchBinding.bind(view);
        mBinding.setClick(new ClickProxy());
        mBinding.setVm(mSearchViewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public class ClickProxy {

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
    }
}
