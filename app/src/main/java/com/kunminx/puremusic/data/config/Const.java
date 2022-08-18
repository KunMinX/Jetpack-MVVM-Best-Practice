package com.kunminx.puremusic.data.config;

import android.os.Environment;

import com.kunminx.architecture.utils.Utils;
/**
 * Create by KunMinX at 2022/8/18
 */
public class Const {
    public static final String COVER_PATH = Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
}
