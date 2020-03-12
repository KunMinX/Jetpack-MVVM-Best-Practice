package com.kunminx.puremusic.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Create by KunMinX at 20/3/12
 */
public class DragableViewPager extends ViewPager {

    private float mLastX;
    private float mLastY;
    private onDragRightListener mListener;
    private int mTouchSlop;

    public DragableViewPager(@NonNull Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public DragableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (Math.abs(dx) > mTouchSlop && Math.abs(dy) > mTouchSlop) {
                    int orientation = getOrientation(dx, dy);
                    Log.d("TAG", "dx:" + dx + " dy:" + dy);
                    Log.d("TAG", String.valueOf(orientation));
                    switch (orientation) {
                        case 'r':
                            if (mListener != null) {
                                mListener.onDragRight();
                            }
                            break;
                        case 'l':
                        case 't':
                        case 'b':
                            break;
                    }
                }
                break;
            default:

        }

        return super.onTouchEvent(event);
    }

    private int getOrientation(float dx, float dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? 'r' : 'l';
        } else {
            return dy > 0 ? 'b' : 't';
        }
    }

    public void setListener(onDragRightListener listener) {
        mListener = listener;
    }

    public interface onDragRightListener {
        void onDragRight();
    }
}
