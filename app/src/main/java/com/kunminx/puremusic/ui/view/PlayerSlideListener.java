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

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.kunminx.architecture.utils.DisplayUtils;
import com.kunminx.architecture.utils.ScreenUtils;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.databinding.FragmentPlayerBinding;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Create by KunMinX at 19/10/29
 */
public class PlayerSlideListener implements SlidingUpPanelLayout.PanelSlideListener {

    private final FragmentPlayerBinding mBinding;
    private final SlidingUpPanelLayout mSlidingUpPanelLayout;
    private final int screenWidth;
    private final int screenHeight;
    private final IntEvaluator intEvaluator = new IntEvaluator();
    private final FloatEvaluator floatEvaluator = new FloatEvaluator();
    private final ArgbEvaluator colorEvaluator = new ArgbEvaluator();
    private int nowPlayingCardColor;
    private int playPauseDrawableColor;
	
    private Drawable albumImageDrawable;
    private int titleEndTranslationX;
    private int artistEndTranslationX;
    private int artistNormalEndTranslationY;
    private int artistFullEndTranslationY;
    private int contentNormalEndTranslationY;
    private int contentFullEndTranslationY;
    private int lyricLineHeight;
    private int lyricFullHeight;
    private int lyricLineStartTranslationY;
    private int lyricLineEndTranslationY;
    private int lyricFullTranslationY;
    private int markStartX;
    private int previousStartX;
    private int playPauseStartX;
    private int nextStartX;
    private int playqueueStartX;
    private int playPauseEndX;
    private int previousEndX;
    private int markEndX;
    private int nextEndX;
    private int playqueueEndX;
    private int iconContainerStartY;
    private int iconContainerEndY;
    private Status mStatus = Status.COLLAPSED;

