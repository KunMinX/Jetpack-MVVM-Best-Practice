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

import com.kunminx.puremusic.ui.callback.SharedViewModel;

/**
 * TODO tip：通过 Lifecycle 来解决抽屉侧滑禁用与否的判断的 一致性问题，
 * <p>
 * 每个需要注册和监听生命周期来判断的视图控制器，无需在各自内部手动书写解绑等操作。
 * 如果这样说还不理解，详见 https://xiaozhuanlan.com/topic/3684721950
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

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

        SharedViewModel.TAG_OF_SECONDARY_PAGES.add(owner.getClass().getSimpleName());

        SharedViewModel.ENABLE_SWIPE_DRAWER
                .setValue(SharedViewModel.TAG_OF_SECONDARY_PAGES.size() == 0);

    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {

        SharedViewModel.TAG_OF_SECONDARY_PAGES.remove(owner.getClass().getSimpleName());

        SharedViewModel.ENABLE_SWIPE_DRAWER
                .setValue(SharedViewModel.TAG_OF_SECONDARY_PAGES.size() == 0);

    }

}
