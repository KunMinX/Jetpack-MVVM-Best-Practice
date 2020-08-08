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

package com.kunminx.puremusic.ui.callback;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.callback.UnPeekLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO tip：callback-ViewModel 的职责仅限于 页面通信，不建议在此处理 UI 逻辑，
 * UI 逻辑只适合在 Activity/Fragment 等视图控制器中完成，是 “数据驱动” 的一部分，
 * 将来升级到 Jetpack Compose 更是如此。
 * <p>
 * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/6257931840
 * <p>
 * <p>
 * Create by KunMinX at 19/10/16
 */
public class SharedViewModel extends ViewModel {

    // TODO tip 1：此处演示通过 UnPeekLiveData 配合 SharedViewModel 来发送 生命周期安全的、事件源可追溯的 通知。

    //TODO tip 2：并且，在页面通信的场景下，使用全局 ViewModel，是因为它被封装在 base 页面中，避免页面之外的组件拿到，从而造成不可预期的推送，
    // 而且尽可能使用单例或 ViewModel 托管 liveData，这样调试时能根据内存中的 liveData 对象找到事件源，
    // liveDataBus 这种通过 tag 来标记的，难以找到。

    // 如果这么说还不理解的话，
    // 详见 https://xiaozhuanlan.com/topic/0168753249 和 https://xiaozhuanlan.com/topic/6257931840

    public static final List<String> TAG_OF_SECONDARY_PAGES = new ArrayList<>();

    //TODO：可以通过构造器的方式来赋予 UnPeekLiveData 消息生存期
    public static final UnPeekLiveData<Boolean> ENABLE_SWIPE_DRAWER =
            new UnPeekLiveData.Builder<Boolean>().setEventSurvivalTime(500).create();

    public final ObservableBoolean isDrawerOpened = new ObservableBoolean();
    public final UnPeekLiveData<Boolean> timeToAddSlideListener = new UnPeekLiveData<>();
    public final UnPeekLiveData<Boolean> closeSlidePanelIfExpanded = new UnPeekLiveData<>();
    public final UnPeekLiveData<Boolean> activityCanBeClosedDirectly = new UnPeekLiveData<>();
    public final UnPeekLiveData<Boolean> openOrCloseDrawer = new UnPeekLiveData<>();

}
