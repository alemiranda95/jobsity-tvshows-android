package com.jobsity.tvshows.ui.presentation.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobsity.tvshows.domain.model.person.Person;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.domain.repository.PersonRepository;
import com.jobsity.tvshows.domain.repository.ShowRepository;
import com.jobsity.tvshows.util.LogUtils;
import com.jobsity.tvshows.util.exception.NoInternetException;

import java.util.List;

import rx.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    //Constants
    private final String LOG_TAG = LogUtils.makeLogTag(SearchViewModel.class);

    //Observables
    private MutableLiveData<SearchViewState> mSearchViewState;

    //Variables
    private ShowRepository showRepository;
    private PersonRepository personRepository;

    public SearchViewModel() {
        mSearchViewState = new MutableLiveData<>(new SearchViewState());
        showRepository = new ShowRepository();
        personRepository = new PersonRepository();
    }

    public void obtainShowsAndPeople(String query) {
        SearchViewState searchViewState = mSearchViewState.getValue();
        searchViewState.setShowProgress(true);
        searchViewState.setShowListChanged(false);
        searchViewState.setPersonListChanged(false);
        searchViewState.setNothingToShow(false);
        searchViewState.setShowNoInternetError(false);
        searchViewState.setErrorMessage(null);
        mSearchViewState.postValue(searchViewState);

        showRepository.searchShow(query)
                .observeOn(Schedulers.io())
                .subscribe(response -> {
                    if (response != null) {
                        loadShowList(response);
                    }
                }, this::manageError);

        personRepository.searchPeople(query)
                .observeOn(Schedulers.io())
                .subscribe(response -> {
                    if (response != null) {
                        loadPersonList(response);
                    }
                }, this::manageError);
    }

    private void manageError(Throwable err) {
        LogUtils.e(LOG_TAG, err.getMessage(), err);

        SearchViewState searchViewState = mSearchViewState.getValue();
        searchViewState.setShowProgress(false);
        searchViewState.setShowListChanged(false);
        searchViewState.setNothingToShow(searchViewState.getShowList().isEmpty() &&
                searchViewState.getPersonList().isEmpty());
        searchViewState.setShowNoInternetError(err instanceof NoInternetException);
        searchViewState.setErrorMessage(err instanceof NoInternetException ?
                null : err.getMessage());
        mSearchViewState.postValue(searchViewState);
    }

    private void loadPersonList(List<Person> personList) {
        SearchViewState searchViewState = mSearchViewState.getValue();
        searchViewState.getPersonList().addAll(personList);
        searchViewState.setPersonListChanged(true);
        searchViewState.setShowListChanged(false);
        searchViewState.setShowProgress(false);
        searchViewState.setShowNoInternetError(false);
        searchViewState.setErrorMessage(null);
        mSearchViewState.postValue(searchViewState);
    }

    private void loadShowList(List<Show> showList) {
        SearchViewState searchViewState = mSearchViewState.getValue();
        searchViewState.getShowList().addAll(showList);
        searchViewState.setShowListChanged(true);
        searchViewState.setPersonListChanged(false);
        searchViewState.setShowProgress(false);
        searchViewState.setShowNoInternetError(false);
        searchViewState.setErrorMessage(null);
        mSearchViewState.postValue(searchViewState);
    }

    //Getters
    public LiveData<SearchViewState> getSearchViewState() {
        return mSearchViewState;
    }

}