package com.halohoop.superwmtoast_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.halohoop.superwmtoast.SuperWmToast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                SuperWmToast.makeText(MainActivity.this, "hahaha").show();
            }
        },1000);
    }
}
