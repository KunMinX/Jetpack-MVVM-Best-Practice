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

package com.kunminx.puremusic.ui.helper;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData;
import com.kunminx.architecture.ui.callback.UnPeekLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO tip 1：通过 Lifecycle 来解决抽屉侧滑禁用与否的判断的 一致性问题，
 * <p>
 * 每个需要注册和监听生命周期来判断的视图控制器，无需在各自内部手动书写解绑等操作。
 * 如果这样说还不理解，详见《为你还原一个真实的 Jetpack Lifecycle》
 * https://xiaozhuanlan.com/topic/3684721950
 * <p>
 * TODO tip 2：与此同时，作为用于 "跨页面通信" 的单例，本类也承担了 "唯一可信源" 的职责，
 * 所有对 Drawer 状态协调相关的请求都交由本单例处理，并统一分发给所有订阅者页面。
 * <p>
 * 如果这样说还不理解的话，详见《LiveData 鲜为人知的 身世背景 和 独特使命》中结合实际场合 对"唯一可信源"本质的解析。
 * https://xiaozhuanlan.com/topic/0168753249
 * <p>
 * <p>
 * Create by KunMinX at 19/11/3
 */
public class DrawerCoordinateHelper implements DefaultLifecycleObserver {

    private static final DrawerCoordinateHelper S_HELPER = new DrawerCoordinateHelper();

    private DrawerCoordinateHelper() {
    }

    public static DrawerCoordinateHelper getInstance() {
        return S_HELPER;
    }

    private final List<String> tagOfSecondaryPages = new ArrayList<>();

    private final UnPeekLiveData<Boolean> enableSwipeDrawer = new UnPeekLiveData<>();

    public ProtectedUnPeekLiveData<Boolean> isEnableSwipeDrawer() {
        return enableSwipeDrawer;
    }

    public void requestToUpdateDrawerMode(boolean pageOpened, String pageName) {
        if (pageOpened) {
            tagOfSecondaryPages.add(pageName);
        } else {
            tagOfSecondaryPages.remove(pageName);
        }
        enableSwipeDrawer.setValue(tagOfSecondaryPages.size() == 0);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

        tagOfSecondaryPages.add(owner.getClass().getSimpleName());

        enableSwipeDrawer.setValue(tagOfSecondaryPages.size() == 0);

    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {

        tagOfSecondaryPages.remove(owner.getClass().getSimpleName());

        enableSwipeDrawer.setValue(tagOfSecondaryPages.size() == 0);

    }

}
