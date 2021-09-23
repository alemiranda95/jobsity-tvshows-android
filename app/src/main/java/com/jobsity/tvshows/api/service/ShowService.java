package com.jobsity.tvshows.api.service;

import com.jobsity.tvshows.api.network.NetworkBuilder;
import com.jobsity.tvshows.domain.model.episode.EpisodeDto;
import com.jobsity.tvshows.domain.model.show.ShowDto;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public class ShowService {

    private interface ShowApi {
        @GET("/shows")
        Observable<List<ShowDto>> getShows(
                @Query("page") int page
        );

        @GET("/shows/{id}/episodes")
        Observable<List<EpisodeDto>> getEpisodes(
                @Path("id") int id
        );
    }

    private static ShowService instance;
    private final ShowApi showApi = NetworkBuilder.getInstance(ShowApi.class);

    public static ShowService getInstance() {
        if (instance == null) {
            instance = new ShowService();
        }
        return instance;
    }

    public Observable<List<ShowDto>> getShows(int page) {
        return showApi.getShows(page);
    }

    public Observable<List<EpisodeDto>> getEpisodes(int id) {
        return showApi.getEpisodes(id);
    }
}
