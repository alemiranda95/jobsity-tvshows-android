package com.jobsity.tvshows.domain.repository;

import com.jobsity.tvshows.api.service.SearchService;
import com.jobsity.tvshows.domain.model.person.Person;
import com.jobsity.tvshows.domain.model.person.PersonMapper;

import java.util.List;

import rx.Observable;

public class PersonRepository {
    private final SearchService mSearchService = SearchService.getInstance();

    public PersonRepository() {
        //COnstructor
    }

    public Observable<List<Person>> searchPeople(String search) {
        return mSearchService.getPeople(search).map(PersonMapper::mapFromSearchPeopleDto);
    }

}
