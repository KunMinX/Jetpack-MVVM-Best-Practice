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

package com.kunminx.puremusic;

import android.os.Bundle;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.architecture.ui.state.State;
import com.kunminx.puremusic.domain.event.Messages;
import com.kunminx.puremusic.domain.message.DrawerCoordinateManager;
import com.kunminx.puremusic.domain.message.PageMessenger;

/**
 * Create by KunMinX at 19/10/16
 */

public class MainActivity extends BaseActivity {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 event-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，
    // event-ViewModel 职责仅限于 "消息分发" 场景承担 "唯一可信源"。

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private MainActivityStates mStates;
    private PageMessenger mMessenger;
    private boolean mIsListened = false;

    @Override
    protected void initViewModel() {
        mStates = getActivityScopeViewModel(MainActivityStates.class);
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

        return new DataBindingConfig(R.layout.activity_main, BR.vm, mStates)
            .addBindingParam(BR.listener, new ListenerHandler());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMessenger.output(this, messages -> {
            switch (messages.eventId) {
                case Messages.EVENT_CLOSE_ACTIVITY_IF_ALLOWED:
                    NavController nav = Navigation.findNavController(this, R.id.main_fragment_host);
                    if (nav.getCurrentDestination() != null && nav.getCurrentDestination().getId() != R.id.mainFragment) {
                        nav.navigateUp();
                    } else if (Boolean.TRUE.equals(mStates.isDrawerOpened.get())) {

                        //TODO 同 tip 3
                        mStates.openDrawer.set(false);
                    } else {
                        super.onBackPressed();
                    }
                    break;
                case Messages.EVENT_OPEN_DRAWER:
                    //TODO yes：同 tip 2: 此处将 drawer 的 open 和 close 都放在 drawerBindingAdapter 中操作，规避 View 实例 Null 安全一致性问题，
                    //因为横屏布局无 drawerLayout。此处如果用手动判空，很容易因疏忽而造成空引用。

                    //TODO 此外，此处为 drawerLayout 绑定状态 "openDrawer"，使用 "去防抖" ObservableField 子类，主要考虑到 ObservableField 具有 "防抖" 特性，不适合该场景。

                    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

                    mStates.openDrawer.set(true);

                    //TODO do not:（容易因疏忽埋下 View 实例 Null 安全一致性隐患）

                    /*if (mBinding.dl != null) {
                        if (aBoolean && !mBinding.dl.isDrawerOpen(GravityCompat.START)) {
                            mBinding.dl.openDrawer(GravityCompat.START);
                        } else {
                            mBinding.dl.closeDrawer(GravityCompat.START);
                        }
                    }*/
                    break;
            }
        });

        DrawerCoordinateManager.getInstance().isEnableSwipeDrawer().observe(this, aBoolean -> {

            //TODO yes: 同 tip 2

            mStates.allowDrawerOpen.set(aBoolean);

            // TODO do not:（容易因疏忽埋下 View 实例 Null 安全一致性隐患）

            /*if (mBinding.dl != null) {
                mBinding.dl.setDrawerLockMode(aBoolean
                        ? DrawerLayout.LOCK_MODE_UNLOCKED
                        : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }*/
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!mIsListened) {

            // TODO tip 3：此处演示向 "唯一可信源" 发送请求，以便实现 "生命周期安全、消息分发可靠一致" 的通知。

            // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一思想是 "最少知道原则"，
            // Activity 内部事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部东西。
            // 因为 Activity 端的处理后续可能会改变，且可受用于更多 fragment，而不单单是本 fragment。

            mMessenger.input(new Messages(Messages.EVENT_ADD_SLIDE_LISTENER));

            mIsListened = true;
        }
    }

    @Override
    public void onBackPressed() {

        // TODO 同 tip 3

        mMessenger.input(new Messages(Messages.EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED));
    }

    public class ListenerHandler extends DrawerLayout.SimpleDrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            mStates.isDrawerOpened.set(true);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            mStates.isDrawerOpened.set(false);
            mStates.openDrawer.set(false);
        }
    }

    //TODO tip 4：每个页面都需单独准备一个 state-ViewModel，托管 DataBinding 绑定的 State，
    // 此外，state-ViewModel 职责仅限于状态托管和保存恢复，不建议在此处理 UI 逻辑，
    // UI 逻辑只适合在 Activity/Fragment 等视图控制器中完成，是 “数据驱动” 一部分，将来升级到 Jetpack Compose 更是如此。

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

    public static class MainActivityStates extends StateHolder {

        //TODO tip 5：此处我们使用 "去除防抖特性" 的 ObservableField 子类 State，用以代替 MutableLiveData，

        //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

        public final State<Boolean> isDrawerOpened = new State<>(false);

        public final State<Boolean> openDrawer = new State<>(false);

        public final State<Boolean> allowDrawerOpen = new State<>(true);

    }
}
