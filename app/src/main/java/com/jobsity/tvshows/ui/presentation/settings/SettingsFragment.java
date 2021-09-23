package com.jobsity.tvshows.ui.presentation.settings;

import androidx.biometric.BiometricManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.TvShowApp;
import com.jobsity.tvshows.ui.presentation.main.MainActivity;
import com.jobsity.tvshows.util.constant.AppConstants;
import com.jobsity.tvshows.view.PinEditTextPreference;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    //Activity
    private MainActivity mActivity;

    //Preference Views
    private PinEditTextPreference pinEditTextPreference;
    private SwitchPreferenceCompat usePinSwitchPreference;
    private SwitchPreferenceCompat useFingerprintSwitchPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        mActivity = (MainActivity) getContext();

        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
        ListPreference themeListPreference = findPreference(getString(R.string.select_app_theme_key));
        themeListPreference.setOnPreferenceChangeListener(this);

        usePinSwitchPreference = findPreference(getString(R.string.use_pin_key));
        usePinSwitchPreference.setOnPreferenceChangeListener(this);

        pinEditTextPreference = findPreference(getString(R.string.pin_key));
        pinEditTextPreference.setOnBindEditTextListener(editText -> {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(AppConstants.PIN_MAX_DDIGITS)});
            editText.setSelection(editText.getText().length());
        });
        pinEditTextPreference.setOnPreferenceChangeListener(this);

        useFingerprintSwitchPreference = findPreference(getString(R.string.use_fingerprint_key));
        useFingerprintSwitchPreference.setVisible(showUseFingerprint());
    }

    private boolean showUseFingerprint() {
        BiometricManager biometricManager = BiometricManager.from(getContext());
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                return false;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(getString(R.string.select_app_theme_key))) {
            switch (Integer.parseInt((String) newValue)) {
                case 0:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case 1:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case 2:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
            }
        } else if (preference.getKey().equals(getString(R.string.use_pin_key))) {
            if ((Boolean) newValue) {
                pinEditTextPreference.show();
            }
        } else if (preference.getKey().equals(getString(R.string.pin_key))) {
            if (AppConstants.EMPTY_STRING.equals((String) newValue)) {
                usePinSwitchPreference.setChecked(false);
            } else if (((String) newValue).length() < 4) {
                mActivity.showToast(TvShowApp.getRes().getString(R.string.pin_minimum_digits));
                pinEditTextPreference.callChangeListener(AppConstants.EMPTY_STRING);
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (TextUtils.isEmpty(pinEditTextPreference.getText())) {
            usePinSwitchPreference.setChecked(false);
            useFingerprintSwitchPreference.setChecked(false);
        }
    }
}