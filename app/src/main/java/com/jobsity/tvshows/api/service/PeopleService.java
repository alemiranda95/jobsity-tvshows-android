package com.jobsity.tvshows.api.service;

import com.jobsity.tvshows.api.network.NetworkBuilder;
import com.jobsity.tvshows.domain.model.personcastcredits.PersonCastCreditsDto;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public class PeopleService {
    private interface PeopleApi {
        @GET("/people/{id}/castcredits?embed=show")
        Observable<List<PersonCastCreditsDto>> getPersonShows(
                @Path("id") int id
        );
    }

    private static PeopleService instance;
    private final PeopleService.PeopleApi peopleApi = NetworkBuilder.getInstance(PeopleService.PeopleApi.class);

    public static PeopleService getInstance() {
        if (instance == null) {
            instance = new PeopleService();
        }
        return instance;
    }

    public Observable<List<PersonCastCreditsDto>> getPersonShows(int id) {
        return peopleApi.getPersonShows(id);
    }
}
