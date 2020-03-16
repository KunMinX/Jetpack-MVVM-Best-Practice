package com.kunminx.puremusic.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Create by KunMinX at 20/3/16
 */
public class DragBackConstraintLayout extends ConstraintLayout {

    private float mLastX;
    private float mLastY;
    private OnDragBackListener mOnDragBackListener;
    private boolean mInitCoordinate;

    public DragBackConstraintLayout(Context context) {
        super(context);
    }

    public DragBackConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragBackConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                return true;
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
                        if (mOnDragBackListener != null) {
                            mOnDragBackListener.onDragBack();
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

    public void setOnDragBackListener(OnDragBackListener onDragBackListener) {
        mOnDragBackListener = onDragBackListener;
    }

    public interface OnDragBackListener {
        void onDragBack();
    }
}
