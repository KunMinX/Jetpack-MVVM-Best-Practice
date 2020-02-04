/*
 * Copyright 2018-2019 KunMinX
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

package com.kunminx.puremusic.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LyricView extends View {

    // 按钮颜色
    private final int mBtnColor = Color.parseColor("#EFEFEF");
    // 指示器颜色
    private final int mIndicatorColor = Color.parseColor("#EFEFEF");
    // 当前拖动位置的颜色
    private final int mCurrentShowColor = Color.parseColor("#AAAAAA");
    // 最低滑行启动速度
    private final int mMinStartUpSpeed = 1600;
    private final int mDefaultMargin = 12;
    private final String mDefaultTime = "00:00";
    private final int MSG_PLAYER_SLIDE = 0x158;
    private final int MSG_PLAYER_HIDE = 0x157;
    /**
     * 计算阻尼效果的大小
     */
    private final int mMaxDampingDistance = 360;
    // 提示语颜色
    private int mHintColor = Color.parseColor("#666666");
    // 默认字体颜色
    private int mDefaultColor = Color.parseColor("#000000");
    // 当前播放位置的颜色
    private int mHighLightColor = Color.parseColor("#4FC5C7");
    // 行数
    private int mLineCount;
    // 行高
    private float mLineHeight;
    // 纵轴偏移量
    private float mScrollY = 0;
    // 纵轴上的滑动速度
    private float mVelocity = 0;
    // 行间距（包含在行高中）
    private float mLineSpace = 0;
    // 渐变过渡的距离
    private float mShaderWidth = 0;
    // 当前拖动位置对应的行数
    private int mCurrentShowLine = 0;
    // 当前播放位置对应的行数
    private int mCurrentPlayLine = 0;
    // 判断当前用户是否触摸
    private boolean mUserTouch = false;
    // 判断当前滑动指示器是否显示
    private boolean mIndicatorShow = false;
    final Handler postman = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PLAYER_HIDE:
                    postman.sendEmptyMessageDelayed(MSG_PLAYER_SLIDE, 1200);
                    mIndicatorShow = false;
                    invalidateView();
                    break;
                case MSG_PLAYER_SLIDE:
                    smoothScrollTo(measureCurrentScrollY(mCurrentPlayLine));
                    invalidateView();
                    break;
                default:
                    break;
            }
        }
    };
    // 判断用户触摸时是否发生move事件
    private boolean mIsMoved = false;
    // 判断当前用户是否点击指示器
    private boolean mPlayerClick = false;
    // Btn 按钮的宽度
    private int mBtnWidth = 0;
    private Rect mBtnBound, mTimerBound;
    private VelocityTracker mVelocityTracker;
    private LyricInfo mLyricInfo;
    private String mDefaultHint = "暂无歌词";
    private Paint mTextPaint, mBtnPaint, mIndicatorPaint;
    private OnPlayerClickListener mClickListener;
    private ValueAnimator mFlingAnimator;
    private boolean mPlayable = false;
    private boolean mSliding = false;
    // 最大纵向滑动速度
    private int maximumFlingVelocity;
    private boolean mTouchable = true;
    // 记录手指按下时的坐标和当前的滑动偏移量
    private float mDownX, mDownY, mLastScrollY;

    public LyricView(Context context) {
        super(context);
        initMyView(context);
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView(context);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMyView(context);
    }

    /**
     * 将解析得到的表示时间的字符转化为Long型
     */
    private static long measureStartTimeMillis(String timeString) {
        //因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
        //将字符串 XX:XX.XX 转换为 XX:XX:XX
        timeString = timeString.replace('.', ':');
        //将字符串 XX:XX:XX 拆分
        String[] times = timeString.split(":");
        // mm:ss:SS
        return Integer.valueOf(times[0]) * 60 * 1000 +//分
                Integer.valueOf(times[1]) * 1000 +//秒
                Integer.valueOf(times[2]);//毫秒
    }

    private void initMyView(Context context) {
        maximumFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        initAllPaints();
        initAllBounds();
    }

    /**
     * 初始化需要的尺寸
     */
    private void initAllBounds() {
        setTextSize(15);
        setLineSpace(12);
        mBtnWidth = (int) (getRawSize(TypedValue.COMPLEX_UNIT_SP, 24));
        mTimerBound = new Rect();
        mIndicatorPaint.getTextBounds(mDefaultTime, 0, mDefaultTime.length(), mTimerBound);

        measureLineHeight();
    }

    /**
     * 初始化画笔
     */
    private void initAllPaints() {
        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setDither(true);
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_SP, 12));
        mIndicatorPaint.setTextAlign(Paint.Align.CENTER);

        mBtnPaint = new Paint();
        mBtnPaint.setDither(true);
        mBtnPaint.setAntiAlias(true);
        mBtnPaint.setColor(mBtnColor);
        mBtnPaint.setStrokeWidth(3.0f);
        mBtnPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mShaderWidth = getMeasuredHeight() * 0.3f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLyricInfo != null && mLyricInfo.song_lines != null && mLyricInfo.song_lines.size() > 0) {
            for (int i = 0, size = mLineCount; i < size; i++) {
                float x = getMeasuredWidth() * 0.5f;
                float y = getMeasuredHeight() * 0.5f + (i + 0.5f) * mLineHeight - 6 - mLineSpace * 0.5f - mScrollY;
                if (y + mLineHeight * 0.5f < 0) {
                    continue;
                }
                if (y - mLineHeight * 0.5f > getMeasuredHeight()) {
                    break;
                }
                if (i == mCurrentPlayLine - 1) {
                    mTextPaint.setColor(mHighLightColor);
                } else {
                    if (mIndicatorShow && i == mCurrentShowLine - 1) {
                        mTextPaint.setColor(mCurrentShowColor);
                    } else {
                        mTextPaint.setColor(mDefaultColor);
                    }
                }
                if (y > getMeasuredHeight() - mShaderWidth || y < mShaderWidth) {
                    if (y < mShaderWidth) {
                        mTextPaint.setAlpha(26 + (int) (23000.0f * y / mShaderWidth * 0.01f));
                    } else {
                        mTextPaint.setAlpha(26 + (int) (23000.0f * (getMeasuredHeight() - y) / mShaderWidth * 0.01f));
                    }
                } else {
                    mTextPaint.setAlpha(255);
                }
                canvas.drawText(mLyricInfo.song_lines.get(i).content, x, y, mTextPaint);
            }
        } else {
            mTextPaint.setColor(mHintColor);
            canvas.drawText(mDefaultHint, getMeasuredWidth() * 0.5f, (getMeasuredHeight() + mLineHeight - 6) * 0.5f, mTextPaint);
        }
        // 滑动提示部分内容绘制
        if (mIndicatorShow && scrollable()) {
            if (mPlayable) {
                drawPlayer(canvas);
                drawIndicator(canvas);
            }
        }
    }

    /**
     * 绘制左侧的播放按钮
     *
     * @param canvas .
     */
    private void drawPlayer(Canvas canvas) {
        mBtnBound = new Rect(mDefaultMargin, (int) (getMeasuredHeight() * 0.5f - mBtnWidth * 0.5f), mBtnWidth + mDefaultMargin, (int) (getMeasuredHeight() * 0.5f + mBtnWidth * 0.5f));

        Path path = new Path();
        float radio = mBtnBound.width() * 0.3f;
        float value = (float) Math.sqrt(Math.pow(radio, 2) - Math.pow(radio * 0.5f, 2));
        path.moveTo(mBtnBound.centerX() - radio * 0.5f, mBtnBound.centerY() - value);
        path.lineTo(mBtnBound.centerX() - radio * 0.5f, mBtnBound.centerY() + value);
        path.lineTo(mBtnBound.centerX() + radio, mBtnBound.centerY());
        path.lineTo(mBtnBound.centerX() - radio * 0.5f, mBtnBound.centerY() - value);
        mBtnPaint.setAlpha(128);
        // 绘制播放按钮的三角形
        canvas.drawPath(path, mBtnPaint);
        // 绘制圆环
        canvas.drawCircle(mBtnBound.centerX(), mBtnBound.centerY(), mBtnBound.width() * 0.48f, mBtnPaint);
    }

    /**
     * 绘制指示器
     *
     * @param canvas .
     */
    private void drawIndicator(Canvas canvas) {
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setAlpha(128);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(measureCurrentTime(), getMeasuredWidth() - mTimerBound.width(), (getMeasuredHeight() + mTimerBound.height() - 6) * 0.5f, mIndicatorPaint);

        Path path = new Path();
        mIndicatorPaint.setStrokeWidth(2.0f);
        mIndicatorPaint.setStyle(Paint.Style.STROKE);
        mIndicatorPaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
        path.moveTo(mPlayable ? mBtnBound.right + 24 : 24, getMeasuredHeight() * 0.5f);
        path.lineTo(getMeasuredWidth() - mTimerBound.width() - mTimerBound.width() - 36, getMeasuredHeight() * 0.5f);
        canvas.drawPath(path, mIndicatorPaint);
    }

    /**
     * 计算行高度
     */
    private void measureLineHeight() {
        Rect lineBound = new Rect();
        mTextPaint.getTextBounds(mDefaultHint, 0, mDefaultHint.length(), lineBound);
        mLineHeight = lineBound.height() + mLineSpace;
    }

    /**
     * 获取当前滑动到的位置的当前时间
     */
    private String measureCurrentTime() {
        DecimalFormat format = new DecimalFormat("00");
        if (mLyricInfo != null && mLineCount > 0 && mCurrentShowLine - 1 < mLineCount && mCurrentShowLine > 0) {
            return format.format(mLyricInfo.song_lines.get(mCurrentShowLine - 1).start / 1000 / 60) + ":" + format.format(mLyricInfo.song_lines.get(mCurrentShowLine - 1).start / 1000 % 60);
        }
        if (mLyricInfo != null && mLineCount > 0 && (mCurrentShowLine - 1) >= mLineCount) {
            return format.format(mLyricInfo.song_lines.get(mLineCount - 1).start / 1000 / 60) + ":" + format.format(mLyricInfo.song_lines.get(mLineCount - 1).start / 1000 % 60);
        }
        if (mLyricInfo != null && mLineCount > 0 && mCurrentShowLine - 1 <= 0) {
            return format.format(mLyricInfo.song_lines.get(0).start / 1000 / 60) + ":" + format.format(mLyricInfo.song_lines.get(0).start / 1000 % 60);
        }
        return mDefaultTime;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!mTouchable) {
            return super.onTouchEvent(event);
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                actionCancel(event);
                break;
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;
            default:
                break;
        }
        invalidateView();
        if (mIsMoved || mPlayerClick) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * 手势取消执行事件
     *
     * @param event .
     */
    private void actionCancel(MotionEvent event) {
        releaseVelocityTracker();
    }

    /**
     * 手势按下执行事件
     *
     * @param event .
     */
    private void actionDown(MotionEvent event) {
        postman.removeMessages(MSG_PLAYER_SLIDE);
        postman.removeMessages(MSG_PLAYER_HIDE);
        mLastScrollY = mScrollY;
        mDownX = event.getX();
        mDownY = event.getY();
        if (mFlingAnimator != null) {
            mFlingAnimator.cancel();
            mFlingAnimator = null;
        }
        setUserTouch(true);
        mIsMoved = false;
        mPlayerClick = false;
    }

    /**
     * 手势移动执行事件
     *
     * @param event .
     */
    private void actionMove(MotionEvent event) {
        if (scrollable()) {
            final VelocityTracker tracker = mVelocityTracker;
            tracker.computeCurrentVelocity(1000, maximumFlingVelocity);
            // 102  -2  58  42
            float scrollY = mLastScrollY + mDownY - event.getY();
            // 52  -52  8  -8
            float value01 = scrollY - (mLineCount * mLineHeight * 0.5f);
            // 2  2  -42  -42
            float value02 = ((Math.abs(value01) - (mLineCount * mLineHeight * 0.5f)));
            mScrollY = value02 > 0 ? scrollY - (measureDampingDistance(value02) * value01 / Math.abs(value01)) : scrollY;   //   value01 / Math.abs(value01)  控制滑动方向
            mVelocity = tracker.getYVelocity();
            measureCurrentLine();
            if (Math.abs(mVelocity) > 1) {
                mIsMoved = true;
            }
        }
    }

    private float measureDampingDistance(float value02) {
        return value02 > mMaxDampingDistance ? (mMaxDampingDistance * 0.6f + (value02 - mMaxDampingDistance) * 0.72f) : value02 * 0.6f;
    }

    /**
     * 手势抬起执行事件
     */
    private void actionUp(MotionEvent event) {
        releaseVelocityTracker();
        // 2.4s 后发送一个指示器隐藏的请求
        postman.sendEmptyMessageDelayed(MSG_PLAYER_HIDE, 2400);
        if (scrollable()) {
            // 用户手指离开屏幕，取消触摸标记
            setUserTouch(false);
            if (overScrolled() && mScrollY < 0) {
                smoothScrollTo(0);
                return;
            }
            if (overScrolled() && mScrollY > mLineHeight * (mLineCount - 1)) {
                smoothScrollTo(mLineHeight * (mLineCount - 1));
                return;
            }
            if (Math.abs(mVelocity) > mMinStartUpSpeed) {
                doFlingAnimator(mVelocity);
                return;
            }
            if (mIndicatorShow && clickPlayer(event)) {
                if (mCurrentShowLine != mCurrentPlayLine) {
                    mIndicatorShow = false;
                    mPlayerClick = true;
                    if (mClickListener != null) {
                        mClickListener.onPlayerClicked(mLyricInfo.song_lines.get(mCurrentShowLine - 1).start, mLyricInfo.song_lines.get(mCurrentShowLine - 1).content);
                    }
                }
            }
        } else {
            performClick();
        }
    }

    /**
     * 刷新View
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            //  当前线程是主UI线程，直接刷新。
            invalidate();
        } else {
            //  当前线程是非UI线程，post刷新。
            postInvalidate();
        }
    }

    /**
     * 设置用户是否触摸的标记
     *
     * @param isUserTouch 标记用户是否触摸屏幕
     */
    private void setUserTouch(boolean isUserTouch) {
        if (mUserTouch == isUserTouch) {
            return;
        }
        mUserTouch = isUserTouch;
        if (isUserTouch) {
            mIndicatorShow = isUserTouch;
        }
    }

    /**
     * 释放速度追踪器
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 滑行动画
     *
     * @param velocity 滑动速度
     */
    private void doFlingAnimator(float velocity) {
        //注：Math.abs(velocity)  < =  16000
        // 计算就已当前的滑动速度理论上的滑行距离是多少
        float distance = (velocity / Math.abs(velocity) * Math.min((Math.abs(velocity) * 0.050f), 640));
        // 综合考虑边界问题后得出的实际滑行距离
        float to = Math.min(Math.max(0, (mScrollY - distance)), (mLineCount - 1) * mLineHeight);

        mFlingAnimator = ValueAnimator.ofFloat(mScrollY, to);
        mFlingAnimator.addUpdateListener(animation -> {
            mScrollY = (float) animation.getAnimatedValue();
            measureCurrentLine();
            invalidateView();
        });

        mFlingAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mVelocity = mMinStartUpSpeed - 1;
                mSliding = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSliding = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        });

        mFlingAnimator.setDuration(420);
        mFlingAnimator.setInterpolator(new DecelerateInterpolator());
        mFlingAnimator.start();
    }

    /**
     * To measure current showing line number based on the view's scroll Y
     */
    private void measureCurrentLine() {
        float baseScrollY = mScrollY + mLineHeight * 0.5f;
        mCurrentShowLine = (int) (baseScrollY / mLineHeight + 1);
    }

    /**
     * Input current showing line to measure the view's current scroll Y
     *
     * @param line 当前指定行号
     */
    private float measureCurrentScrollY(int line) {
        return (line - 1) * mLineHeight;
    }

    /**
     * 判断当前点击事件是否落在播放按钮触摸区域范围内
     *
     * @param event 触摸事件
     */
    private boolean clickPlayer(MotionEvent event) {
        if (mBtnBound != null && mDownX > (mBtnBound.left - mDefaultMargin) && mDownX < (mBtnBound.right + mDefaultMargin) && mDownY > (mBtnBound.top - mDefaultMargin) && mDownY < (mBtnBound.bottom + mDefaultMargin)) {
            float upX = event.getX();
            float upY = event.getY();
            return upX > (mBtnBound.left - mDefaultMargin) && upX < (mBtnBound.right + mDefaultMargin) && upY > (mBtnBound.top - mDefaultMargin) && upY < (mBtnBound.bottom + mDefaultMargin);
        }
        return false;
    }

    /**
     * 从当前位置滑动到指定位置上
     *
     * @param toY 指定纵坐标位置
     */
    private void smoothScrollTo(float toY) {
        final ValueAnimator animator = ValueAnimator.ofFloat(mScrollY, toY);
        animator.addUpdateListener(animation -> {
            if (mUserTouch) {
                animator.cancel();
                return;
            }
            mScrollY = (float) animation.getAnimatedValue();
            invalidateView();
        });

        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mSliding = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSliding = false;
                measureCurrentLine();
                invalidateView();
            }
        });
        animator.setDuration(640);
        animator.setInterpolator(new OvershootInterpolator(0.5f));

        animator.start();
    }

    /**
     * 判断是否可以进行滑动
     */
    private boolean scrollable() {
        return mLyricInfo != null && mLyricInfo.song_lines != null && mLyricInfo.song_lines.size() > 0;
    }

    /**
     * 判断当前View是否已经滑动到歌词的内容区域之外
     */
    private boolean overScrolled() {
        return scrollable() && (mScrollY > mLineHeight * (mLineCount - 1) || mScrollY < 0);
    }

    /**
     * 根据当前给定的时间戳滑动到指定位置
     *
     * @param time 时间戳
     */
    private void scrollToCurrentTimeMillis(long time) {
        int position = 0;
        if (scrollable()) {
            for (int i = 0, size = mLineCount; i < size; i++) {
                LineInfo lineInfo = mLyricInfo.song_lines.get(i);
                if (lineInfo != null && lineInfo.start > time) {
                    position = i;
                    break;
                }
                if (i == mLineCount - 1) {
                    position = mLineCount;
                }
            }
        }
        if (mCurrentPlayLine != position && !mUserTouch && !mSliding && !mIndicatorShow) {
            mCurrentPlayLine = position;
            smoothScrollTo(measureCurrentScrollY(position));
        } else {
            if (!mSliding && !mIndicatorShow) {
                mCurrentPlayLine = mCurrentShowLine = position;
            }
        }
    }

    /**
     * 初始化歌词信息
     *
     * @param inputStream 歌词文件的流信息
     */
    private void setupLyricResource(InputStream inputStream, String charsetName) {
        if (inputStream != null) {
            try {
                LyricInfo lyricInfo = new LyricInfo();
                lyricInfo.song_lines = new ArrayList<>();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    analyzeLyric(lyricInfo, line);
                }
                //歌词排序
                Collections.sort(lyricInfo.song_lines, new sort());
                reader.close();
                inputStream.close();
                inputStreamReader.close();

                mLyricInfo = lyricInfo;
                mLineCount = mLyricInfo.song_lines.size();
                invalidateView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mDefaultHint = "暂无歌词";
            invalidateView();
        }
    }

    /**
     * 逐行解析歌词内容
     */
    private void analyzeLyric(LyricInfo lyricInfo, @Nullable String line) {
        int index = line.indexOf("]");
        if (line != null && line.startsWith("[offset:")) {
            // 时间偏移量
            String string = line.substring(8, index).trim();
            lyricInfo.song_offset = Long.parseLong(string);
            return;
        }
        if (line != null && line.startsWith("[ti:")) {
            // title 标题
            lyricInfo.song_title = line.substring(4, index).trim();
            return;
        }
        if (line != null && line.startsWith("[ar:")) {
            // artist 作者
            lyricInfo.song_artist = line.substring(4, index).trim();
            return;
        }
        if (line != null && line.startsWith("[al:")) {
            // album 所属专辑
            lyricInfo.song_album = line.substring(4, index).trim();
            return;
        }
        if (line != null && line.startsWith("[by:")) {
            return;
        }
        if (line != null && index == 9 && line.trim().length() > 10) {
            // 歌词内容,需要考虑一行歌词有多个时间戳的情况
            int lastIndexOfRightBracket = line.lastIndexOf("]");
            String content = line.substring(lastIndexOfRightBracket + 1);

            String times = line.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-");
            String[] arrTimes = times.split("-");
            for (String temp : arrTimes) {
                if (temp.trim().length() == 0) {
                    continue;
                }
                /* [02:34.14][01:07.00]当你我不小心又想起她
                 *
                 上面的歌词的就可以拆分为下面两句歌词了
                 [02:34.14]当你我不小心又想起她
                 [01:07.00]当你我不小心又想起她
                 */
                LineInfo lineInfo = new LineInfo();
                lineInfo.content = content;
                lineInfo.start = measureStartTimeMillis(temp);
                lyricInfo.song_lines.add(lineInfo);
            }
        }
    }

    /**
     * 重置歌词内容
     */
    private void resetLyricInfo() {
        if (mLyricInfo != null) {
            if (mLyricInfo.song_lines != null) {
                mLyricInfo.song_lines.clear();
                mLyricInfo.song_lines = null;
            }
            mLyricInfo = null;
        }
    }

    /**
     * 初始化控件
     */
    private void resetView() {
        mCurrentPlayLine = mCurrentShowLine = 0;
        resetLyricInfo();
        invalidateView();
        mLineCount = 0;
        mScrollY = 0;
    }

    private void setRawTextSize(float size) {
        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);
            measureLineHeight();
            mScrollY = measureCurrentScrollY(mCurrentPlayLine);
            invalidateView();
        }
    }

    private float getRawSize(int unit, float size) {
        Context context = getContext();
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        return TypedValue.applyDimension(unit, size, resources.getDisplayMetrics());
    }

    /**
     * 设置当前时间显示位置
     *
     * @param current 时间戳
     */
    public void setCurrentTimeMillis(long current) {
        scrollToCurrentTimeMillis(current);
    }

    /**
     * 设置歌词文件
     *
     * @param file        歌词文件
     * @param charsetName 解析字符集
     */
    public void setLyricFile(File file, String charsetName) {
        if (file != null && file.exists()) {
            try {
                setupLyricResource(new FileInputStream(file), charsetName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            mDefaultHint = "暂无歌词";
            invalidateView();
        }
    }

    /**
     * 设置播放按钮点击监听事件
     *
     * @param mClickListener 监听器
     */
    public void setOnPlayerClickListener(OnPlayerClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    /**
     * 重置、设置歌词内容被重置后的提示内容
     *
     * @param message 提示内容
     */
    public void reset(String message) {
        mDefaultHint = message;
        resetView();
    }


    /*
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ *
     *                                                                                             对外API                                                                                        *
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ *
     * */

    /**
     * 设置高亮显示文本的字体颜色
     *
     * @param color 颜色值
     */
    public void setHighLightTextColor(int color) {
        if (mHighLightColor != color) {
            mHighLightColor = color;
            invalidateView();
        }
    }

    public int getDefaultColor() {
        return mDefaultColor;
    }

    public void setDefaultColor(int color) {
        if (mDefaultColor != color) {
            mDefaultColor = color;
            invalidateView();
        }
    }

    /**
     * 设置歌词内容行间距
     *
     * @param lineSpace 行间距大小
     */
    public void setLineSpace(float lineSpace) {
        if (mLineSpace != lineSpace) {
            mLineSpace = getRawSize(TypedValue.COMPLEX_UNIT_SP, lineSpace);
            measureLineHeight();
            mScrollY = measureCurrentScrollY(mCurrentPlayLine);
            invalidateView();
        }
    }

    /**
     * 设置歌词文本内容字体大小
     *
     * @param unit .
     * @param size .
     */
    public void setTextSize(int unit, float size) {
        setRawTextSize(getRawSize(unit, size));
    }

    /**
     * 设置歌词文本内容字体大小
     *
     * @param size .
     */
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setPlayable(boolean playable) {
        mPlayable = playable;
    }

    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    public void setHintColor(int color) {
        if (mHintColor != color) {
            mHintColor = color;
            invalidate();
        }
    }

    public interface OnPlayerClickListener {
        void onPlayerClicked(long progress, String content);
    }

    class LyricInfo {
        List<LineInfo> song_lines;

        String song_artist;  // 歌手
        String song_title;  // 标题
        String song_album;  // 专辑

        long song_offset;  // 偏移量
    }

    class LineInfo {
        String content;  // 歌词内容
        long start;  // 开始时间
    }

    class sort implements Comparator<LineInfo> {

        @Override
        public int compare(LineInfo lrc, LineInfo lrc2) {
            return Long.compare(lrc.start, lrc2.start);
        }
    }

}
