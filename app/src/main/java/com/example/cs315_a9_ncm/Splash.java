package com.example.cs315_a9_ncm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity
{
    Handler mHandler;
    Runnable mRunnable;

//    ModelsJunk modelJ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = () ->
        {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        };

        mHandler.postDelayed(mRunnable,3000);
    }
}