package com.jobsity.tvshows.ui.presentation;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.jobsity.tvshows.R;

public class BaseActivity extends AppCompatActivity {

    public void showSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0,0);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void showNoInternetSnackbar(View.OnClickListener onTryAgainListener) {
        Snackbar snackbar = createSnackbar(getString(R.string.no_internet_error));
        snackbar.setAction(R.string.no_internet_snackbar_action_text, onTryAgainListener);
        snackbar.show();
    }

    public void showErrorSnackbar(String error) {
        createSnackbar(error).show();
    }

    private Snackbar createSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                text, BaseTransientBottomBar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.darkred));
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(3);
        return snackbar;
    }
}
