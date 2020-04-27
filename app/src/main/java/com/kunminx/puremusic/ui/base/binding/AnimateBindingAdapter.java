/*
 * Copyright 2018-2020 KunMinX
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

package com.kunminx.puremusic.ui.base.binding;

import android.view.View;
import android.view.animation.Animation;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.databinding.BindingAdapter;

/**
 * Create by KunMinX at 20/4/19
 */
public class AnimateBindingAdapter {

    @BindingAdapter(value = {"setX"}, requireAll = false)
    public static void setX(View view, int setX) {
        view.setX(setX);
    }

    @BindingAdapter(value = {"setY"}, requireAll = false)
    public static void setY(View view, int setY) {
        view.setY(setY);
    }

    @BindingAdapter(value = {"startAnimation"}, requireAll = false)
    public static void startAnimation(View view, Animation animation) {
        if (view != null && animation != null) {
            view.startAnimation(animation);
        }
    }

    @BindingAdapter(value = {"transitionToStart"}, requireAll = false)
    public static void transitionToStart(MotionLayout motionLayout, boolean transitionToStart) {
        motionLayout.transitionToStart();
    }

    @BindingAdapter(value = {"transitionToEnd"}, requireAll = false)
    public static void transitionToEnd(MotionLayout motionLayout, boolean transitionToEnd) {
        motionLayout.transitionToEnd();
    }
}
