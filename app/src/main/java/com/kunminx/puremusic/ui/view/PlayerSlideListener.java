/*
 * Copyright 2018-present KunMinX
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

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.kunminx.architecture.utils.DisplayUtils;
import com.kunminx.architecture.utils.ScreenUtils;
import com.kunminx.puremusic.databinding.FragmentPlayerBinding;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Create by KunMinX at 19/10/29
 */
public class PlayerSlideListener implements SlidingUpPanelLayout.PanelSlideListener {

    private final FragmentPlayerBinding mBinding;
    private final SlidingUpPanelLayout mSlidingUpPanelLayout;

    private int mTitleEndTranslationX;
    private int mArtistEndTranslationX;
    private int mArtistNormalEndTranslationY;
    private int mContentNormalEndTranslationY;

    private int mModeStartX;
    private int mPreviousStartX;
    private int mPlayPauseStartX;
    private int mNextStartX;
    private int mPlayQueueStartX;
    private int mPlayPauseEndX;
    private int mPreviousEndX;
    private int mModeEndX;
    private int mNextEndX;
    private int mPlayQueueEndX;
    private int mIconContainerStartY;
    private int mIconContainerEndY;

    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private final IntEvaluator INT_EVALUATOR = new IntEvaluator();
    private final FloatEvaluator FLOAT_EVALUATOR = new FloatEvaluator();
    private final ArgbEvaluator COLOR_EVALUATOR = new ArgbEvaluator();

    private final int NOW_PLAYING_CARD_COLOR;
    private final int PLAY_PAUSE_DRAWABLE_COLOR;
    private Status mStatus = Status.COLLAPSED;

    public enum Status {
        EXPANDED,
        COLLAPSED,
        FULLSCREEN
    }

    public PlayerSlideListener(FragmentPlayerBinding binding, SlidingUpPanelLayout slidingUpPanelLayout) {
        mBinding = binding;
        mSlidingUpPanelLayout = slidingUpPanelLayout;
        SCREEN_WIDTH = ScreenUtils.getScreenWidth();
        SCREEN_HEIGHT = ScreenUtils.getScreenHeight();
        PLAY_PAUSE_DRAWABLE_COLOR = Color.BLACK;
        NOW_PLAYING_CARD_COLOR = Color.WHITE;
        calculateTitleAndArtist();
        calculateIcons();
        mBinding.playPause.setDrawableColor(PLAY_PAUSE_DRAWABLE_COLOR);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

        calculateTitleAndArtist();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mBinding.albumArt.getLayoutParams();

        int tempImgSize = INT_EVALUATOR.evaluate(slideOffset, DisplayUtils.dp2px(55), SCREEN_WIDTH);
        params.width = tempImgSize;
        params.height = tempImgSize;
        mBinding.albumArt.setLayoutParams(params);

        mBinding.title.setTranslationX(FLOAT_EVALUATOR.evaluate(slideOffset, 0, mTitleEndTranslationX));
        mBinding.artist.setTranslationX(FLOAT_EVALUATOR.evaluate(slideOffset, 0, mArtistEndTranslationX));
        mBinding.artist.setTranslationY(FLOAT_EVALUATOR.evaluate(slideOffset, 0, mArtistNormalEndTranslationY));
        mBinding.summary.setTranslationY(FLOAT_EVALUATOR.evaluate(slideOffset, 0, mContentNormalEndTranslationY));

        mBinding.playPause.setX(INT_EVALUATOR.evaluate(slideOffset, mPlayPauseStartX, mPlayPauseEndX));
        mBinding.playPause.setCircleAlpha(INT_EVALUATOR.evaluate(slideOffset, 0, 255));
        mBinding.playPause.setDrawableColor((int) COLOR_EVALUATOR.evaluate(slideOffset, PLAY_PAUSE_DRAWABLE_COLOR, NOW_PLAYING_CARD_COLOR));
        mBinding.previous.setX(INT_EVALUATOR.evaluate(slideOffset, mPreviousStartX, mPreviousEndX));
        mBinding.mode.setX(INT_EVALUATOR.evaluate(slideOffset, mModeStartX, mModeEndX));
        mBinding.next.setX(INT_EVALUATOR.evaluate(slideOffset, mNextStartX, mNextEndX));
        mBinding.icPlayList.setX(INT_EVALUATOR.evaluate(slideOffset, mPlayQueueStartX, mPlayQueueEndX));
        mBinding.mode.setAlpha(FLOAT_EVALUATOR.evaluate(slideOffset, 0, 1));
        mBinding.previous.setAlpha(FLOAT_EVALUATOR.evaluate(slideOffset, 0, 1));
        mBinding.iconContainer.setY(INT_EVALUATOR.evaluate(slideOffset, mIconContainerStartY, mIconContainerEndY));

        CoordinatorLayout.LayoutParams params1 = (CoordinatorLayout.LayoutParams) mBinding.summary.getLayoutParams();
        params1.height = INT_EVALUATOR.evaluate(slideOffset, DisplayUtils.dp2px(55), DisplayUtils.dp2px(60));
        mBinding.summary.setLayoutParams(params1);

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState,
                                    SlidingUpPanelLayout.PanelState newState) {
        if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            if (mBinding.songProgressNormal.getVisibility() != View.INVISIBLE) {
                mBinding.songProgressNormal.setVisibility(View.INVISIBLE);
            }
            if (mBinding.mode.getVisibility() != View.VISIBLE) {
                mBinding.mode.setVisibility(View.VISIBLE);
            }
            if (mBinding.previous.getVisibility() != View.VISIBLE) {
                mBinding.previous.setVisibility(View.VISIBLE);
            }
        }

