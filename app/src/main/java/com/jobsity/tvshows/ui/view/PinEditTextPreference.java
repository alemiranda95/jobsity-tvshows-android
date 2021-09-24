package com.jobsity.tvshows.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;


public class PinEditTextPreference extends EditTextPreference {

    public PinEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void show() {
        getPreferenceManager().showDialog(this);
    }

}
