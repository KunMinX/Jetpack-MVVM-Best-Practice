package com.kunminx.architecture.utils;

import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2019/06/12
 *     desc  : utils about click
 * </pre>
 */
public class ClickUtils {

    private static final int DEBOUNCING_TAG = -7;
    private static final long DEBOUNCING_DEFAULT_VALUE = 700;

    private ClickUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Apply single debouncing for the view's click.
     *
     * @param view     The view.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View view, final View.OnClickListener listener) {
        applySingleDebouncing(new View[]{view}, listener);
    }

    /**
     * Apply single debouncing for the views' click.
     *
     * @param views    The views.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View[] views, final View.OnClickListener listener) {
        applySingleDebouncing(views, DEBOUNCING_DEFAULT_VALUE, listener);
    }

    /**
     * Apply single debouncing for the views' click.
     *
     * @param views    The views.
     * @param duration The duration of debouncing.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View[] views,
                                             @IntRange(from = 0) long duration,
                                             final View.OnClickListener listener) {
        applyDebouncing(views, false, duration, listener);
    }

    private static void applyDebouncing(final View[] views,
                                        final boolean isGlobal,
                                        @IntRange(from = 0) long duration,
                                        final View.OnClickListener listener) {
        if (views == null || views.length == 0 || listener == null) {
            return;
        }
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setOnClickListener(new OnDebouncingClickListener(isGlobal, duration) {
                @Override
                public void onDebouncingClick(View v) {
                    listener.onClick(v);
                }
            });
        }
    }

    public static abstract class OnDebouncingClickListener implements View.OnClickListener {

        private static boolean mEnabled = true;

        private static final Runnable ENABLE_AGAIN = () -> mEnabled = true;
        private final long mDuration;
        private final boolean mIsGlobal;

        public OnDebouncingClickListener() {
            this(true, DEBOUNCING_DEFAULT_VALUE);
        }

        public OnDebouncingClickListener(final boolean isGlobal) {
            this(isGlobal, DEBOUNCING_DEFAULT_VALUE);
        }

        public OnDebouncingClickListener(final long duration) {
            this(true, duration);
        }

        public OnDebouncingClickListener(final boolean isGlobal, final long duration) {
            mIsGlobal = isGlobal;
            mDuration = duration;
        }

        private static boolean isValid(@NonNull final View view, final long duration) {
            long curTime = System.currentTimeMillis();
            Object tag = view.getTag(DEBOUNCING_TAG);
            if (!(tag instanceof Long)) {
                view.setTag(DEBOUNCING_TAG, curTime);
                return true;
            }
            long preTime = (Long) tag;
            if (curTime - preTime <= duration) {
                return false;
            }
            view.setTag(DEBOUNCING_TAG, curTime);
            return true;
        }

        public abstract void onDebouncingClick(View v);

        @Override
        public final void onClick(View v) {
            if (mIsGlobal) {
                if (mEnabled) {
                    mEnabled = false;
                    v.postDelayed(ENABLE_AGAIN, mDuration);
                    onDebouncingClick(v);
                }
            } else {
                if (isValid(v, mDuration)) {
                    onDebouncingClick(v);
                }
            }
        }
    }


}
