package com.kunminx.architecture.utils;

import android.widget.Toast;

/**
 * Create by KunMinX at 2021/8/19
 */
public class ToastUtils {

    public static void showLongToast(String text) {
        Toast.makeText(Utils.getApp().getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String text) {
        Toast.makeText(Utils.getApp().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
