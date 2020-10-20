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

    private LoginViewModel mLoginViewModel;

    @Override
    protected void initViewModel() {
        mLoginViewModel = getFragmentViewModel(LoginViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图刷新的安全性将和基于函数式编程的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_login, BR.vm, mLoginViewModel)
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

        //TODO tip: request-ViewModel 和 state-ViewModel 边界分明、点到为止、各司其职，
        //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/6257931840

        mLoginViewModel.accountRequest.getTokenLiveData().observe(getViewLifecycleOwner(), s -> {
            if (TextUtils.isEmpty(s)) {
                return;
            }
            SPUtils.getInstance().put(Configs.TOKEN, s);
            mLoginViewModel.loadingVisible.set(false);

            //TODO 登录成功后进行的下一步操作...
            nav().navigateUp();
        });

        mLoginViewModel.accountRequest.getNetStateEvent().observe(getViewLifecycleOwner(), netState -> {
            mLoginViewModel.loadingVisible.set(false);
            if (!netState.isSuccess()) {
                showLongToast("网络状态不佳，请重试");
            }
        });
    }

    public class ClickProxy {

        public void back() {
            nav().navigateUp();
        }

        public void login() {

            //TODO 通过 xml 中的双向绑定，使得能够通过 stateViewModel 中与控件发生绑定的可观察数据 拿到控件的数据。避免直接接触控件实例而埋下一致性隐患。
            //如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

            if (TextUtils.isEmpty(mLoginViewModel.name.get()) || TextUtils.isEmpty(mLoginViewModel.password.get())) {
                showLongToast("用户名或密码不完整");
                return;
            }
            User user = new User(mLoginViewModel.name.get(), mLoginViewModel.password.get());
            mLoginViewModel.accountRequest.requestLogin(user);
            mLoginViewModel.loadingVisible.set(true);
        }

    }

}
