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

package com.kunminx.puremusic.ui.page;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.player.PlayingInfoManager;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.databinding.FragmentPlayerBinding;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.callback.SharedViewModel;
import com.kunminx.puremusic.ui.state.PlayerViewModel;
import com.kunminx.puremusic.ui.view.PlayerSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Create by KunMinX at 19/10/29
 */
public class PlayerFragment extends BaseFragment {

    private PlayerViewModel mPlayerViewModel;
    private SharedViewModel mSharedViewModel;

    @Override
    protected void initViewModel() {
        mPlayerViewModel = getFragmentViewModel(PlayerViewModel.class);
        mSharedViewModel = getAppViewModelProvider().get(SharedViewModel.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这样的方式，来彻底解决 视图调用的一致性问题，
        // 如此，视图刷新的安全性将和基于函数式编程的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样的背景下，用于为 base 页面中的 DataBinding 提供绑定项。

        // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_player, BR.vm, mPlayerViewModel)
                .addBindingParam(BR.click, new ClickProxy())
                .addBindingParam(BR.event, new EventHandler());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO tip 7:
        // getViewLifeCycleOwner 是 2020 年新增的特性，
        // 主要是为了解决 getView() 的生命长度 比 fragment 短（仅存活于 onCreateView 之后和 onDestroyView 之前），
        // 导致某些时候 fragment 其他成员还活着，但 getView() 为 null 的 生命周期安全问题，
        // 也即，在 fragment 的场景下，请使用 getViewLifeCycleOwner 来作为 liveData 的观察者。
        // Activity 则不用改变。

        mSharedViewModel.timeToAddSlideListener.observe(getViewLifecycleOwner(), aBoolean -> {
            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
                sliding.addPanelSlideListener(new PlayerSlideListener((FragmentPlayerBinding) getBinding(), sliding));
                sliding.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                    @Override
                    public void onPanelSlide(View view, float v) {

                    }

                    @Override
                    public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState,
                                                    SlidingUpPanelLayout.PanelState panelState1) {

                        if (panelState1 == SlidingUpPanelLayout.PanelState.EXPANDED) {
                            SharedViewModel.TAG_OF_SECONDARY_PAGES.add(this.getClass().getSimpleName());
                        } else {
                            SharedViewModel.TAG_OF_SECONDARY_PAGES.remove(this.getClass().getSimpleName());
                        }
                        SharedViewModel.ENABLE_SWIPE_DRAWER.setValue(SharedViewModel.TAG_OF_SECONDARY_PAGES.size() == 0);
                    }
                });
            }
        });

        PlayerManager.getInstance().getChangeMusicLiveData().observe(getViewLifecycleOwner(), changeMusic -> {

            // TODO tip 3：同 tip 2.

            // 切歌时，音乐的标题、作者、封面 状态的改变
            mPlayerViewModel.title.set(changeMusic.getTitle());
            mPlayerViewModel.artist.set(changeMusic.getSummary());
            mPlayerViewModel.coverImg.set(changeMusic.getImg());
        });

        PlayerManager.getInstance().getPlayingMusicLiveData().observe(getViewLifecycleOwner(), playingMusic -> {

            // TODO tip 4：同 tip 2.

            // 播放进度 状态的改变
            mPlayerViewModel.maxSeekDuration.set(playingMusic.getDuration());
            mPlayerViewModel.currentSeekPosition.set(playingMusic.getPlayerPosition());
        });

        PlayerManager.getInstance().getPauseLiveData().observe(getViewLifecycleOwner(), aBoolean -> {

            // TODO tip 2：所有播放状态的改变，都要通过这个 作为 唯一可信源 的 PlayerManager 来统一分发，

            // 如此才能方便 追溯事件源、保证 全应用范围内 所有状态的正确和及时，以及 避免 不可预期的 推送和错误。

            // 👆👆👆 划重点

            // 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249

            // 播放按钮 状态的改变
            mPlayerViewModel.isPlaying.set(!aBoolean);
        });

        PlayerManager.getInstance().getPlayModeLiveData().observe(getViewLifecycleOwner(), anEnum -> {
            int tip;
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

        mSharedViewModel.closeSlidePanelIfExpanded.observe(getViewLifecycleOwner(), aBoolean -> {

            // 按下返回键，如果此时 slide 面板是展开的，那么只对面板进行 slide down

            if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {

                SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();

                if (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else {

                    // TODO tip 6：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

                    // fragment 与 Activity 的交互，同属于页面通信的范畴，适合统一地以 页面通信 的方式实现。

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

    public class ClickProxy {

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
    }

    public static class EventHandler implements SeekBar.OnSeekBarChangeListener {

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
