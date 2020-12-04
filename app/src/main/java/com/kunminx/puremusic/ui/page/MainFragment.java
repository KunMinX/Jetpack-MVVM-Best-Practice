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
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.callback.SharedViewModel;
import com.kunminx.puremusic.ui.page.adapter.PlaylistAdapter;
import com.kunminx.puremusic.ui.state.MainViewModel;

/**
 * Create by KunMinX at 19/10/29
 */
public class MainFragment extends BaseFragment {

    //TODO tip 1：每个页面都要单独配备一个 state-ViewModel，职责仅限于 "状态托管和恢复"，
    //callback-ViewModel 则是用于在 "跨页面通信" 的场景下，承担 "唯一可信源"，

    //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/8204519736

    private MainViewModel mState;
    private SharedViewModel mPageCallback;

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(MainViewModel.class);
        mPageCallback = getApplicationScopeViewModel(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_main, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.adapter, new PlaylistAdapter(getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO tip 3：所有播放状态的改变，都要通过这个 作为 唯一可信源 的 PlayerManager 来统一分发，

        // 如此才能确保 消息同步的一致性 和 可靠性，以及 避免 不可预期的 推送和错误。
        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249

        PlayerManager.getInstance().getChangeMusicLiveData().observe(getViewLifecycleOwner(), changeMusic -> {
            mState.notifyCurrentListChanged.setValue(true);
        });

        //TODO tip :
        // getViewLifeCycleOwner 是 2020 年新增的特性，
        // 主要是为了解决 getView() 的生命长度 比 fragment 短（仅存活于 onCreateView 之后和 onDestroyView 之前），
        // 导致某些时候 fragment 其他成员还活着，但 getView() 为 null 的 生命周期安全问题，
        // 也即，在 fragment 的场景下，请使用 getViewLifeCycleOwner 来作为 liveData 的观察者。
        // Activity 则不用改变。

        mState.musicRequest.getFreeMusicsLiveData().observe(getViewLifecycleOwner(), dataResult -> {
            if (!dataResult.getResponseStatus().isSuccess()) return;

            TestAlbum musicAlbum = dataResult.getResult();

            // TODO tip 4：未作 UnPeek 处理的 用于 request 的 LiveData，在视图控制器重建时会自动倒灌数据
            // 请记得这一点，因为如果没有妥善处理，这里就可能出现预期外的错误（例如收到旧数据的推送），
            // 再一次地，请记得它在重建时 是一定会倒灌的。

            // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/6719328450

            if (musicAlbum != null && musicAlbum.getMusics() != null) {
                mState.list.setValue(musicAlbum.getMusics());

                if (PlayerManager.getInstance().getAlbum() == null ||
                        !PlayerManager.getInstance().getAlbum().getAlbumId().equals(musicAlbum.getAlbumId())) {
                    PlayerManager.getInstance().loadAlbum(musicAlbum);
                }
            }
        });

        if (PlayerManager.getInstance().getAlbum() == null) {

            //TODO tip 5：将 request 作为 state-ViewModel 的成员暴露给 Activity/Fragment，
            // 如此便于语义的明确，以及实现多个 request 在 state-ViewModel 中的组合和复用。

            //如果这样说还不理解的话，详见《如何让同事爱上架构模式、少写 bug 多注释》的解析
            //https://xiaozhuanlan.com/topic/8204519736

            mState.musicRequest.requestFreeMusics();

        } else {

            //TODO tip 6："唯一可信源"的理念仅适用于"跨域通信"的场景，
            // state-ViewModel 与"跨域通信"的场景无关，其所持有的 LiveData 仅用于"无防抖加持"的视图状态绑定用途
            // （也即它是用于在不适合防抖加持的场景下替代"自带防抖特性的 ObservableField"），
            // 因而此处 LiveData 可以直接在页面内 setValue：所通知的目标不包含其他页面的状态，而仅是当前页内部的状态。

            // 如果这样说还不理解的话，详见《LiveData》篇和《DataBinding》篇的解析
            // https://xiaozhuanlan.com/topic/0168753249、https://xiaozhuanlan.com/topic/9816742350

            mState.list.setValue(PlayerManager.getInstance().getAlbum().getMusics());
        }
    }


    // TODO tip 7：此处通过 DataBinding 来规避 在 setOnClickListener 时存在的 视图调用的一致性问题，

    // 也即，有绑定就有绑定，没绑定也没什么大不了的，总之 不会因一致性问题造成 视图调用的空指针。
    // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

    public class ClickProxy {

        public void openMenu() {

            // TODO tip 8：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、确保消息同步一致性和可靠性的 通知。

            // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一个思想是 最少知道原则，
            // Activity 内部的事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部的东西。
            // 因为 Activity 端的处理后续可能会改变，并且可受用于更多的 fragment，而不单单是本 fragment。

            mPageCallback.requestToOpenOrCloseDrawer(true);
        }

        public void login() {
            nav().navigate(R.id.action_mainFragment_to_loginFragment);
        }

        public void search() {
            nav().navigate(R.id.action_mainFragment_to_searchFragment);
        }

    }

}
