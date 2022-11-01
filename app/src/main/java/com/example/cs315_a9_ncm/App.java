package com.example.cs315_a9_ncm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class App extends Application
{
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
