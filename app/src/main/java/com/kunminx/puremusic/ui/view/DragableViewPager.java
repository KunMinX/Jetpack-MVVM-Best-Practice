package com.kunminx.puremusic.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

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
    private boolean mInitCoordinate;

    public DragableViewPager(@NonNull Context context) {
        super(context);
    }

    public DragableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
                if (!mInitCoordinate) {
                    mLastX = x;
                    mLastY = y;
                    mInitCoordinate = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                float dx = x - mLastX;
                float dy = y - mLastY;
                int orientation = getOrientation(dx, dy);

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
                mInitCoordinate = false;
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
