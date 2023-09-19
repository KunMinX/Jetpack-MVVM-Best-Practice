package com.kunminx.architecture.utils;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.util.Objects;
/**
 * Create by KunMinX at 2023/6/5
 */
public class Res {
    public static Drawable getDrawable(int resId) {
        return Objects.requireNonNull(ContextCompat.getDrawable(Utils.getApp(), resId));
    }
}
