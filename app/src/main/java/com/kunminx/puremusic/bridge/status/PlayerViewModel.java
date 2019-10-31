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

package com.kunminx.puremusic.bridge.status;

import android.graphics.drawable.Drawable;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.utils.Utils;
import com.kunminx.puremusic.R;

/**
 * TODO tip：每个页面都要单独准备一个 statusViewModel，
 * 来托管 DataBinding 绑定的临时状态，
 * statusViewModel 生命周期应设置为与 视图控制器 同生共死。
 * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350
 * <p>
 * Create by KunMinX at 19/10/29
 */
public class PlayerViewModel extends ViewModel {

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableField<String> artist = new ObservableField<>();

    public final ObservableField<String> coverImg = new ObservableField<>();

    public final ObservableField<Drawable> placeHolder = new ObservableField<>();

    public final ObservableInt maxSeekDuration = new ObservableInt();

    public final ObservableInt currentSeekPosition = new ObservableInt();

    public final ObservableBoolean isPlaying = new ObservableBoolean();


    {
        title.set(Utils.getApp().getString(R.string.app_name));
        artist.set(Utils.getApp().getString(R.string.app_name));
        placeHolder.set(Utils.getApp().getResources().getDrawable(R.drawable.bg_album_default));
    }
}
