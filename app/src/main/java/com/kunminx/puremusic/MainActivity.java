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

package com.kunminx.puremusic;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.kunminx.puremusic.bridge.state.MainActivityViewModel;
import com.kunminx.puremusic.databinding.ActivityMainBinding;
import com.kunminx.puremusic.ui.base.BaseActivity;

/**
 * Create by KunMinX at 19/10/16
 */

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;
    private MainActivityViewModel mMainActivityViewModel;
    private boolean isListened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivityViewModel = getActivityViewModelProvider(this).get(MainActivityViewModel.class);

        // TODO tip 1: 此处通过 DataBinding 来规避 潜在的 视图调用的一致性问题，

        // 因为本项目采用 横、竖 两套布局，且不同布局的控件存在差异，
        // 在 DataBinding 适配器模式的加持下，有绑定就有绑定，没绑定也没什么大不了的，
        // 总之 不会因一致性问题造成 视图调用的空指针异常。

        // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setLifecycleOwner(this);
        mBinding.setVm(mMainActivityViewModel);

        mSharedViewModel.activityCanBeClosedDirectly.observe(this, aBoolean -> {
            NavController nav = Navigation.findNavController(this, R.id.main_fragment_host);
            if (nav.getCurrentDestination() != null && nav.getCurrentDestination().getId() != R.id.mainFragment) {
                nav.navigateUp();

                //TODO tip 6: 同 tip 5.

            } else if (mBinding.dl != null && mBinding.dl.isDrawerOpen(GravityCompat.START)) {
                mBinding.dl.closeDrawer(GravityCompat.START);

            } else {
                super.onBackPressed();
            }
        });

        // TODO tip 4：同 tip 2.

        // TODO tip 5: 同 tip 1，这边我将 drawer 的 open 和 close 都放在 bindingAdapter 中操作，

        // 规避了视图的一致性问题，因为 横屏布局 根本就没有 drawerLayout，此处如果用传统的视图调用方式，会很容易疏忽而造成空引用。

        mSharedViewModel.openOrCloseDrawer.observe(this, aBoolean -> {
            //TODO yes:

            //TODO 此处绑定的状态，使用 LiveData 而不是 ObservableField，主要是考虑到 ObservableField 具有防抖的特性，不适合该场景。

            //如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350

            mMainActivityViewModel.openDrawer.setValue(aBoolean);

            //TODO do not:（容易埋下视图调用的一致性隐患）

            /*if (mBinding.dl != null) {
                if (aBoolean && !mBinding.dl.isDrawerOpen(GravityCompat.START)) {
                    mBinding.dl.openDrawer(GravityCompat.START);
                } else {
                    mBinding.dl.closeDrawer(GravityCompat.START);
                }
            }*/
        });

        mSharedViewModel.enableSwipeDrawer.observe(this, aBoolean -> {

            //TODO yes:

            mMainActivityViewModel.allowDrawerOpen.setValue(aBoolean);

            // TODO tip 7: do not:（容易埋下视图调用的一致性隐患）

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
        if (!isListened) {

            // TODO tip 2：此处演示通过 UnPeekLiveData 来发送 生命周期安全的、事件源可追溯的 通知。

            // 如果这么说还不理解的话，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一个思想是 最少知道原则，
            // fragment 内部的事情在 fragment 内部消化，不要试图在 Activity 中调用和操纵 Fragment 内部的东西。
            // 因为 fragment 端的处理后续可能会改变，并且可受用于更多的 Activity，而不单单是本 Activity。

            mSharedViewModel.timeToAddSlideListener.setValue(true);

            isListened = true;
        }
    }

    @Override
    public void onBackPressed() {

        // TODO tip 3：同 tip 2

        mSharedViewModel.closeSlidePanelIfExpanded.setValue(true);
    }
}
