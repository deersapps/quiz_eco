package com.jkdeers.activitygame;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import in.myinnos.customfontlibrary.TypefaceUtil;

public class activitygame extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/lato.ttf");
    }
}
