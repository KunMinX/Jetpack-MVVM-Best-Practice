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
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.callback.SharedViewModel;
import com.kunminx.puremusic.ui.page.adapter.PlaylistAdapter;
import com.kunminx.puremusic.ui.state.MainViewModel;

/**
 * Create by KunMinX at 19/10/29
 */
public class MainFragment extends BaseFragment {

    private MainViewModel mMainViewModel;
    private SharedViewModel mSharedViewModel;

    @Override
    protected void initViewModel() {
        mMainViewModel = getFragmentViewModel(MainViewModel.class);
        mSharedViewModel = getAppViewModelProvider().get(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图刷新的安全性将和基于函数式编程的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_main, BR.vm, mMainViewModel)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.adapter, new PlaylistAdapter(getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlayerManager.getInstance().getChangeMusicLiveData().observe(getViewLifecycleOwner(), changeMusic -> {

            // TODO tip 1：所有播放状态的改变，都要通过这个 作为 唯一可信源 的 PlayerManager 来统一分发，

            // 如此才能方便 追溯事件源，以及 避免 不可预期的 推送和错误。
            // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249

            mMainViewModel.notifyCurrentListChanged.setValue(true);
        });

        mMainViewModel.musicRequest.getFreeMusicsLiveData().observe(getViewLifecycleOwner(), musicAlbum -> {
            if (musicAlbum != null && musicAlbum.getMusics() != null) {
                mMainViewModel.list.setValue(musicAlbum.getMusics());

                // TODO tip 4：未作 UnPeek 处理的 用于 request 的 LiveData，在视图控制器重建时会自动倒灌数据

                // 一定要记住这一点，因为如果没有妥善处理，这里就会出现预期外的错误，一定要记得它在重建时 是一定会倒灌的。

                // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0129483567

                if (PlayerManager.getInstance().getAlbum() == null ||
                        !PlayerManager.getInstance().getAlbum().getAlbumId().equals(musicAlbum.getAlbumId())) {
                    PlayerManager.getInstance().loadAlbum(musicAlbum);
                }
            }
        });

        if (PlayerManager.getInstance().getAlbum() == null) {

            //TODO 将 request 作为 ViewModel 的成员暴露给 Activity/Fragment，
            // 如此便于语义的明确，以及实现多个 request 在 ViewModel 中的组合和复用。

            mMainViewModel.musicRequest.requestFreeMusics();
        } else {
            mMainViewModel.list.setValue(PlayerManager.getInstance().getAlbum().getMusics());
        }
    }


    // TODO tip 2：此处通过 DataBinding 来规避 在 setOnClickListener 时存在的 视图调用的一致性问题，

    // 也即，有绑定就有绑定，没绑定也没什么大不了的，总之 不会因一致性问题造成 视图调用的空指针。
    // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

    public class ClickProxy {

        public void openMenu() {

            // TODO tip 3：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

            // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一个思想是 最少知道原则，
            // Activity 内部的事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部的东西。
            // 因为 Activity 端的处理后续可能会改变，并且可受用于更多的 fragment，而不单单是本 fragment。

            mSharedViewModel.openOrCloseDrawer.setValue(true);
        }

        public void login() {
            nav().navigate(R.id.action_mainFragment_to_loginFragment);
        }

        public void search() {
            nav().navigate(R.id.action_mainFragment_to_searchFragment);
        }

    }

}
