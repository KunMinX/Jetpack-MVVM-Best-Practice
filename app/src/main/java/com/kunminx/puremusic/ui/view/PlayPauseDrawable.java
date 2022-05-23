package com.kunminx.puremusic.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Property;

import androidx.annotation.ColorInt;


public class PlayPauseDrawable extends Drawable {

    private static final Property<PlayPauseDrawable, Float> PROGRESS = new Property<PlayPauseDrawable, Float>(Float.class, "progress") {
        @Override
        public Float get(PlayPauseDrawable d) {
            return d.getProgress();
        }

        @Override
        public void set(PlayPauseDrawable d, Float value) {
            d.setProgress(value);
        }
    };

    private final Path mLeftPauseBar = new Path();
    private final Path mRightPauseBar = new Path();
    private final Paint mPaint = new Paint();
    private final RectF mBounds = new RectF();
    private float mPauseBarWidth;
    private float mPauseBarHeight;
    private float mPauseBarDistance;

    private float mWidth;
    private float mHeight;

    private float mProgress;
    private boolean mIsPlay;

    public PlayPauseDrawable() {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
    }

    public PlayPauseDrawable(@ColorInt int color) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
    }

    private static float interpolate(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public void setIsPlay(boolean isPlay) {
        this.mIsPlay = isPlay;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds.set(bounds);
        mWidth = mBounds.width();
        mHeight = mBounds.height();

        mPauseBarWidth = mWidth / 8;
        mPauseBarHeight = mHeight * 0.40f;
        mPauseBarDistance = mPauseBarWidth;

    }

    @Override
    public void draw(Canvas canvas) {
        mLeftPauseBar.rewind();
        mRightPauseBar.rewind();

        final float barDist = interpolate(mPauseBarDistance, 0, mProgress);
        final float barWidth = interpolate(mPauseBarWidth, mPauseBarHeight / 2f, mProgress);
        final float firstBarTopLeft = interpolate(0, barWidth, mProgress);
        final float secondBarTopRight = interpolate(2 * barWidth + barDist, barWidth + barDist, mProgress);

        mLeftPauseBar.moveTo(0, 0);
        mLeftPauseBar.lineTo(firstBarTopLeft, -mPauseBarHeight);
        mLeftPauseBar.lineTo(barWidth, -mPauseBarHeight);
        mLeftPauseBar.lineTo(barWidth, 0);
        mLeftPauseBar.close();

        mRightPauseBar.moveTo(barWidth + barDist, 0);
        mRightPauseBar.lineTo(barWidth + barDist, -mPauseBarHeight);
        mRightPauseBar.lineTo(secondBarTopRight, -mPauseBarHeight);
        mRightPauseBar.lineTo(2 * barWidth + barDist, 0);
        mRightPauseBar.close();

        canvas.save();

        canvas.translate(interpolate(0, mPauseBarHeight / 8f, mProgress), 0);

        final float rotationProgress = mIsPlay ? 1 - mProgress : mProgress;
        final float startingRotation = mIsPlay ? 90 : 0;
        canvas.rotate(interpolate(startingRotation, startingRotation + 90, rotationProgress), mWidth / 2f, mHeight / 2f);

        canvas.translate(mWidth / 2f - ((2 * barWidth + barDist) / 2f), mHeight / 2f + (mPauseBarHeight / 2f));

        canvas.drawPath(mLeftPauseBar, mPaint);
        canvas.drawPath(mRightPauseBar, mPaint);

        canvas.restore();
    }

    public Animator getPausePlayAnimator() {
        final Animator anim = ObjectAnimator.ofFloat(this, PROGRESS, mIsPlay ? 1 : 0, mIsPlay ? 0 : 1);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsPlay = !mIsPlay;
            }
        });
        return anim;
    }

    public boolean isPlay() {
        return mIsPlay;
    }

    private float getProgress() {
        return mProgress;
    }

    private void setProgress(float progress) {
        mProgress = progress;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    public void setDrawableColor(@ColorInt int color) {
        mPaint.setColor(color);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
