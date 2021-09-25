package com.jobsity.tvshows.ui.presentation.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.domain.repository.ShowRepository;
import com.jobsity.tvshows.util.LogUtils;
import com.jobsity.tvshows.util.exception.NoInternetException;

import java.util.List;

import rx.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    //Constants
    private final String LOG_TAG = LogUtils.makeLogTag(HomeViewModel.class);

    //Observables
    private MutableLiveData<HomeViewState> mhomeViewState;

    //Variables
    private ShowRepository showRepository;
    private boolean isSyncing;

    public HomeViewModel() {
        mhomeViewState = new MutableLiveData<>(new HomeViewState());
        showRepository = new ShowRepository();
        obtainShows();
    }

    public void obtainShows() {
        if (!isSyncing) {
            HomeViewState homeViewState = mhomeViewState.getValue();
            homeViewState.setShowProgress(true);
            homeViewState.setShowListChanged(false);
            homeViewState.setNothingToShow(false);
            homeViewState.setShowNoInternetError(false);
            homeViewState.setErrorMessage(null);
            mhomeViewState.setValue(homeViewState);

            isSyncing = true;
            showRepository.getShows()
                    .observeOn(Schedulers.io())
                    .subscribe(response -> {
                        isSyncing = false;
                        if (response != null) {
                            showRepository.setEnded(response.isEmpty());
                            loadShowList(response);
                        }
                    }, err -> {
                        LogUtils.e(LOG_TAG, err.getMessage(), err);
                        isSyncing = false;
                        showRepository.getShowsFailed();

                        homeViewState.setShowProgress(false);
                        homeViewState.setShowListChanged(false);
                        homeViewState.setNothingToShow(homeViewState.getShowList().isEmpty());
                        homeViewState.setShowNoInternetError(err instanceof NoInternetException);
                        homeViewState.setErrorMessage(err instanceof NoInternetException ?
                                null : err.getMessage());
                        mhomeViewState.postValue(homeViewState);
                    });
        }
    }

    private void loadShowList(List<Show> showList) {
        HomeViewState homeViewState = mhomeViewState.getValue();
        List<Show> currentShowList = mhomeViewState.getValue().getShowList();
        homeViewState.setPreviousShowListSize(currentShowList.size());
        homeViewState.getShowList().addAll(showList);
        homeViewState.setShowListChanged(!showList.isEmpty());
        homeViewState.setShowProgress(false);
        homeViewState.setShowNoInternetError(false);
        homeViewState.setErrorMessage(null);
        mhomeViewState.postValue(homeViewState);
    }

    //Getters
    public LiveData<HomeViewState> gethomeViewState() {
        return mhomeViewState;
    }

}