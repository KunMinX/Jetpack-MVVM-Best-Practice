package com.kunminx.puremusic.ui.base.binding;

import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kunminx.architecture.ui.adapter.CommonViewPagerAdapter;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.ui.view.DragableViewPager;

/**
 * Create by KunMinX at 2020/3/13
 */
public class TabPageBinding {

    @BindingAdapter(value = {"initTabAndPage"}, requireAll = false)
    public static void initTabAndPage(TabLayout tabLayout, boolean initTabAndPage) {
        if (initTabAndPage) {
            int count = tabLayout.getTabCount();
            String[] title = new String[count];
            for (int i = 0; i < count; i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null && tab.getText() != null) {
                    title[i] = tab.getText().toString();
                }
            }
            ViewPager viewPager = (tabLayout.getRootView()).findViewById(R.id.view_pager);
            if (viewPager != null) {
                viewPager.setAdapter(new CommonViewPagerAdapter(count, false, title));
                tabLayout.setupWithViewPager(viewPager);
            }
        }
    }

    @BindingAdapter(value = {"tabSelectedListener"}, requireAll = false)
    public static void tabSelectedListener(TabLayout tabLayout, TabLayout.OnTabSelectedListener listener) {
        tabLayout.addOnTabSelectedListener(listener);
    }

    @BindingAdapter(value = {"onDragRightListener"}, requireAll = false)
    public static void onDragRightListener(DragableViewPager viewPager, DragableViewPager.OnDragRightListener listener) {
        viewPager.setOnDragRightListener(listener);
    }

}
