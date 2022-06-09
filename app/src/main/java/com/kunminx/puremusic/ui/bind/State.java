package com.kunminx.puremusic.ui.bind;

import androidx.databinding.ObservableField;

/**
 * Create by KunMinX at 2022/5/30
 */
public class State<T> extends ObservableField<T> {

    private final boolean mIsDebouncing;

    public State() {
        this(null);
    }

    public State(T value) {
        this(value, false);
    }

    public State(T value, boolean isDebouncing) {
        super(value);
        mIsDebouncing = isDebouncing;
    }

    @Override
    public void set(T value) {
        boolean isUnChanged = get() == value;
        super.set(value);
        if (!mIsDebouncing && isUnChanged) {
            notifyChange();
        }
    }
}
