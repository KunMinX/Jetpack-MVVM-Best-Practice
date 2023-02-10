package com.kunminx.puremusic.ui.bind;

import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kunminx.architecture.ui.adapter.CommonViewPagerAdapter;
import com.kunminx.puremusic.R;

/**
 * Create by KunMinX at 2020/3/13
 */
public class TabPageBindingAdapter {

    @BindingAdapter(value = {"initTabAndPage"}, requireAll = false)
    public static void initTabAndPage(ViewPager viewPager, boolean initTabAndPage) {
        TabLayout tabLayout = (viewPager.getRootView()).findViewById(R.id.tab_layout);
        int count = tabLayout.getTabCount();
        String[] title = new String[count];
        for (int i = 0; i < count; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.getText() != null) {
                title[i] = tab.getText().toString();
            }
        }
        viewPager.setAdapter(new CommonViewPagerAdapter(false, title));
        tabLayout.setupWithViewPager(viewPager);
    }

    @BindingAdapter(value = {"tabSelectedListener"}, requireAll = false)
    public static void tabSelectedListener(TabLayout tabLayout, TabLayout.OnTabSelectedListener listener) {
        tabLayout.addOnTabSelectedListener(listener);
    }

}
