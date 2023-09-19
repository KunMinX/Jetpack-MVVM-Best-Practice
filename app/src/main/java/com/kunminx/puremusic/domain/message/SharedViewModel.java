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
 * TODO tip：本类专用于跨页面通信，
 * 本类已被 PageMessenger 类代替，具体可参见 PageMessenger 类说明
 * <p>
 * Create by KunMinX at 19/10/16
 */
@Deprecated
public class SharedViewModel extends ViewModel {

    //TODO tip 2：此处演示 UnPeekLiveData 配合 SharedViewModel 实现 "生命周期安全、可靠一致" 消息分发。

    //TODO tip 3：为便于理解，原 UnPeekLiveData 已改名为 MutableResult；
    // ProtectedUnPeekLiveData 改名 Result；

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
