package com.wlcookies.fundemo.ui.bluetooth;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.R;

public class UnableDragSeekBar extends AppCompatSeekBar {

    private boolean touch = true;

    public UnableDragSeekBar(@NonNull Context context) {
        this(context, null);
    }

    public UnableDragSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarStyle);
    }

    public UnableDragSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTouch(boolean touch) {
        this.touch = touch;
    }

    /**
     * onTouchEvent 是在 SeekBar 继承的抽象类 AbsSeekBar
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touch) {
            return super.onTouchEvent(event);
        }
        return false;
    }
}
