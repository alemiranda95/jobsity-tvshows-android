package com.jobsity.tvshows.domain.repository;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.TvShowApp;

public class SecurityRepository {

    private final TvShowApp app = TvShowApp.getInstance();
    private final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (app);

    public SecurityRepository() {
        //Constructor
    }

    public boolean isUsePin() {
        return prefs.getBoolean(app.getString(R.string.use_pin_key), false);
    }

    public boolean isPinValid(String pin) {
        String savedPin = prefs.getString(app.getString(R.string.pin_key), null);
        return pin.equals(savedPin);
    }

    public boolean isUseFingerPrint() {
        return prefs.getBoolean(app.getString(R.string.use_fingerprint_key), false);
    }
}
