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
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.data.config.utils.KeyValueProvider;
import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.architecture.ui.state.State;
import com.kunminx.architecture.utils.ToastUtils;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.bean.User;
import com.kunminx.puremusic.data.config.Configs;
import com.kunminx.puremusic.domain.event.Messages;
import com.kunminx.puremusic.domain.message.DrawerCoordinateManager;
import com.kunminx.puremusic.domain.message.PageMessenger;
import com.kunminx.puremusic.domain.request.AccountRequester;

/**
 * Create by KunMinX at 20/04/26
 */
public class LoginFragment extends BaseFragment {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 result-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，作用域仅限于本页面，
    // result-ViewModel 职责仅限于 "消息分发" 场景承担 "可信源"，作用域依 "数据请求" 或 "跨页通信" 消息分发范围而定

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private LoginStates mStates;
    private AccountRequester mAccountRequester;
    private PageMessenger mMessenger;
    private final Configs mConfigs = KeyValueProvider.get(Configs.class);

    @Override
    protected void initViewModel() {
        mStates = getFragmentScopeViewModel(LoginStates.class);
        mMessenger = getApplicationScopeViewModel(PageMessenger.class);
        mAccountRequester = getFragmentScopeViewModel(AccountRequester.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_login, BR.vm, mStates)
            .addBindingParam(BR.click, new ClickProxy());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(DrawerCoordinateManager.getInstance());

        //TODO tip 3：让 accountRequest 可观察页面生命周期，
        // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
        // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期问题。

        getLifecycle().addObserver(mAccountRequester);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO tip 4: 从可信源 Requester 通过 immutable Result 获取请求结果的只读数据，set 给 mutable State，
        //而非 Result、State 不分，直接在页面 set Result，

        //如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
        //https://xiaozhuanlan.com/topic/6017825943

        mAccountRequester.getTokenResult().observe(getViewLifecycleOwner(), dataResult -> {
            if (!dataResult.getResponseStatus().isSuccess()) {
                mStates.loadingVisible.set(false);
                ToastUtils.showLongToast(getString(R.string.network_state_retry));
                return;
            }

            String s = dataResult.getResult();
            if (TextUtils.isEmpty(s)) return;

            //TODO tip：成功获取 token 后，可通过 KeyValueX 框架存储配置，
            // 以及通过作用域为 Application 的 PageMessenger 框架通知其他页面刷新状态，
            // 具体详见 Configs 类和 PageMessenger 类说明

            mConfigs.token().set(s);
            mStates.loadingVisible.set(false);
            mMessenger.input(new Messages(Messages.EVENT_LOGIN_SUCCESS));
            nav().navigateUp();
        });
    }

    public class ClickProxy {

        public void back() {
            nav().navigateUp();
        }

        public void login() {

            //TODO tip 5：通过双向绑定，使能通过 state-ViewModel 中与 xml 控件发生绑定的"可观察数据" 拿到控件数据，
            // 避免直接接触控件实例而埋下 Null 安全一致性隐患。

            //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

            if (TextUtils.isEmpty(mStates.name.get()) || TextUtils.isEmpty(mStates.password.get())) {
                ToastUtils.showLongToast(getString(R.string.username_or_pwd_incomplete));
                return;
            }
            User user = new User(mStates.name.get(), mStates.password.get());
            mAccountRequester.requestLogin(user);
            mStates.loadingVisible.set(true);
        }
    }

    //TODO tip 6：基于单一职责原则，抽取 Jetpack ViewModel "状态保存和恢复" 的能力作为 StateHolder，
    // 并使用 ObservableField 的改良版子类 State 来承担 BehaviorSubject，用作所绑定控件的 "可信数据源"，
    // 从而在收到来自 PublishSubject 的结果回推后，响应结果数据的变化，也即通知控件属性重新渲染，并为其兜住最后一次状态，

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805

    public static class LoginStates extends StateHolder {

        public final State<String> name = new State<>("");

        public final State<String> password = new State<>("");

        public final State<Boolean> loadingVisible = new State<>(false);

    }

}
