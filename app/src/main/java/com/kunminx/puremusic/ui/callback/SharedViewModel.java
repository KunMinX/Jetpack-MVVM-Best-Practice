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

import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData;
import com.kunminx.architecture.ui.callback.UnPeekLiveData;

/**
 * TODO tip 1：callback-ViewModel 的职责仅限于在 "跨页面通信" 的场景下，承担 "唯一可信源"，
 * 所有跨页面的 "状态同步请求" 都交由该可信源在内部决策和处理，并统一分发给所有订阅者页面。
 * <p>
 * 如果这样说还不理解的话，详见《LiveData 鲜为人知的 身世背景 和 独特使命》中结合实际场合 对"唯一可信源"本质的解析。
 * https://xiaozhuanlan.com/topic/0168753249
 *
 * <p>
 * Create by KunMinX at 19/10/16
 */
public class SharedViewModel extends ViewModel {

    //TODO tip 2：此处演示通过 UnPeekLiveData 配合 SharedViewModel 来发送 生命周期安全的、
    // 确保消息同步一致性和可靠性的 "跨页面" 通知。

    //TODO tip 3：并且，在 "页面通信" 的场景下，使用全局 ViewModel，是因为它被封装在 base 页面中，
    // 避免页面之外的组件拿到，从而造成不可预期的推送。
    // 而且尽可能使用单例或 ViewModel 托管 liveData，这样能利用好 LiveData "读写分离" 的特性
    // 来实现 "唯一可信源" 单向数据流的决策和分发，从而避免只读数据被篡改 导致的其他页面拿到脏数据。

    // 如果这么说还不理解的话，
    // 详见 https://xiaozhuanlan.com/topic/0168753249 和 https://xiaozhuanlan.com/topic/6257931840

    private final UnPeekLiveData<Boolean> toCloseSlidePanelIfExpanded = new UnPeekLiveData<>();

    private final UnPeekLiveData<Boolean> toCloseActivityIfAllowed = new UnPeekLiveData<>();

    private final UnPeekLiveData<Boolean> toOpenOrCloseDrawer = new UnPeekLiveData<>();

    //TODO tip 4：可以通过构造器的方式来配置 UnPeekLiveData

    // 具体存在有缘和使用方式可详见《LiveData 数据倒灌 背景缘由全貌 独家解析》
    // https://xiaozhuanlan.com/topic/6719328450

    private final UnPeekLiveData<Boolean> toAddSlideListener =
            new UnPeekLiveData.Builder<Boolean>().setAllowNullValue(false).create();

    public ProtectedUnPeekLiveData<Boolean> isToAddSlideListener() {
        return toAddSlideListener;
    }

    public ProtectedUnPeekLiveData<Boolean> isToCloseSlidePanelIfExpanded() {
        return toCloseSlidePanelIfExpanded;
    }

    public ProtectedUnPeekLiveData<Boolean> isToCloseActivityIfAllowed() {
        return toCloseActivityIfAllowed;
    }

    public ProtectedUnPeekLiveData<Boolean> isToOpenOrCloseDrawer() {
        return toOpenOrCloseDrawer;
    }

    public void requestToCloseActivityIfAllowed(boolean allow) {
        toCloseActivityIfAllowed.setValue(allow);
    }

    public void requestToOpenOrCloseDrawer(boolean open) {
        toOpenOrCloseDrawer.setValue(open);
    }

    public void requestToCloseSlidePanelIfExpanded(boolean close) {
        toCloseSlidePanelIfExpanded.setValue(close);
    }

    public void requestToAddSlideListener(boolean add) {
        toAddSlideListener.setValue(add);
    }
}
