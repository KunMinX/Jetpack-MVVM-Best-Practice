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

package com.kunminx.puremusic.ui.state;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.ui.page.State;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.player.PlayingInfoManager;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.player.PlayerManager;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * TODO tip：每个页面都要单独准备一个 state-ViewModel，
 * 来托管 DataBinding 绑定的临时状态，以及视图控制器重建时状态的恢复。
 * <p>
 * 此外，state-ViewModel 的职责仅限于 状态托管，不建议在此处理 UI 逻辑，
 * UI 逻辑只适合在 Activity/Fragment 等视图控制器中完成，是 “数据驱动” 的一部分，
 * 将来升级到 Jetpack Compose 更是如此。
 * <p>
 * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350
 * <p>
 * Create by KunMinX at 19/10/29
 */
public class PlayerViewModel extends ViewModel {

    public final State<String> title = new State<>(Utils.getApp().getString(R.string.app_name));

    public final State<String> artist = new State<>(Utils.getApp().getString(R.string.app_name));

    public final State<String> coverImg = new State<>();

    public final State<Drawable> placeHolder = new State<>(ContextCompat.getDrawable(Utils.getApp(), R.drawable.bg_album_default));

    public final State<Integer> maxSeekDuration = new State<>();

    public final State<Integer> currentSeekPosition = new State<>();

    public final State<Boolean> isPlaying = new State<>(null, false);

    public final State<MaterialDrawableBuilder.IconValue> playModeIcon = new State<>();

    {
        if (PlayerManager.getInstance().getRepeatMode() == PlayingInfoManager.RepeatMode.LIST_CYCLE) {
            playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT);
        } else if (PlayerManager.getInstance().getRepeatMode() == PlayingInfoManager.RepeatMode.SINGLE_CYCLE) {
            playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
        } else {
            playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE);
        }
    }
}
