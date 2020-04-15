package com.kunminx.puremusic.ui.base.binding;

import androidx.databinding.BindingAdapter;

import com.kunminx.puremusic.ui.view.PlayPauseView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

/**
 * Create by KunMinX at 2020/3/13
 */
public class IconBindingAdapter {

    @BindingAdapter(value = {"isPlaying"}, requireAll = false)
    public static void isPlaying(PlayPauseView pauseView, boolean isPlaying) {
        if (isPlaying) {
            pauseView.play();
        } else {
            pauseView.pause();
        }
    }

    @BindingAdapter(value = {"mdIcon"}, requireAll = false)
    public static void setIcon(MaterialIconView view, MaterialDrawableBuilder.IconValue iconValue) {
        view.setIcon(iconValue);
    }
}
