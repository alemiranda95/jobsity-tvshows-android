package com.jobsity.tvshows.api.service;

import com.jobsity.tvshows.api.network.NetworkBuilder;
import com.jobsity.tvshows.domain.model.search.SearchPersonDto;
import com.jobsity.tvshows.domain.model.search.SearchShowDto;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class SearchService {
    private interface SearchApi {
        @GET("/search/shows")
        Observable<List<SearchShowDto>> searchShows(
                @Query("q") String search
        );

        @GET("/search/people")
        Observable<List<SearchPersonDto>> searchPeople(
                @Query("q") String search
        );
    }

    private static SearchService instance;
    private final SearchService.SearchApi searchApi = NetworkBuilder.getInstance(SearchService.SearchApi.class);

    public static SearchService getInstance() {
        if (instance == null) {
            instance = new SearchService();
        }
        return instance;
    }

    public Observable<List<SearchShowDto>> getShows(String search) {
        return searchApi.searchShows(search);
    }

    public Observable<List<SearchPersonDto>> getPeople(String search) {
        return searchApi.searchPeople(search);
    }
}
