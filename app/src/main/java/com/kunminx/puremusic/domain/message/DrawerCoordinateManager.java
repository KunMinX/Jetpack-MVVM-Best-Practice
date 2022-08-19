/*
 *
 *  * Copyright 2018-present KunMinX
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.kunminx.puremusic.domain.message;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.domain.message.MutableResult;
import com.kunminx.architecture.domain.message.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO tip 1：通过 Lifecycle 来解决抽屉侧滑禁用与否的判断的一致性问题，
 * <p>
 * 每个需要注册和监听生命周期来判断的视图控制器，无需在各自内部手动书写解绑等操作。
 * 如这么说无体会，详见《为你还原一个真实的 Jetpack Lifecycle》
 * https://xiaozhuanlan.com/topic/3684721950
 * <p>
 * TODO tip 2：与此同时，作为用于 "跨页面通信" 单例，本类也承担 "唯一可信源" 职责，
 * 所有对 Drawer 状态协调相关的请求都交由本单例处理，并统一分发给所有订阅者页面。
 * <p>
 * 如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
 * https://xiaozhuanlan.com/topic/6017825943
 * <p>
 * <p>
 * Create by KunMinX at 19/11/3
 */
public class DrawerCoordinateManager implements DefaultLifecycleObserver {

    private static final DrawerCoordinateManager S_HELPER = new DrawerCoordinateManager();

    private DrawerCoordinateManager() {
    }

    public static DrawerCoordinateManager getInstance() {
        return S_HELPER;
    }

    private final List<String> tagOfSecondaryPages = new ArrayList<>();

    private boolean isNoneSecondaryPage() {
        return tagOfSecondaryPages.size() == 0;
    }

    private final MutableResult<Boolean> enableSwipeDrawer = new MutableResult<>();

    public Result<Boolean> isEnableSwipeDrawer() {
        return enableSwipeDrawer;
    }

    public void requestToUpdateDrawerMode(boolean pageOpened, String pageName) {
        if (pageOpened) {
            tagOfSecondaryPages.add(pageName);
        } else {
            tagOfSecondaryPages.remove(pageName);
        }
        enableSwipeDrawer.setValue(isNoneSecondaryPage());
    }

    //TODO tip 3：让 NetworkStateManager 可观察页面生命周期，
    // 从而在进入或离开目标页面时，自动在此登记和处理抽屉的禁用和解禁，避免一系列不可预期问题。

    // 关于 Lifecycle 组件的存在意义，可详见《为你还原一个真实的 Jetpack Lifecycle》解析
    // https://xiaozhuanlan.com/topic/3684721950

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

        tagOfSecondaryPages.add(owner.getClass().getSimpleName());

        enableSwipeDrawer.setValue(isNoneSecondaryPage());

    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {

        tagOfSecondaryPages.remove(owner.getClass().getSimpleName());

        enableSwipeDrawer.setValue(isNoneSecondaryPage());

    }

}
