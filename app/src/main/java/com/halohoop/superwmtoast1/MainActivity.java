package com.halohoop.superwmtoast1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.halohoop.superwmtoast.SuperWmToast;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private WindowManager mWindowManager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //判断是否有焦点
        if(hasFocus && Build.VERSION.SDK_INT >= 19){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            |View.SYSTEM_UI_FLAG_FULLSCREEN
//                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//            );
            //-------------
//            View decorView = getWindow().getDecorView();
//        //让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
//        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//        decorView.setSystemUiVisibility(option);
//        //设置状态栏颜色为透明
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
            //-------------
////得到当前界面的装饰视图
//            View decorView = getWindow().getDecorView();
////        SYSTEM_UI_FLAG_FULLSCREEN表示全屏的意思，也就是会将状态栏隐藏
//            //设置系统UI元素的可见性
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    public void showError(View view) {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
            return;
        } else {
            textView = new TextView(getApplicationContext());
            textView.setText("Toast Show");
            textView.setTextColor(Color.RED);
            textView.setBackgroundColor(Color.BLACK);
            textView.setOnClickListener(this);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.height = 1280;
            layoutParams.width = 720;
            layoutParams.alpha = 1f;
            layoutParams.x = 0;
            layoutParams.y = 0;
            layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            layoutParams.flags = FLAG_TRANSLUCENT_STATUS | FLAG_NOT_TOUCH_MODAL |FLAG_FULLSCREEN;
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.type = WindowManager.LayoutParams.TYPE_SEARCH_BAR;//2001 error
            layoutParams.type = WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG;//2009 error
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG;//2008 error
            layoutParams.type = WindowManager.LayoutParams.TYPE_INPUT_METHOD;//2011 error
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//2003 behind status can click
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;//2002 behind status can click
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;//2006 behind status can't click
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;//2010 above status can click

            mWindowManager.addView(textView, layoutParams);
            SuperWmToast.makeText(MainActivity.this, "halohoop1").show();
        }
    }

    public void hide(View view) {
        if (textView.isAttachedToWindow()) {
            mWindowManager.removeView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "asd", Toast.LENGTH_SHORT).show();
    }
}
