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

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.utils.SPUtils;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.bean.User;
import com.kunminx.puremusic.data.config.Configs;
import com.kunminx.puremusic.ui.helper.DrawerCoordinateHelper;
import com.kunminx.puremusic.ui.state.LoginViewModel;

/**
 * Create by KunMinX at 20/04/26
 */
public class LoginFragment extends BaseFragment {

    //TODO tip 1：每个页面都要单独配备一个 state-ViewModel，职责仅限于 "状态托管和恢复"，
    //callback-ViewModel 则是用于在 "跨页面通信" 的场景下，承担 "唯一可信源"，

    //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/8204519736

    private LoginViewModel mState;

    @Override
    protected void initViewModel() {
        mState = getFragmentScopeViewModel(LoginViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图调用的安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_login, BR.vm, mState)
                .addBindingParam(BR.click, new ClickProxy());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(DrawerCoordinateHelper.getInstance());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO tip 2：将 request 作为 state-ViewModel 的成员暴露给 Activity/Fragment，
        // 如此便于语义的明确，以及实现多个 request 在 state-ViewModel 中的组合和复用。

        //如果这样说还不理解的话，详见《如何让同事爱上架构模式、少写 bug 多注释》的解析
        //https://xiaozhuanlan.com/topic/8204519736

        mState.accountRequest.getTokenLiveData().observe(getViewLifecycleOwner(), dataResult -> {
            if (!dataResult.getResponseStatus().isSuccess()) {
                mState.loadingVisible.set(false);
                showLongToast(getString(R.string.network_state_retry));
                return;
            }

            String s = dataResult.getResult();
            if (TextUtils.isEmpty(s)) return;

            SPUtils.getInstance().put(Configs.TOKEN, s);
            mState.loadingVisible.set(false);

            //TODO 登录成功后进行的下一步操作...
            nav().navigateUp();
        });
    }

    public class ClickProxy {

        public void back() {
            nav().navigateUp();
        }

        public void login() {

            //TODO tip 3：通过 xml 中的双向绑定，使得能够通过 state-ViewModel 中与控件发生绑定的"可观察数据"拿到控件的数据，
            // 避免直接接触控件实例而埋下视图调用的一致性隐患。

            //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

            if (TextUtils.isEmpty(mState.name.get()) || TextUtils.isEmpty(mState.password.get())) {
                showLongToast(getString(R.string.username_or_pwd_incomplete));
                return;
            }
            User user = new User(mState.name.get(), mState.password.get());
            mState.accountRequest.requestLogin(user);
            mState.loadingVisible.set(true);
        }

    }

}
