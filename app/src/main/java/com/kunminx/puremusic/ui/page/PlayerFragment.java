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

package com.kunminx.puremusic.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.kunminx.player.PlayingInfoManager;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.bridge.callback.SharedViewModel;
import com.kunminx.puremusic.bridge.state.PlayerViewModel;
import com.kunminx.puremusic.databinding.FragmentPlayerBinding;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.base.BaseFragment;
import com.kunminx.puremusic.ui.view.PlayerSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Create by KunMinX at 19/10/29
 */
public class PlayerFragment extends BaseFragment {

    private FragmentPlayerBinding mBinding;
    private PlayerViewModel mPlayerViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        // TODO tip 1: 此处通过 DataBinding 来规避 潜在的 视图调用的一致性问题，

        // 因为本项目采用 横、竖 两套布局，且不同布局的控件存在差异，
        // 在 DataBinding 的适配器模式加持下，有绑定就有绑定，没绑定也没什么大不了的，
        // 总之 不会因一致性问题造成 视图调用的空指针。

        // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

        mBinding = FragmentPlayerBinding.bind(view);
        mBinding.setClick(new ClickProxy());
        mBinding.setVm(mPlayerViewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSharedViewModel.timeToAddSlideListener.observe(this, aBoolean -> {
            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
                sliding.addPanelSlideListener(new PlayerSlideListener(mBinding, sliding));
                sliding.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                    @Override
                    public void onPanelSlide(View view, float v) {

                    }

                    @Override
                    public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState,
                                                    SlidingUpPanelLayout.PanelState panelState1) {

                        if (panelState1 == SlidingUpPanelLayout.PanelState.EXPANDED) {
                            SharedViewModel.tagOfSecondaryPages.add(this.getClass().getSimpleName());
                        } else {
                            SharedViewModel.tagOfSecondaryPages.remove(this.getClass().getSimpleName());
                        }
                        mSharedViewModel.enableSwipeDrawer.setValue(SharedViewModel.tagOfSecondaryPages.size() == 0);
                    }
                });
            }
        });

        PlayerManager.getInstance().getChangeMusicLiveData().observe(this, changeMusic -> {

            // TODO tip 3：同 tip 2.

            // 切歌时，音乐的标题、作者、封面 状态的改变
            mPlayerViewModel.title.set(changeMusic.getTitle());
            mPlayerViewModel.artist.set(changeMusic.getSummary());
            mPlayerViewModel.coverImg.set(changeMusic.getImg());
        });

        PlayerManager.getInstance().getPlayingMusicLiveData().observe(this, playingMusic -> {

            // TODO tip 4：同 tip 2.

            // 播放进度 状态的改变
            mPlayerViewModel.maxSeekDuration.set(playingMusic.getDuration());
            mPlayerViewModel.currentSeekPosition.set(playingMusic.getPlayerPosition());
        });

        PlayerManager.getInstance().getPauseLiveData().observe(this, aBoolean -> {

            // TODO tip 2：所有播放状态的改变，都要通过这个 作为 唯一可信源 的 PlayerManager 来统一分发，

            // 如此才能方便 追溯事件源、保证 全应用范围内 所有状态的正确和及时，以及 避免 不可预期的 推送和错误。

            // 👆👆👆 划重点

            // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249

            // 播放按钮 状态的改变
            mPlayerViewModel.isPlaying.set(!aBoolean);
        });

        PlayerManager.getInstance().getPlayModeLiveData().observe(this, anEnum -> {
            int tip = 0;
            if (anEnum == PlayingInfoManager.RepeatMode.LIST_LOOP) {
                mPlayerViewModel.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT);
                tip = R.string.play_repeat;
            } else if (anEnum == PlayingInfoManager.RepeatMode.ONE_LOOP) {
                mPlayerViewModel.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
                tip = R.string.play_repeat_once;
            } else {
                mPlayerViewModel.playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE);
                tip = R.string.play_shuffle;
            }
            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
                if (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    showShortToast(tip);
                }
            }
        });

        mSharedViewModel.closeSlidePanelIfExpanded.observe(this, aBoolean -> {

            // 按下返回键，如果此时 slide 面板是展开的，那么只对面板进行 slide down

            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {

                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();

                if (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {

                    // TODO tip 6：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

                    // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
                    // --------
                    // 与此同时，此处传达的另一个思想是 最少知道原则，
                    // Activity 内部的事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部的东西。
                    // 因为 Activity 端的处理后续可能会改变，并且可受用于更多的 fragment，而不单单是本 fragment。

                    // TODO: yes:
                    mSharedViewModel.activityCanBeClosedDirectly.setValue(true);

                    // TODO: do not:
                    // mActivity.finish();
                }
            } else {
                mSharedViewModel.activityCanBeClosedDirectly.setValue(true);
            }
        });


    }

    // TODO tip 7：此处通过 DataBinding 来规避 在 setOnClickListener 时存在的 视图调用的一致性问题，

    // 也即，有绑定就有绑定，没绑定也没什么大不了的，总之 不会因一致性问题造成 视图调用的空指针。
    // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

    public class ClickProxy implements SeekBar.OnSeekBarChangeListener {

        public void playMode() {
            PlayerManager.getInstance().changeMode();
        }

        public void previous() {
            PlayerManager.getInstance().playPrevious();
        }

        public void togglePlay() {
            PlayerManager.getInstance().togglePlay();
        }

        public void next() {
            PlayerManager.getInstance().playNext();
        }

        public void showPlayList() {
            showShortToast(R.string.unfinished);
        }

        public void slideDown() {
            mSharedViewModel.closeSlidePanelIfExpanded.setValue(true);
        }

        public void more() {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            PlayerManager.getInstance().setSeek(seekBar.getProgress());
        }
    }

}