        if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mStatus = Status.EXPANDED;
            mBinding.customToolbar.setVisibility(View.VISIBLE);
        } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mStatus = Status.COLLAPSED;
            if (mBinding.songProgressNormal.getVisibility() != View.VISIBLE) {
                mBinding.songProgressNormal.setVisibility(View.VISIBLE);
            }
            if (mBinding.mode.getVisibility() != View.GONE) {
                mBinding.mode.setVisibility(View.GONE);
            }
            if (mBinding.previous.getVisibility() != View.GONE) {
                mBinding.previous.setVisibility(View.GONE);
            }
            mBinding.topContainer.setOnClickListener(v -> {
                if (mSlidingUpPanelLayout.isTouchEnabled()) {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            });
        } else if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
            if (mBinding.customToolbar.getVisibility() != View.INVISIBLE) {
                mBinding.customToolbar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void calculateTitleAndArtist() {
        int titleWidth = getTextWidth(mBinding.title);
        int artistWidth = getTextWidth(mBinding.artist);

        mTitleEndTranslationX = (SCREEN_WIDTH / 2) - (titleWidth / 2) - DisplayUtils.dp2px(67);
        mArtistEndTranslationX = (SCREEN_WIDTH / 2) - (artistWidth / 2) - DisplayUtils.dp2px(67);
        mArtistNormalEndTranslationY = DisplayUtils.dp2px(12);
        mContentNormalEndTranslationY = SCREEN_WIDTH + DisplayUtils.dp2px(32);

        mBinding.title.setTranslationX(mStatus == Status.COLLAPSED ? 0 : mTitleEndTranslationX);
        mBinding.artist.setTranslationX(mStatus == Status.COLLAPSED ? 0 : mArtistEndTranslationX);
    }

    private void calculateIcons() {
        mModeStartX = mBinding.mode.getLeft();
        mPreviousStartX = mBinding.previous.getLeft();
        mPlayPauseStartX = mBinding.playPause.getLeft();
        mNextStartX = mBinding.next.getLeft();
        mPlayQueueStartX = mBinding.icPlayList.getLeft();
        int size = DisplayUtils.dp2px(36);
        int gap = (SCREEN_WIDTH - 5 * (size)) / 6;
        mPlayPauseEndX = (SCREEN_WIDTH / 2) - (size / 2);
        mPreviousEndX = mPlayPauseEndX - gap - size;
        mModeEndX = mPreviousEndX - gap - size;
        mNextEndX = mPlayPauseEndX + gap + size;
        mPlayQueueEndX = mNextEndX + gap + size;
        mIconContainerStartY = mBinding.iconContainer.getTop();
        mIconContainerEndY = SCREEN_HEIGHT - 3 * mBinding.iconContainer.getHeight() - mBinding.seekBottom.getHeight();
    }

    private int getTextWidth(TextView textView) {
        Rect artistBounds = new Rect();
        textView.getPaint().getTextBounds(textView.getText().toString(), 0,
            textView.getText().length(), artistBounds);
        return artistBounds.width();
    }

}
