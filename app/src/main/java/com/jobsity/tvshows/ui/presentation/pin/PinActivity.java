package com.jobsity.tvshows.ui.presentation.pin;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.databinding.ActivityPinBinding;
import com.jobsity.tvshows.ui.presentation.BaseActivity;

import java.util.concurrent.Executor;

public class PinActivity extends BaseActivity {

    //Binding
    private ActivityPinBinding mBinding;

    //ViewModel
    private PinViewModel mPinViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityPinBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setViewModel();
        setObservers();
        setListeners();

        mBinding.setPinVM(mPinViewModel);
    }

    private void setListeners() {
        mBinding.buttonEnter.setOnClickListener(v ->
                mPinViewModel.validatePind()
        );

        mBinding.imageviewFingerprint.setOnClickListener(v ->
                showBiometricsPrompt()
        );
    }

    private void setViewModel() {
        mPinViewModel = new ViewModelProvider(this).get(PinViewModel.class);
    }

    private void setObservers() {
        mPinViewModel.getPinViewState().observe(this, pinViewState -> {
            if (pinViewState.isUseFingerprint()) {
                showBiometricsPrompt();
                mBinding.imageviewFingerprint.setVisibility(View.VISIBLE);
            } else {
                focusPinEditText();
            }

            if (pinViewState.isValidPin()) {
                finish();
            }

            if (pinViewState.getErrorMessage() != null) {
                showErrorSnackbar(pinViewState.getErrorMessage());
            }
        });
    }

    private void showBiometricsPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                    errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                    showErrorSnackbar(errString.toString());
                }
                focusPinEditText();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometrict_prompt_title))
                .setNegativeButtonText(getString(R.string.biometrict_prompt_cancel))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void focusPinEditText() {
        mBinding.edittextPin.requestFocus();
        showSoftKeyboard();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}