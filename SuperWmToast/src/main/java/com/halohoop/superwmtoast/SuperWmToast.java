/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * SuperWmToast.java
 *
 * Super Toast which is show on top of every View;
 *
 * Author huanghaiqi, Created at 2016-10-21
 *
 * Ver 1.0, 2016-10-21, huanghaiqi, Create file.
 */

package com.halohoop.superwmtoast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class SuperWmToast {
    private final static double DEFAULT_TIME = 2;
    private final static int DEFAULT_MARGIN_BOTTOM = 100;
    private double mTime = DEFAULT_TIME;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private View mView;
    private Timer mTimer;
    private Context mContext;

    private SuperWmToast(Context context, String text) {
        this.mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mTimer = new Timer();

        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mView = toast.getView();
        Drawable background = mView.getBackground();
        ColorFilter colorFilter = background.getColorFilter();
        Log.i(TAG, "SuperWmToast: "+colorFilter);

        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.alpha = 0f;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        mParams.windowAnimations = toast.getView().getAnimation().INFINITE;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        mParams.y = DEFAULT_MARGIN_BOTTOM;
    }

    public static SuperWmToast makeText(Context context, String text) {
        SuperWmToast toastCustom = new SuperWmToast(context, text);
        return toastCustom;
    }

    public SuperWmToast setTime(int seconds) {
        this.mTime = seconds;
        return this;
    }

    public SuperWmToast setMarginBottom(int marginBottom) {
        this.mParams.y = marginBottom;
        return this;
    }

    public void show() {
        if (!Settings.canDrawOverlays(mContext)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            mContext.startActivity(intent);
            return;
        } else {
            animationToShow();
        }
    }

    private void animationToShow() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mView.isAttachedToWindow()) {
                    float value = (float) animation.getAnimatedValue();
                    mParams.alpha = value;
                    mWindowManager.updateViewLayout(mView, mParams);
                }
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mWindowManager.addView(mView, mParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mView.post(new Runnable() {
                            @Override
                            public void run() {
                                animationToHide();
                            }
                        });
                    }
                }, (long) (mTime * 1000));
            }
        });
        valueAnimator.start();
    }

    private void animationToHide() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mParams.alpha = value;
                mWindowManager.updateViewLayout(mView, mParams);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTimer.cancel();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mWindowManager.removeView(mView);
            }
        });
        valueAnimator.start();
    }

    public void cancel() {
        animationToHide();
    }

}
