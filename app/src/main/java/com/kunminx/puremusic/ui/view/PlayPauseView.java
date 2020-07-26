

package com.kunminx.puremusic.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.kunminx.puremusic.R;


public class PlayPauseView extends FrameLayout {

    private static final Property<PlayPauseView, Integer> COLOR = new Property<PlayPauseView, Integer>(Integer.class, "color") {
        @Override
        public Integer get(PlayPauseView v) {
            return v.getCircleColor();
        }

        @Override
        public void set(PlayPauseView v, Integer value) {
            v.setCircleColor(value);
        }
    };

    private static final long PLAY_PAUSE_ANIMATION_DURATION = 200;
    public final boolean isDrawCircle;
    private final PlayPauseDrawable mDrawable;
    private final Paint mPaint = new Paint();
    public int circleAlpha;
    private int mDrawableColor;
    private AnimatorSet mAnimatorSet;
    private int mBackgroundColor;
    private int mWidth;
    private int mHeight;
    private boolean mIsPlay;

    public PlayPauseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlayPauseView);
        isDrawCircle = typedArray.getBoolean(R.styleable.PlayPauseView_isCircleDraw, true);
        circleAlpha = typedArray.getInt(R.styleable.PlayPauseView_circleAlpha, 255);
        mDrawableColor = typedArray.getInt(R.styleable.PlayPauseView_drawableColor, Color.WHITE);
        typedArray.recycle();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(circleAlpha);
        mPaint.setColor(mBackgroundColor);
        mDrawable = new PlayPauseDrawable(context, mDrawableColor);
        mDrawable.setCallback(this);

    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawable.setBounds(0, 0, w, h);
        mWidth = w;
        mHeight = h;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
            setClipToOutline(true);
        }
    }

    public void setCircleAlpha(int alpah) {
        circleAlpha = alpah;
        invalidate();
    }

    private int getCircleColor() {
        return mBackgroundColor;
    }

    public void setCircleColor(@ColorInt int color) {
        mBackgroundColor = color;
        invalidate();
    }

    public int getDrawableColor() {
        return mDrawableColor;
    }

    public void setDrawableColor(@ColorInt int color) {
        mDrawableColor = color;
        mDrawable.setDrawableColor(color);
        invalidate();
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mDrawable || super.verifyDrawable(who);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mBackgroundColor);
        final float radius = Math.min(mWidth, mHeight) / 2f;
        if (isDrawCircle) {
            mPaint.setColor(mBackgroundColor);
            mPaint.setAlpha(circleAlpha);
            canvas.drawCircle(mWidth / 2f, mHeight / 2f, radius, mPaint);
        }
        mDrawable.draw(canvas);
    }

    public boolean isPlay() {
        return mIsPlay;
    }

    public void play() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
        mAnimatorSet = new AnimatorSet();
        mIsPlay = true;
        mDrawable.setIsPlay(mIsPlay);
        final Animator pausePlayAnim = mDrawable.getPausePlayAnimator();
        mAnimatorSet.setInterpolator(new DecelerateInterpolator());
        mAnimatorSet.setDuration(PLAY_PAUSE_ANIMATION_DURATION);
        pausePlayAnim.start();
    }

    public void pause() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }

        mAnimatorSet = new AnimatorSet();
        mIsPlay = false;
        mDrawable.setIsPlay(mIsPlay);
        final Animator pausePlayAnim = mDrawable.getPausePlayAnimator();
        mAnimatorSet.setInterpolator(new DecelerateInterpolator());
        mAnimatorSet.setDuration(PLAY_PAUSE_ANIMATION_DURATION);
        pausePlayAnim.start();
    }

}
