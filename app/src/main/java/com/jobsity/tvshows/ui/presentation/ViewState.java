package com.jobsity.tvshows.ui.presentation;

import lombok.Data;

@Data
public class ViewState {
    private boolean nothingToShow = true;
    private boolean showProgress = false;
    private String errorMessage = null;
    private boolean showNoInternetError = false;
}
