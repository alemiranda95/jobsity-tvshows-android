package com.jobsity.tvshows.util;

import android.text.Html;
import android.util.TypedValue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jobsity.tvshows.TvShowApp;
import com.jobsity.tvshows.util.constant.AppConstants;

import java.lang.reflect.Type;
import java.util.List;

public class FormatUtils {

    private FormatUtils() {}

    public static String getYearFromDate(String date) {
        if (date != null && date.contains("-")) {
            String[] split = date.split("-");
            if (split.length > 0) {
                return split[0];
            }
        }
        return AppConstants.EMPTY_STRING;
    }

    public static String formatHtml(String text) {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString();
    }

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                TvShowApp.getRes().getDisplayMetrics()
        );
    }

    public static String stringlistToString(List<String> stringList) {
        Gson gson = new Gson();
        return gson.toJson(stringList);
    }

    public static List<String> stringToStringList(String string) {
        Type type = new TypeToken<List<String>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(string, type);
    }
}
