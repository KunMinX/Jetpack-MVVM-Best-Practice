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

package com.kunminx.puremusic.ui.bind;

import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.kunminx.architecture.utils.ClickUtils;

/**
 * Create by KunMinX at 19/9/18
 */
public class CommonBindingAdapter {

    @BindingAdapter(value = {"imageUrl", "placeHolder"}, requireAll = false)
    public static void imageUrl(ImageView view, String url, Drawable placeHolder) {
        Glide.with(view.getContext()).load(url).placeholder(placeHolder).into(view);
    }

    @BindingAdapter(value = {"visible"}, requireAll = false)
    public static void visible(View view, boolean visible) {
        if (visible && view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else if (!visible && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter(value = {"invisible"}, requireAll = false)
    public static void invisible(View view, boolean visible) {
        if (visible && view.getVisibility() == View.INVISIBLE) {
            view.setVisibility(View.VISIBLE);
        } else if (!visible && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @BindingAdapter(value = {"size"}, requireAll = false)
    public static void size(View view, Pair<Integer, Integer> size) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.width = size.first;
        params.height = size.second;
        view.setLayoutParams(params);
    }

    @BindingAdapter(value = {"transX"}, requireAll = false)
    public static void translationX(View view, float translationX) {
        view.setTranslationX(translationX);
    }

    @BindingAdapter(value = {"transY"}, requireAll = false)
    public static void translationY(View view, float translationY) {
        view.setTranslationY(translationY);
    }

    @BindingAdapter(value = {"x"}, requireAll = false)
    public static void x(View view, float x) {
        view.setX(x);
    }

    @BindingAdapter(value = {"y"}, requireAll = false)
    public static void y(View view, float y) {
        view.setY(y);
    }

    @BindingAdapter(value = {"alpha"}, requireAll = false)
    public static void alpha(View view, float alpha) {
        view.setAlpha(alpha);
    }

    @BindingAdapter(value = {"textColor"}, requireAll = false)
    public static void setTextColor(TextView textView, int textColorRes) {
        textView.setTextColor(textView.getContext().getColor(textColorRes));
    }

    @BindingAdapter(value = {"selected"}, requireAll = false)
    public static void selected(View view, boolean select) {
        view.setSelected(select);
    }


    @BindingAdapter(value = {"onClickWithDebouncing"}, requireAll = false)
    public static void onClickWithDebouncing(View view, View.OnClickListener clickListener) {
        ClickUtils.applySingleDebouncing(view, clickListener);
    }
}
