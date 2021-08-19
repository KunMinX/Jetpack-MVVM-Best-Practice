package com.kunminx.architecture.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/12
 *     desc  : utils about image
 * </pre>
 */
public final class ImageUtils {

    /**
     * Return bitmap.
     *
     * @param filePath The path of file.
     * @return bitmap
     */
    public static Bitmap getBitmap(final String filePath) {
        if (isSpace(filePath)) {
            return null;
        }
        return BitmapFactory.decodeFile(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
