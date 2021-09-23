package com.jobsity.tvshows;

import android.app.Application;
import android.content.res.Resources;

import com.jobsity.tvshows.util.DBHelper;

public class TvShowApp extends Application {

    private static TvShowApp instance;
    public static Resources resources;

    public static TvShowApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = TvShowApp.this;
        resources = getResources();

        DBHelper.getInstance();
    }

    //Getters
    public static Resources getRes() {
        return resources;
    }
}
