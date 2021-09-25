package com.jobsity.tvshows.ui.presentation.showinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobsity.tvshows.domain.model.episode.Episode;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.domain.model.show.ShowDescription;
import com.jobsity.tvshows.domain.repository.ShowRepository;
import com.jobsity.tvshows.util.LogUtils;
import com.jobsity.tvshows.util.exception.NoInternetException;

import java.util.ArrayList;
import java.util.List;

import rx.schedulers.Schedulers;

public class ShowInfoViewModel extends ViewModel {

    //Constants
    private final String LOG_TAG = LogUtils.makeLogTag(ShowInfoViewModel.class);

    //Bindings
    public MutableLiveData<ShowDescription> mShowInfo;

    //Observables
    private MutableLiveData<ShowInfoViewState> mShowInfoViewState;

    //Variables
    private Show mShow;
    private ShowRepository showRepository;
    private List<Episode> mEpisodeList;

    public ShowInfoViewModel(Show show) {
        mShow = show;
        mEpisodeList = new ArrayList<>();
        showRepository = new ShowRepository();
        mShowInfo = new MutableLiveData<>(showRepository.getShowDescription(show));
        ShowInfoViewState showInfoViewState = new ShowInfoViewState();
        showInfoViewState.setNothingToShow(false);
        mShowInfoViewState = new MutableLiveData<>(showInfoViewState);
        checkIfFavorite();
        obtainEpisodes();
    }

    private void checkIfFavorite() {
        boolean isFavorite = showRepository.isFavorite(mShow.getId());

        ShowInfoViewState showInfoViewState = mShowInfoViewState.getValue();
        showInfoViewState.setFavorite(isFavorite);
        showInfoViewState.setEpisodeListChanged(false);
        mShowInfoViewState.setValue(showInfoViewState);
    }

    public void obtainEpisodes() {
        if (mEpisodeList.isEmpty()) {
            ShowInfoViewState showInfoViewState = mShowInfoViewState.getValue();
            showInfoViewState.setShowProgress(true);
            showInfoViewState.setEpisodeListChanged(false);
            showInfoViewState.setShowNoInternetError(false);
            showInfoViewState.setErrorMessage(null);
            mShowInfoViewState.setValue(showInfoViewState);

            showRepository.getEpisodes(mShow.getId())
                    .observeOn(Schedulers.io())
                    .subscribe(response -> {
                        if (response != null) {
                            loadSeassons(response);
                        }
                    }, err -> {
                        LogUtils.e(LOG_TAG, err.getMessage(), err);

                        showInfoViewState.setShowProgress(false);
                        showInfoViewState.setEpisodeListChanged(false);
                        showInfoViewState.setShowNoInternetError(err instanceof NoInternetException);
                        showInfoViewState.setErrorMessage(err instanceof NoInternetException ?
                                null : err.getMessage());
                        mShowInfoViewState.postValue(showInfoViewState);
                    });
        }
    }

    private void loadSeassons(List<Episode> episodeList) {
        ShowInfoViewState showInfoViewState = mShowInfoViewState.getValue();
        if (!episodeList.isEmpty()) {
            mEpisodeList = episodeList;
            int lastSeasson = episodeList.get(episodeList.size()-1).getSeason();
            showInfoViewState.setLastSeasson(lastSeasson);
            showInfoViewState.setSeassonsSetted(true);
        }
        showInfoViewState.setShowProgress(false);
        showInfoViewState.setShowNoInternetError(false);
        showInfoViewState.setErrorMessage(null);
        mShowInfoViewState.postValue(showInfoViewState);
    }

    public void loadEpisodeList(int seasson) {
        ShowInfoViewState showInfoViewState = mShowInfoViewState.getValue();
        showInfoViewState.setSeassonsSetted(false);
        showInfoViewState.getEpisodeList().clear();
        showInfoViewState.getEpisodeList().addAll(
                showRepository.getEpisodesBySeasson(mEpisodeList, seasson)
        );
        showInfoViewState.setEpisodeListChanged(true);
        mShowInfoViewState.setValue(showInfoViewState);
    }

    public void setFavorite() {
        ShowInfoViewState showInfoViewState = mShowInfoViewState.getValue();
        showInfoViewState.setFavorite(!showInfoViewState.isFavorite());
        showInfoViewState.setEpisodeListChanged(false);
        if (showInfoViewState.isFavorite()) {
            showRepository.insertFavoriteShow(mShow);
        } else {
            showRepository.removeFavoriteShow(mShow.getId());
        }
        mShowInfoViewState.setValue(showInfoViewState);
    }

    //Getters
    public LiveData<ShowInfoViewState> getShowInfoViewState() {
        return mShowInfoViewState;
    }

}