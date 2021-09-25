package com.jobsity.tvshows.ui.presentation.pin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.TvShowApp;
import com.jobsity.tvshows.domain.repository.SecurityRepository;
import com.jobsity.tvshows.util.constant.AppConstants;

public class PinViewModel extends ViewModel {

    //Binding
    public MutableLiveData<String> mPin;

    //Observers
    private MutableLiveData<PinViewState> mPinViewState;

    //Variables
    private SecurityRepository mSecurityRepository;

    public PinViewModel() {
        mSecurityRepository = new SecurityRepository();
        mPin = new MutableLiveData<>(AppConstants.EMPTY_STRING);
        PinViewState pinViewState = new PinViewState();
        pinViewState.setUseFingerprint(mSecurityRepository.isUseFingerPrint());
        mPinViewState = new MutableLiveData<>(pinViewState);
    }

    public void validatePind() {
        boolean isValid = mSecurityRepository.isPinValid(mPin.getValue());

        PinViewState pinViewState = mPinViewState.getValue();
        pinViewState.setUseFingerprint(false);
        pinViewState.setValidPin(isValid);
        pinViewState.setErrorMessage(isValid ?
                null : TvShowApp.getRes().getString(R.string.invalid_pin));
        mPinViewState.setValue(pinViewState);
    }

    //Getters
    public LiveData<PinViewState> getPinViewState() {
        return mPinViewState;
    }
}
