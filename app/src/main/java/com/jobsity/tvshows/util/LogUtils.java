package com.jobsity.tvshows.util;

import android.util.Log;

import com.jobsity.tvshows.BuildConfig;

public class LogUtils {

    private static final String LOG_PREFIX = "TVShow_";

    private LogUtils() {}

    public static String makeLogTag(Class c) {
        return LOG_PREFIX + c.getSimpleName();
    }

    public static void v(String tag, final String msg) {
        Log.v(tag, msg);
    }

    public static void v(String tag, final String msg, Throwable tr) {
        Log.v(tag, msg, tr);
    }

    public static void d(String tag, final String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, final String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, final String msg) {
        Log.i(tag, msg);
    }

    public static void i(String tag, final String msg, final String details) {
        Log.i(tag, msg);
    }

    public static void w(String tag, final String msg) {
        Log.w(tag, msg);
    }

    public static void w(String tag, final String msg, Throwable tr) {
        Log.w(tag, msg, tr);
    }

    public static void e(String tag, final String msg) {
        Log.e(tag, msg);
    }

    public static void e(String tag, final String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
    }

}
