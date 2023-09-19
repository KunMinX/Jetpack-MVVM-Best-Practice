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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kunminx.architecture.ui.page.BaseFragment;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.architecture.ui.state.State;
import com.kunminx.architecture.utils.Res;
import com.kunminx.architecture.utils.ToastUtils;
import com.kunminx.architecture.utils.Utils;
import com.kunminx.puremusic.BR;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.databinding.FragmentPlayerBinding;
import com.kunminx.puremusic.domain.event.Messages;
import com.kunminx.puremusic.domain.message.DrawerCoordinateManager;
import com.kunminx.puremusic.domain.message.PageMessenger;
import com.kunminx.puremusic.domain.proxy.PlayerManager;
import com.kunminx.puremusic.ui.page.helper.DefaultInterface;
import com.kunminx.puremusic.ui.view.PlayerSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Create by KunMinX at 19/10/29
 */
public class PlayerFragment extends BaseFragment {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 result-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，作用域仅限于本页面，
    // result-ViewModel 职责仅限于 "消息分发" 场景承担 "可信源"，作用域依 "数据请求" 或 "跨页通信" 消息分发范围而定

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private PlayerStates mStates;
    private PlayerSlideListener.SlideAnimatorStates mAnimatorStates;
    private PageMessenger mMessenger;
    private PlayerSlideListener mListener;

    @Override
    protected void initViewModel() {
        mStates = getFragmentScopeViewModel(PlayerStates.class);
        mAnimatorStates = getFragmentScopeViewModel(PlayerSlideListener.SlideAnimatorStates.class);
        mMessenger = getApplicationScopeViewModel(PageMessenger.class);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {

        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return new DataBindingConfig(R.layout.fragment_player, BR.vm, mStates)
            .addBindingParam(BR.panelVm, mAnimatorStates)
            .addBindingParam(BR.click, new ClickProxy())
            .addBindingParam(BR.listener, new ListenerHandler());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO tip 3: 此处演示使用 "可信源" MVI-Dispatcher input-output 接口完成消息收发

        //如这么说无体会，详见《领域层设计》篇拆解 https://juejin.cn/post/7117498113983512589

        mMessenger.output(this, messages -> {
            switch (messages.eventId) {
                case Messages.EVENT_ADD_SLIDE_LISTENER:
                    if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
                        SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();

                        //TODO tip 4: 警惕使用。非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
                        // 目前方案是于 debug 模式，对获取实例情况给予提示。

                        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

                        mListener = new PlayerSlideListener((FragmentPlayerBinding) getBinding(), mAnimatorStates, sliding);
                        sliding.addPanelSlideListener(mListener);
                        sliding.addPanelSlideListener(new DefaultInterface.PanelSlideListener() {
                            @Override
                            public void onPanelStateChanged(
                                View view, SlidingUpPanelLayout.PanelState panelState,
                                SlidingUpPanelLayout.PanelState panelState1) {
                                DrawerCoordinateManager.getInstance().requestToUpdateDrawerMode(
                                    panelState1 == SlidingUpPanelLayout.PanelState.EXPANDED,
                                    this.getClass().getSimpleName()
                                );
                            }
                        });
                    }
                    break;
                case Messages.EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED:
                    // 按下返回键，如果此时 slide 面板是展开的，那么只对面板进行 slide down

                    if (view.getParent().getParent() instanceof SlidingUpPanelLayout) {
                        SlidingUpPanelLayout sliding = (SlidingUpPanelLayout) view.getParent().getParent();
                        if (sliding.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                            sliding.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        } else {

                            // TODO tip 5：此处演示向 "可信源" 发送请求，以便实现 "生命周期安全、消息分发可靠一致" 的通知。

                            // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/0168753249
                            // --------
                            // 与此同时，此处传达的另一思想是 "最少知道原则"，
                            // Activity 内部事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部东西。
                            // 因为 Activity 端的处理后续可能会改变，且可受用于更多 fragment，而不单单是本 fragment。

                            // TODO: yes:

                            mMessenger.input(new Messages(Messages.EVENT_CLOSE_ACTIVITY_IF_ALLOWED));

                            // TODO: do not:
                            // mActivity.finish();
                        }
                    } else {
                        mMessenger.input(new Messages(Messages.EVENT_CLOSE_ACTIVITY_IF_ALLOWED));
                    }
                    break;
            }
        });

        // TODO tip 6：所有播放状态的改变，皆来自 "可信源" PlayerInfoDispatcher 统一分发，
        //  确保 "消息分发可靠一致"，避免不可预期推送和错误。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/6017825943 & https://juejin.cn/post/7117498113983512589

        PlayerManager.getInstance().getUiStates().observe(getViewLifecycleOwner(), uiStates -> {
            mStates.title.set(uiStates.getTitle());
            mStates.artist.set(uiStates.getSummary());
            mStates.coverImg.set(uiStates.getImg(), onDiff -> {
                if (mListener != null) view.post(mListener::calculateTitleAndArtist);
            });
            mStates.isPlaying.set(!uiStates.isPaused());
            mStates.playModeIcon.set(PlayerManager.getInstance().getModeIcon(uiStates.getRepeatMode()));
            mStates.maxSeekDuration.set(uiStates.getDuration());
            mStates.currentSeekPosition.set(uiStates.getProgress());
        });
    }

    // TODO tip 7：此处通过 DataBinding 规避 setOnClickListener 时存在的 View 实例 Null 安全一致性问题，

    // 也即，有视图就绑定，无就无绑定，总之 不会因不一致性造成 View 实例 Null 安全问题。
    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

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
            ToastUtils.showShortToast(getString(R.string.unfinished));
        }

        //TODO tip: 同 tip 3

        public void slideDown() {
            mMessenger.input(new Messages(Messages.EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED));
        }

        public void more() {
        }
    }

    public static class ListenerHandler implements DefaultInterface.OnSeekBarChangeListener {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            PlayerManager.getInstance().setSeek(seekBar.getProgress());
        }
    }

    //TODO tip 8：基于单一职责原则，抽取 Jetpack ViewModel "状态保存和恢复" 的能力作为 StateHolder，
    // 并使用 ObservableField 的改良版子类 State 来承担 BehaviorSubject，用作所绑定控件的 "可信数据源"，
    // 从而在收到来自 PublishSubject 的结果回推后，响应结果数据的变化，也即通知控件属性重新渲染，并为其兜住最后一次状态，

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805

    public static class PlayerStates extends StateHolder {
        public final State<String> title = new State<>(Utils.getApp().getString(R.string.app_name), true);
        public final State<String> artist = new State<>(Utils.getApp().getString(R.string.app_name), true);
        public final State<String> coverImg = new State<>("", true);
        public final State<Drawable> placeHolder = new State<>(Res.getDrawable(R.drawable.bg_album_default), true);
        public final State<Integer> maxSeekDuration = new State<>(0, true);
        public final State<Integer> currentSeekPosition = new State<>(0, true);
        public final State<Boolean> isPlaying = new State<>(false, true);
        public final State<MaterialDrawableBuilder.IconValue> playModeIcon = new State<>(PlayerManager.getInstance().getModeIcon(), true);
    }

}
