

package com.kunminx.puremusic.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
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

    public PlayPauseDrawable(Context context) {
        final Resources res = context.getResources();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
    }

    public PlayPauseDrawable(Context context, @ColorInt int color) {
        final Resources res = context.getResources();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
    }

    /**
     * Linear interpolate between a and b with parameter t.
     */
    private static float lerp(float a, float b, float t) {
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

        // The current distance between the two pause bars.
        final float barDist = lerp(mPauseBarDistance, 0, mProgress);
        // The current width of each pause bar.
        final float barWidth = lerp(mPauseBarWidth, mPauseBarHeight / 2f, mProgress);
        // The current position of the left pause bar's top left coordinate.
        final float firstBarTopLeft = lerp(0, barWidth, mProgress);
        // The current position of the right pause bar's top right coordinate.
        final float secondBarTopRight = lerp(2 * barWidth + barDist, barWidth + barDist, mProgress);

        // Draw the left pause bar. The left pause bar transforms into the
        // top half of the play button triangle by animating the position of the
        // rectangle's top left coordinate and expanding its bottom width.
        mLeftPauseBar.moveTo(0, 0);
        mLeftPauseBar.lineTo(firstBarTopLeft, -mPauseBarHeight);
        mLeftPauseBar.lineTo(barWidth, -mPauseBarHeight);
        mLeftPauseBar.lineTo(barWidth, 0);
        mLeftPauseBar.close();

        // Draw the right pause bar. The right pause bar transforms into the
        // bottom half of the play button triangle by animating the position of
        // the
        // rectangle's top right coordinate and expanding its bottom width.
        mRightPauseBar.moveTo(barWidth + barDist, 0);
        mRightPauseBar.lineTo(barWidth + barDist, -mPauseBarHeight);
        mRightPauseBar.lineTo(secondBarTopRight, -mPauseBarHeight);
        mRightPauseBar.lineTo(2 * barWidth + barDist, 0);
        mRightPauseBar.close();

        canvas.save();

        // Translate the play button a tiny bit to the right so it looks more
        // centered.
        canvas.translate(lerp(0, mPauseBarHeight / 8f, mProgress), 0);

        // (1) Pause --> Play: rotate 0 to 90 degrees clockwise.
        // (2) Play --> Pause: rotate 90 to 180 degrees clockwise.
        final float rotationProgress = mIsPlay ? 1 - mProgress : mProgress;
        final float startingRotation = mIsPlay ? 90 : 0;
        canvas.rotate(lerp(startingRotation, startingRotation + 90, rotationProgress), mWidth / 2f, mHeight / 2f);

        // Position the pause/play button in the center of the drawable's
        // bounds.
        canvas.translate(mWidth / 2f - ((2 * barWidth + barDist) / 2f), mHeight / 2f + (mPauseBarHeight / 2f));

        // Draw the two bars that form the animated pause/play button.
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