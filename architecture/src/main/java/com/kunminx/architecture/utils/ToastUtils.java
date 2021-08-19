package com.kunminx.architecture.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Create by KunMinX at 2021/8/19
 */
public class ToastUtils {

    public static void showLongToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
