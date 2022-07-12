package com.wlcookies.fundemo.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.wlcookies.fundemo.R;

public class CameraImageView extends AppCompatImageView {

    private Paint mPaint;
    private Camera mCamera;
    private Bitmap mBitmap;
    private int mProgress;
    private Matrix mMatrix = new Matrix();

    public CameraImageView(@NonNull Context context) {
        this(context, null);
    }

    public CameraImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();
    }

    private void init() {
        mCamera = new Camera();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCamera.save();
        canvas.save();

        mCamera.rotateY(mProgress);
        mCamera.applyToCanvas(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        mCamera.restore();
        super.onDraw(canvas);
        canvas.restore();
    }
}
