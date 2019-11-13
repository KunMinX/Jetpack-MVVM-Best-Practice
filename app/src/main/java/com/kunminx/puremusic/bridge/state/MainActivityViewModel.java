/*
 * Copyright 2018-2019 KunMinX
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

package com.kunminx.puremusic.bridge.state;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

/**
 * TODO tip：每个页面都要单独准备一个 statusViewModel，
 * 来托管 DataBinding 绑定的临时状态，以及视图控制器重建时状态的恢复。
 *
 * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350
 * <p>
 * Create by KunMinX at 19/10/29
 */
public class MainActivityViewModel extends ViewModel {

    public final ObservableBoolean openDrawer = new ObservableBoolean();

    public final ObservableBoolean allowDrawerOpen = new ObservableBoolean();

    {
        allowDrawerOpen.set(true);
    }
}
