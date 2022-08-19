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
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.architecture.ui.state.State;
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
    private final SlideAnimatorStates mStates;

    private int mTitleEndTranslationX;
    private int mArtistEndTranslationX;
    private int mArtistNormalEndTranslationY;
    private int mContentNormalEndTranslationY;

    private final int mModeStartX;
    private final int mPreviousStartX;
    private final int mPlayPauseStartX;
    private final int mNextStartX;
    private final int mPlayQueueStartX;
    private final int mPlayPauseEndX;
    private final int mPreviousEndX;
    private final int mModeEndX;
    private final int mNextEndX;
    private final int mPlayQueueEndX;
    private final int mIconContainerStartY;
    private final int mIconContainerEndY;

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
    }

    public PlayerSlideListener(FragmentPlayerBinding binding, SlideAnimatorStates states, SlidingUpPanelLayout slidingUpPanelLayout) {
        mBinding = binding;
        mStates = states;
        mSlidingUpPanelLayout = slidingUpPanelLayout;
        SCREEN_WIDTH = ScreenUtils.getScreenWidth();
        SCREEN_HEIGHT = ScreenUtils.getScreenHeight();
        PLAY_PAUSE_DRAWABLE_COLOR = Color.BLACK;
        NOW_PLAYING_CARD_COLOR = Color.WHITE;
        calculateTitleAndArtist();
        mModeStartX = binding.mode != null ? binding.mode.getLeft() : 0;
        mPreviousStartX = binding.previous.getLeft();
        mPlayPauseStartX = binding.playPause.getLeft();
        mNextStartX = binding.next.getLeft();
        mPlayQueueStartX = binding.icPlayList != null ? binding.icPlayList.getLeft() : 0;
        int size = DisplayUtils.dp2px(36);
        int gap = (SCREEN_WIDTH - 5 * (size)) / 6;
        mPlayPauseEndX = (SCREEN_WIDTH / 2) - (size / 2);
        mPreviousEndX = mPlayPauseEndX - gap - size;
        mModeEndX = mPreviousEndX - gap - size;
        mNextEndX = mPlayPauseEndX + gap + size;
        mPlayQueueEndX = mNextEndX + gap + size;
        mIconContainerStartY = binding.iconContainer.getTop();
        int tempImgSize = DisplayUtils.dp2px(55);
        mStates.albumArtSize.set(new Pair<>(tempImgSize, tempImgSize));
        mIconContainerEndY = SCREEN_HEIGHT - 3 * binding.iconContainer.getHeight() - binding.seekBottom.getHeight();
        mStates.playPauseDrawableColor.set(PLAY_PAUSE_DRAWABLE_COLOR);
        mStates.playCircleAlpha.set(INT_EVALUATOR.evaluate(0, 0, 255));
        mStates.nextX.set(mNextStartX);
    mStates.modeX.set(0);
    mStates.previousX.set(0);
        mStates.playPauseX.set(mPlayPauseStartX);
        mStates.iconContainerY.set(mIconContainerStartY);
        mBinding.executePendingBindings();
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        calculateTitleAndArtist();
        int tempImgSize = INT_EVALUATOR.evaluate(slideOffset, DisplayUtils.dp2px(55), SCREEN_WIDTH);
        mStates.albumArtSize.set(new Pair<>(tempImgSize, tempImgSize));
        mStates.titleTranslationX.set(FLOAT_EVALUATOR.evaluate(slideOffset, 0, mTitleEndTranslationX));
        mStates.artistTranslationX.set(FLOAT_EVALUATOR.evaluate(slideOffset, 0, mArtistEndTranslationX));
        mStates.artistTranslationY.set(FLOAT_EVALUATOR.evaluate(slideOffset, 0, mArtistNormalEndTranslationY));
        mStates.summaryTranslationY.set(FLOAT_EVALUATOR.evaluate(slideOffset, 0, mContentNormalEndTranslationY));
        mStates.playPauseX.set(INT_EVALUATOR.evaluate(slideOffset, mPlayPauseStartX, mPlayPauseEndX));
        mStates.playCircleAlpha.set(INT_EVALUATOR.evaluate(slideOffset, 0, 255));
        mStates.playPauseDrawableColor.set((int) COLOR_EVALUATOR.evaluate(slideOffset, PLAY_PAUSE_DRAWABLE_COLOR, NOW_PLAYING_CARD_COLOR));
        mStates.previousX.set(INT_EVALUATOR.evaluate(slideOffset, mPreviousStartX, mPreviousEndX));
        mStates.modeX.set(INT_EVALUATOR.evaluate(slideOffset, mModeStartX, mModeEndX));
        mStates.nextX.set(INT_EVALUATOR.evaluate(slideOffset, mNextStartX, mNextEndX));
        mStates.icPlayListX.set(INT_EVALUATOR.evaluate(slideOffset, mPlayQueueStartX, mPlayQueueEndX));
        mStates.modeAlpha.set(FLOAT_EVALUATOR.evaluate(slideOffset, 0, 1));
        mStates.previousAlpha.set(FLOAT_EVALUATOR.evaluate(slideOffset, 0, 1));
        mStates.iconContainerY.set(INT_EVALUATOR.evaluate(slideOffset, mIconContainerStartY, mIconContainerEndY));
        mBinding.executePendingBindings();
    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mStates.songProgressNormalVisibility.set(false);
            mStates.modeVisibility.set(true);
            mStates.previousVisibility.set(true);
        }
        if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mStatus = Status.EXPANDED;
            mStates.customToolbarVisibility.set(true);
        } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mStatus = Status.COLLAPSED;
            mStates.songProgressNormalVisibility.set(true);
            mStates.modeVisibility.set(false);
            mStates.previousVisibility.set(false);
            mBinding.topContainer.setOnClickListener(v -> {
                if (mSlidingUpPanelLayout.isTouchEnabled())
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            });
        } else if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
            mStates.customToolbarVisibility.set(false);
        }
    }

    public void calculateTitleAndArtist() {
        int titleWidth = getTextWidth(mBinding.title != null ? mBinding.title : null);
        int artistWidth = getTextWidth(mBinding.artist != null ? mBinding.artist : null);
        mTitleEndTranslationX = (SCREEN_WIDTH / 2) - (titleWidth / 2) - DisplayUtils.dp2px(67);
        mArtistEndTranslationX = (SCREEN_WIDTH / 2) - (artistWidth / 2) - DisplayUtils.dp2px(67);
        mArtistNormalEndTranslationY = DisplayUtils.dp2px(12);
        mContentNormalEndTranslationY = SCREEN_WIDTH + DisplayUtils.dp2px(32);
        mStates.titleTranslationX.set(mStatus == Status.COLLAPSED ? 0f : mTitleEndTranslationX);
        mStates.artistTranslationX.set(mStatus == Status.COLLAPSED ? 0f : mArtistEndTranslationX);
    }

    private int getTextWidth(TextView textView) {
        if (textView == null) return 0;
        Rect artistBounds = new Rect();
        textView.getPaint().getTextBounds(textView.getText().toString(), 0, textView.getText().length(), artistBounds);
        return artistBounds.width();
    }

    /**
     * TODO tip：使用 ObservableField 绑定，尽可能减少 View 实例 Null 安全一致性问题
     * <p>
     *  如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
     */
    public static class SlideAnimatorStates extends StateHolder {
        public final State<Float> titleTranslationX = new State<>(0f);
        public final State<Float> artistTranslationX = new State<>(0f);
        public final State<Float> artistTranslationY = new State<>(0f);
        public final State<Float> summaryTranslationY = new State<>(0f);
        public final State<Integer> playPauseX = new State<>(0);
        public final State<Integer> playCircleAlpha = new State<>(0);
        public final State<Integer> playPauseDrawableColor = new State<>(0);
        public final State<Integer> previousX = new State<>(0);
        public final State<Integer> modeX = new State<>(0);
        public final State<Integer> nextX = new State<>(0);
        public final State<Integer> icPlayListX = new State<>(0);
        public final State<Float> modeAlpha = new State<>(0f);
        public final State<Float> previousAlpha = new State<>(0f);
        public final State<Integer> iconContainerY = new State<>(0);
        public final State<Boolean> songProgressNormalVisibility = new State<>(false);
        public final State<Boolean> modeVisibility = new State<>(false);
        public final State<Boolean> previousVisibility = new State<>(false);
        public final State<Boolean> customToolbarVisibility = new State<>(false);
        public final State<Pair<Integer, Integer>> albumArtSize = new State<>(new Pair<>(0, 0));
    }
}
