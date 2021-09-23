package com.jobsity.tvshows.ui.presentation.personinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jobsity.tvshows.domain.model.person.Person;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.domain.repository.ShowRepository;
import com.jobsity.tvshows.util.LogUtils;
import com.jobsity.tvshows.util.exception.NoInternetException;

import java.util.List;

import rx.schedulers.Schedulers;

public class PersonInfoViewModel extends ViewModel {

    //Constants
    private final String LOG_TAG = LogUtils.makeLogTag(PersonInfoViewModel.class);

    //Observables
    private MutableLiveData<PersonInfoViewState> mPersonInfoViewState;

    //Variables
    private Person mPerson;
    private ShowRepository showRepository;

    public PersonInfoViewModel(Person person) {
        mPerson = person;
        showRepository = new ShowRepository();
        PersonInfoViewState personInfoViewState = new PersonInfoViewState();
        personInfoViewState.setNothingToShow(false);
        mPersonInfoViewState = new MutableLiveData<>(personInfoViewState);

        obtainPersonShows();
    }

    public void obtainPersonShows() {
        PersonInfoViewState personInfoViewState = mPersonInfoViewState.getValue();
        if (personInfoViewState.getPersonShowList().isEmpty()) {
            personInfoViewState.setShowProgress(true);
            personInfoViewState.setPersonShowListChanged(false);
            personInfoViewState.setShowNoInternetError(false);
            personInfoViewState.setErrorMessage(null);
            mPersonInfoViewState.setValue(personInfoViewState);

            showRepository.getPersonShows(mPerson.getId())
                    .observeOn(Schedulers.io())
                    .subscribe(response -> {
                        if (response != null) {
                            loadPersonShowList(response);
                        }
                    }, err -> {
                        LogUtils.e(LOG_TAG, err.getMessage(), err);
                        if (err instanceof NoInternetException) {
                            personInfoViewState.setShowProgress(false);
                            personInfoViewState.setPersonShowListChanged(false);
                            personInfoViewState.setShowNoInternetError(true);
                            personInfoViewState.setErrorMessage(null);
                            mPersonInfoViewState.postValue(personInfoViewState);
                        } else {
                            personInfoViewState.setShowProgress(false);
                            personInfoViewState.setPersonShowListChanged(false);
                            personInfoViewState.setShowNoInternetError(false);
                            personInfoViewState.setErrorMessage(err.getMessage());
                            mPersonInfoViewState.postValue(personInfoViewState);
                        }
                    });
        }
    }

    private void loadPersonShowList(List<Show> showList) {
        PersonInfoViewState personInfoViewState = mPersonInfoViewState.getValue();
        personInfoViewState.getPersonShowList().clear();
        personInfoViewState.getPersonShowList().addAll(showList);
        personInfoViewState.setPersonShowListChanged(true);
        personInfoViewState.setShowProgress(false);
        personInfoViewState.setShowNoInternetError(false);
        personInfoViewState.setErrorMessage(null);
        mPersonInfoViewState.postValue(personInfoViewState);
    }

    //Getters
    public LiveData<PersonInfoViewState> getPersonInfoViewState() {
        return mPersonInfoViewState;
    }

}