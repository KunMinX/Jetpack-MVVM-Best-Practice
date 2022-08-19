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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.architecture.ui.state.State;
import com.kunminx.player.domain.PlayerEvent;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.domain.event.Messages;
import com.kunminx.puremusic.domain.message.PageMessenger;
import com.kunminx.puremusic.domain.request.MusicRequester;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.page.adapter.PlaylistAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by KunMinX at 19/10/29
 */
public class MainFragment extends BaseFragment {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 event-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，
    // event-ViewModel 职责仅限于 "消息分发" 场景承担 "唯一可信源"。

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private MainStates mStates;
    private PageMessenger mMessenger;
    private MusicRequester mMusicRequester;
    private PlaylistAdapter mAdapter;

    @Override
    protected void initViewModel() {
        mStates = getFragmentScopeViewModel(MainStates.class);
        mMessenger = getApplicationScopeViewModel(PageMessenger.class);
        mMusicRequester = getFragmentScopeViewModel(MusicRequester.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        mAdapter = new PlaylistAdapter(getContext());
        mAdapter.setOnItemClickListener((viewId, item, position) -> {
            PlayerManager.getInstance().playAudio(position);
        });

        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_main, BR.vm, mStates)
            .addBindingParam(BR.click, new ClickProxy())
            .addBindingParam(BR.adapter, mAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO tip 3：所有播放状态的改变，皆来自 "唯一可信源" PlayerManager 统一分发，
        //  确保 "消息分发可靠一致"，避免不可预期推送和错误。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/6017825943 & https://juejin.cn/post/7117498113983512589

        PlayerManager.getInstance().getDispatcher().output(this, playerEvent -> {
            if (playerEvent.eventId == PlayerEvent.EVENT_CHANGE_MUSIC) {
                mAdapter.notifyDataSetChanged();
            }
        });

        //TODO tip 4:
        // getViewLifeCycleOwner 是 2020 年新增特性，
        // 主要为了解决 getView() 生命长度 比 fragment 短（仅存活于 onCreateView 之后和 onDestroyView 之前），
        // 导致某些时候 fragment 其他成员还活着，但 getView() 为 null 的 生命周期安全问题，
        // 也即，在 fragment 场景下，请使用 getViewLifeCycleOwner 作为 liveData 观察者。
        // Activity 则不用改变。

        mMusicRequester.getFreeMusicsResult().observe(getViewLifecycleOwner(), dataResult -> {
            if (!dataResult.getResponseStatus().isSuccess()) return;

            TestAlbum musicAlbum = dataResult.getResult();

            // TODO tip 5：未作 UnPeek 处理的 LiveData，在视图控制器重建时会自动倒灌数据
            // 请记得这一点，因为如果没有妥善处理，这里就可能出现预期外错误（例如收到旧数据推送），
            // 所以，再一次，请记得它在重建时一定会倒灌。

            // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/6719328450

            if (musicAlbum != null && musicAlbum.getMusics() != null) {
                mStates.list.set(musicAlbum.getMusics());
                PlayerManager.getInstance().loadAlbum(musicAlbum);
            }
        });

        if (PlayerManager.getInstance().getAlbum() == null) mMusicRequester.requestFreeMusics();
    }

    // TODO tip 7：此处通过 DataBinding 规避 setOnClickListener 时存在的 View 实例 Null 安全一致性问题，

    // 也即，有视图就绑定，无就无绑定，总之 不会因不一致性造成 View 实例 Null 安全问题。
    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

    public class ClickProxy {

        public void openMenu() {

            // TODO tip 8：此处演示向 "唯一可信源" 发送请求，以便实现 "生命周期安全、消息分发可靠一致" 的通知。

            // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/6017825943 & https://juejin.cn/post/7117498113983512589
            // --------
            // 与此同时，此处传达的另一思想是 "最少知道原则"，
            // Activity 内部事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部东西。
            // 因为 Activity 端的处理后续可能会改变，且可受用于更多 fragment，而不单单是本 fragment。

            mMessenger.input(new Messages(Messages.EVENT_OPEN_DRAWER));
        }

        public void login() {
            nav().navigate(R.id.action_mainFragment_to_loginFragment);
        }

        public void search() {
            nav().navigate(R.id.action_mainFragment_to_searchFragment);
        }

    }

    //TODO tip 9：每个页面都需单独准备一个 state-ViewModel，托管 DataBinding 绑定的 State，
    // 此外，state-ViewModel 职责仅限于状态托管和保存恢复，不建议在此处理 UI 逻辑，
    // UI 逻辑只适合在 Activity/Fragment 等视图控制器中完成，是 “数据驱动” 一部分，将来升级到 Jetpack Compose 更是如此。

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

    public static class MainStates extends StateHolder {

        //TODO tip 10：此处我们使用 "去除防抖特性" 的 ObservableField 子类 State，用以代替 MutableLiveData，

        //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

        public final State<Boolean> initTabAndPage = new State<>(true);

        public final State<String> pageAssetPath = new State<>("summary.html");

        public final State<List<TestAlbum.TestMusic>> list = new State<>(new ArrayList<>());

    }

}
