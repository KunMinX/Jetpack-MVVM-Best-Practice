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

package com.kunminx.puremusic.domain.message;

import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.domain.message.MutableResult;
import com.kunminx.architecture.domain.message.Result;

/**
 * TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 Result-ViewModel，
 * Result-ViewModel 职责仅限于 "消息分发" 场景承担 "唯一可信源"。
 * <p>
 * 常见消息分发场景包括：数据请求，页面间通信等，
 * 数据请求 Requester 负责，页面通信 Messenger 负责，
 * <p>
 * 所有事件都可交由 "唯一可信源" 在内部决策和处理，并统一分发结果给所有订阅者页面。
 * <p>
 * 如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
 * https://xiaozhuanlan.com/topic/6017825943
 *
 * <p>
 * Create by KunMinX at 19/10/16
 *
 *
 *
 *
 *
 * TODO:Note 2022.07.04
 * 可于领域层通过 MVI-Dispatcher 实现成熟形态 "唯一可信源"，
 * 使支持 LiveData 连续发送多种类事件 + 彻底消除 mutable 样板代码 + 彻底杜绝团队新手 LiveData.setValue 误用滥用，
 * 鉴于本项目场景难发挥 MVI-Dispatcher 潜能，故目前仅以改造 SharedViewModel 为例，
 * 通过对比 SharedViewModel 和 PageMessenger 易得，后者可简洁优雅实现可靠一致消息分发，
 *
 * 具体可参见专为 MVI-Dispatcher 唯一可信源编写之 MVI 绝佳使用案例：
 *
 * https://github.com/KunMinX/MVI-Dispatcher
 */
@Deprecated
public class SharedViewModel extends ViewModel {

    //TODO tip 2：此处演示 UnPeekLiveData 配合 SharedViewModel 实现 "生命周期安全、可靠一致" 消息分发。

    //TODO tip 3：为便于理解，原 UnPeekLiveData 已改名 MutableResult；
    // ProtectedUnPeekLiveData 改名 Result；
    // SharedViewModel 改名 PageMessenger。

    private final MutableResult<Boolean> toCloseSlidePanelIfExpanded = new MutableResult<>();

    private final MutableResult<Boolean> toCloseActivityIfAllowed = new MutableResult<>();

    private final MutableResult<Boolean> toOpenOrCloseDrawer = new MutableResult<>();

    //TODO tip 4：可通过构造器方式配置 MutableResult

    private final MutableResult<Boolean> toAddSlideListener =
        new MutableResult.Builder<Boolean>().setAllowNullValue(false).create();

    public Result<Boolean> isToAddSlideListener() {
        return toAddSlideListener;
    }

    public Result<Boolean> isToCloseSlidePanelIfExpanded() {
        return toCloseSlidePanelIfExpanded;
    }

    public Result<Boolean> isToCloseActivityIfAllowed() {
        return toCloseActivityIfAllowed;
    }

    public Result<Boolean> isToOpenOrCloseDrawer() {
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
