package com.kunminx.puremusic.ui.base.binding;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.databinding.BindingAdapter;
import androidx.drawerlayout.widget.DrawerLayout;

import com.kunminx.puremusic.App;
import com.kunminx.puremusic.bridge.callback.SharedViewModel;

/**
 * Create by KunMinX at 2020/3/13
 */
public class DrawerBindingAdapter {

    @BindingAdapter(value = {"isOpenDrawer"}, requireAll = false)
    public static void openDrawer(DrawerLayout drawerLayout, boolean isOpenDrawer) {
        if (isOpenDrawer && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @BindingAdapter(value = {"allowDrawerOpen"}, requireAll = false)
    public static void allowDrawerOpen(DrawerLayout drawerLayout, boolean allowDrawerOpen) {
        drawerLayout.setDrawerLockMode(allowDrawerOpen
                ? DrawerLayout.LOCK_MODE_UNLOCKED
                : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @BindingAdapter(value = {"drawerListener"}, requireAll = false)
    public static void drawerListener(DrawerLayout drawerLayout, DrawerLayout.DrawerListener drawerListener) {
        drawerLayout.addDrawerListener(drawerListener);
    }
}
