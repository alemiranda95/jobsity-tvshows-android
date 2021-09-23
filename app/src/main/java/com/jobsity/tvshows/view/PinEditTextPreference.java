package com.jobsity.tvshows.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;

import com.jobsity.tvshows.util.constant.AppConstants;


public class PinEditTextPreference extends EditTextPreference {

    public PinEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void show() {
        getPreferenceManager().showDialog(this);
    }

}
