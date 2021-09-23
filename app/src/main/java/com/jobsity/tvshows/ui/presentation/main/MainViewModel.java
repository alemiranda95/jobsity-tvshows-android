package com.jobsity.tvshows.ui.presentation.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobsity.tvshows.domain.repository.SecurityRepository;

public class MainViewModel extends ViewModel {

    //Observers
    private MutableLiveData<MainViewState> mMainViewState;

    //Variables
    private SecurityRepository securityRepository;

    public MainViewModel() {
        securityRepository = new SecurityRepository();
        MainViewState mainViewState = new MainViewState();
        mainViewState.setUsePin(securityRepository.isUsePin());
        mMainViewState = new MutableLiveData<>(mainViewState);
    }

    //Getters
    public LiveData<MainViewState> getMainViewState() {
        return mMainViewState;
    }
}