    public PlayerSlideListener(FragmentPlayerBinding binding, SlidingUpPanelLayout slidingUpPanelLayout) {
        mBinding = binding;
        mSlidingUpPanelLayout = slidingUpPanelLayout;
        screenWidth = ScreenUtils.getScreenWidth();
        screenHeight = ScreenUtils.getScreenHeight();
        playPauseDrawableColor = Color.BLACK;
        nowPlayingCardColor = Color.WHITE;
        caculateTitleAndArtist();
        caculateIcons();
        caculateLyricView();
        mBinding.playPause.setDrawableColor(playPauseDrawableColor);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mBinding.albumArt.getLayoutParams();

        //animate albumImage
        int tempImgSize = intEvaluator.evaluate(slideOffset, DisplayUtils.dp2px(55), screenWidth);
        params.width = tempImgSize;
        params.height = tempImgSize;
        mBinding.albumArt.setLayoutParams(params);

        //animate title and artist
        mBinding.title.setTranslationX(floatEvaluator.evaluate(slideOffset, 0, titleEndTranslationX));
        mBinding.artist.setTranslationX(floatEvaluator.evaluate(slideOffset, 0, artistEndTranslationX));
        mBinding.artist.setTranslationY(floatEvaluator.evaluate(slideOffset, 0, artistNormalEndTranslationY));
        mBinding.summary.setTranslationY(floatEvaluator.evaluate(slideOffset, 0, contentNormalEndTranslationY));

        //aniamte icons
        mBinding.playPause.setX(intEvaluator.evaluate(slideOffset, playPauseStartX, playPauseEndX));
        mBinding.playPause.setCircleAlpah(intEvaluator.evaluate(slideOffset, 0, 255));
        mBinding.playPause.setDrawableColor((int) colorEvaluator.evaluate(slideOffset, playPauseDrawableColor, nowPlayingCardColor));
        mBinding.previous.setX(intEvaluator.evaluate(slideOffset, previousStartX, previousEndX));
        mBinding.mark.setX(intEvaluator.evaluate(slideOffset, markStartX, markEndX));
        mBinding.next.setX(intEvaluator.evaluate(slideOffset, nextStartX, nextEndX));
        mBinding.icPlayList.setX(intEvaluator.evaluate(slideOffset, playqueueStartX, playqueueEndX));
        mBinding.mark.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        mBinding.previous.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1));
        mBinding.iconContainer.setY(intEvaluator.evaluate(slideOffset, iconContainerStartY, iconContainerEndY));

        CoordinatorLayout.LayoutParams params1 = (CoordinatorLayout.LayoutParams) mBinding.summary.getLayoutParams();
        params1.height = intEvaluator.evaluate(slideOffset, DisplayUtils.dp2px(55), DisplayUtils.dp2px(60));
        mBinding.summary.setLayoutParams(params1);

        //animate lyric view
        mBinding.lyricView.setTranslationY(lyricLineStartTranslationY - (lyricLineStartTranslationY - lyricLineEndTranslationY) * slideOffset);
    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState,
                                    SlidingUpPanelLayout.PanelState newState) {
        if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            if (mBinding.songProgressNormal.getVisibility() != View.INVISIBLE) {
                mBinding.songProgressNormal.setVisibility(View.INVISIBLE);
            }
            if (mBinding.mark.getVisibility() != View.VISIBLE) {
                mBinding.mark.setVisibility(View.VISIBLE);
            }
            if (mBinding.previous.getVisibility() != View.VISIBLE) {
                mBinding.previous.setVisibility(View.VISIBLE);
            }
        } else if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            if (mStatus == Status.FULLSCREEN) {
                animateToNormal();
            }
        }

        if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mStatus = Status.EXPANDED;
            toolbarSlideIn(panel.getContext());
            mBinding.mark.setClickable(true);
            mBinding.previous.setClickable(true);
            mBinding.topContainer.setOnClickListener(v -> {
                if (mStatus == Status.EXPANDED) {
                    animateToFullscreen();
                } else if (mStatus == Status.FULLSCREEN) {
                    animateToNormal();
                } else {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            });
        } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mStatus = Status.COLLAPSED;
            if (mBinding.songProgressNormal.getVisibility() != View.VISIBLE) {
                mBinding.songProgressNormal.setVisibility(View.VISIBLE);
            }
            if (mBinding.mark.getVisibility() != View.GONE) {
                mBinding.mark.setVisibility(View.GONE);
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
            if (mBinding.lyricView.getVisibility() != View.VISIBLE) {
                mBinding.lyricView.setVisibility(View.VISIBLE);
            }
        }

    }

    private void caculateTitleAndArtist() {
        Rect titleBounds = new Rect();
        mBinding.title.getPaint().getTextBounds(mBinding.title.getText().toString(), 0,
                mBinding.title.getText().length(), titleBounds);
        int titleWidth = titleBounds.width();

        Rect artistBounds = new Rect();
        mBinding.artist.getPaint().getTextBounds(mBinding.artist.getText().toString(), 0,
                mBinding.artist.getText().length(), artistBounds);
        int artistWidth = artistBounds.width();

        titleEndTranslationX = (screenWidth / 2) - (titleWidth / 2) - DisplayUtils.dp2px(67);

        artistEndTranslationX = (screenWidth / 2) - (artistWidth / 2) - DisplayUtils.dp2px(67);
        artistNormalEndTranslationY = DisplayUtils.dp2px(12);
        artistFullEndTranslationY = 0;

        contentNormalEndTranslationY = screenWidth + DisplayUtils.dp2px(32);
        contentFullEndTranslationY = DisplayUtils.dp2px(32);

        if (mStatus == Status.COLLAPSED) {
            mBinding.title.setTranslationX(0);
            mBinding.artist.setTranslationX(0);
        } else {
            mBinding.title.setTranslationX(titleEndTranslationX);
            mBinding.artist.setTranslationX(artistEndTranslationX);
        }
    }

    private void caculateIcons() {
        markStartX = mBinding.mark.getLeft();
        previousStartX = mBinding.previous.getLeft();
        playPauseStartX = mBinding.playPause.getLeft();
        nextStartX = mBinding.next.getLeft();
        playqueueStartX = mBinding.icPlayList.getLeft();
        int size = DisplayUtils.dp2px(36);
        int gap = (screenWidth - 5 * (size)) / 6;
        playPauseEndX = (screenWidth / 2) - (size / 2);
        previousEndX = playPauseEndX - gap - size;
        markEndX = previousEndX - gap - size;
        nextEndX = playPauseEndX + gap + size;
        playqueueEndX = nextEndX + gap + size;
        iconContainerStartY = mBinding.iconContainer.getTop();
        iconContainerEndY = screenHeight - 3 * mBinding.iconContainer.getHeight() - mBinding.seekBottom.getHeight();
    }

    private void caculateLyricView() {
        int lyricFullMarginTop = mBinding.customToolbar.getTop()
                + mBinding.customToolbar.getHeight() + DisplayUtils.dp2px(32);

        int lyricFullMarginBottom = mBinding.iconContainer.getBottom()
                + mBinding.iconContainer.getHeight() + DisplayUtils.dp2px(32);

        lyricLineHeight = DisplayUtils.dp2px(32);
        lyricFullHeight = screenHeight - lyricFullMarginTop - lyricFullMarginBottom;

        lyricLineStartTranslationY = screenHeight;
        int gapBetweenArtistAndLyric = iconContainerEndY - contentNormalEndTranslationY - mBinding.summary.getHeight();
        lyricLineEndTranslationY = iconContainerEndY - gapBetweenArtistAndLyric / 2 - lyricLineHeight / 2;
        lyricFullTranslationY = mBinding.customToolbar.getTop()
                + mBinding.customToolbar.getHeight() + DisplayUtils.dp2px(32);
    }

    private void toolbarSlideIn(Context context) {
        mBinding.customToolbar.post(() -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.toolbar_slide_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mBinding.customToolbar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mBinding.customToolbar.startAnimation(animation);
        });
    }

    private void animateToFullscreen() {
        //album art fullscreen
        albumImageDrawable = mBinding.albumArt.getDrawable();

        //animate title and artist
        Animation contentAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mBinding.summary.setTranslationY(contentNormalEndTranslationY - (contentNormalEndTranslationY - contentFullEndTranslationY) * interpolatedTime);
                mBinding.artist.setTranslationY(artistNormalEndTranslationY - (artistNormalEndTranslationY - artistFullEndTranslationY) * interpolatedTime);
            }
        };
        contentAnimation.setDuration(150);
        mBinding.artist.startAnimation(contentAnimation);

        //animate lyric
        Animation lyricAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                CoordinatorLayout.LayoutParams lyricLayout = (CoordinatorLayout.LayoutParams) mBinding.lyricView.getLayoutParams();
                lyricLayout.height = (int) (lyricLineHeight + (lyricFullHeight - lyricLineHeight) * interpolatedTime);
                mBinding.lyricView.setLayoutParams(lyricLayout);
                mBinding.lyricView.setTranslationY(lyricLineEndTranslationY - (lyricLineEndTranslationY - lyricFullTranslationY) * interpolatedTime);
            }
        };
        lyricAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBinding.lyricView.setHighLightTextColor(Color.BLACK);
                mBinding.lyricView.setPlayable(true);
                mBinding.lyricView.setTouchable(true);
                mBinding.lyricView.setOnClickListener(v -> animateToNormal());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lyricAnimation.setDuration(150);
        mBinding.lyricView.startAnimation(lyricAnimation);

        mStatus = Status.FULLSCREEN;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void animateToNormal() {
        //album art
        CoordinatorLayout.LayoutParams imageLayout = (CoordinatorLayout.LayoutParams) mBinding.albumArt.getLayoutParams();
        imageLayout.height = screenWidth;
        imageLayout.width = screenWidth;
        mBinding.albumArt.setLayoutParams(imageLayout);

        //animate title and artist
        Animation contentAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mBinding.summary.setTranslationY(contentFullEndTranslationY + (contentNormalEndTranslationY - contentFullEndTranslationY) * interpolatedTime);
                mBinding.artist.setTranslationY(artistFullEndTranslationY + (artistNormalEndTranslationY - artistFullEndTranslationY) * interpolatedTime);
            }
        };
        contentAnimation.setDuration(300);
        mBinding.artist.startAnimation(contentAnimation);

        //adjust lyricview
        Animation lyricAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mBinding.lyricView.getLayoutParams();
                layoutParams.height = (int) (lyricFullHeight - (lyricFullHeight - lyricLineHeight) * interpolatedTime);
                mBinding.lyricView.setLayoutParams(layoutParams);
                mBinding.lyricView.setTranslationY(lyricFullTranslationY + (lyricLineEndTranslationY - lyricFullTranslationY) * interpolatedTime);
            }
        };
        lyricAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBinding.lyricView.setPlayable(false);
                mBinding.lyricView.setHighLightTextColor(mBinding.lyricView.getDefaultColor());
                mBinding.lyricView.setTouchable(false);
                mBinding.lyricView.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lyricAnimation.setDuration(300);
        mBinding.lyricView.setPlayable(false);
        mBinding.lyricView.startAnimation(lyricAnimation);


        mStatus = Status.EXPANDED;
    }

    public enum Status {
        EXPANDED,
        COLLAPSED,
        FULLSCREEN
    }


}
