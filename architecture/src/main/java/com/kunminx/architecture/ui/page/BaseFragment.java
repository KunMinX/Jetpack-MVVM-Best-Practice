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

package com.kunminx.architecture.ui.page;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kunminx.architecture.domain.manager.NetState;
import com.kunminx.architecture.domain.manager.NetworkStateManager;
import com.kunminx.architecture.ui.page.DataBindingFragment;


/**
 * Create by KunMinX at 19/7/11
 */
public abstract class BaseFragment extends DataBindingFragment {

    //TODO tip: DataBinding 严格模式（详见 DataBindingFragment - - - - - ）：
    // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
    // 通过这样的方式，来彻底解决 视图调用的一致性问题，
    // 如此，视图刷新的安全性将和基于函数式编程的 Jetpack Compose 持平。

    // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO 注意 liveData 的 lambda 回调中不可空实现，不然会出现 Cannot add the same observer with different lifecycles 的现象，
        // 详见：https://stackoverflow.com/questions/47025233/android-lifecycle-library-cannot-add-the-same-observer-with-different-lifecycle
        NetworkStateManager.getInstance().networkStateCallback.observe(getViewLifecycleOwner(), netState -> {
            if (!isHidden()) {
                onNetworkStateChanged(netState);
            }
        });
    }

    @SuppressWarnings("EmptyMethod")
    protected void onNetworkStateChanged(NetState netState) {
        //TODO 子类可以重写该方法，统一的网络状态通知和处理

    }

    protected NavController nav() {
        return NavHostFragment.findNavController(this);
    }

    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
