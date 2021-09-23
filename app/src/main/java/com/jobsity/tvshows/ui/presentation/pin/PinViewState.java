package com.jobsity.tvshows.ui.presentation.pin;

import com.jobsity.tvshows.ui.presentation.ViewState;

import lombok.Data;

@Data
public class PinViewState extends ViewState {
    private boolean useFingerprint = false;
    private boolean validPin = false;
}
