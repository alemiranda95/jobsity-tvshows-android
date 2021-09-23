package com.jobsity.tvshows.ui.presentation.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.domain.repository.ShowRepository;

import java.util.List;

public class FavoriteViewModel extends ViewModel {

    //Observables
    private MutableLiveData<FavoriteViewState> mFavoriteViewState;

    //Variables
    private ShowRepository showRepository;

    public FavoriteViewModel() {
        mFavoriteViewState = new MutableLiveData<>(new FavoriteViewState());
        showRepository = new ShowRepository();
        obtainShows();
    }

    public void obtainShows() {
        List<Show> showList = showRepository.getFavoriteShows();

        FavoriteViewState favoriteViewState = mFavoriteViewState.getValue();
        favoriteViewState.getShowList().clear();
        favoriteViewState.getShowList().addAll(showList);
        favoriteViewState.setShowListChanged(true);
        favoriteViewState.setNothingToShow(showList.isEmpty());
        mFavoriteViewState.setValue(favoriteViewState);
    }

    //Getters
    public LiveData<FavoriteViewState> getFavoriteViewState() {
        return mFavoriteViewState;
    }

}