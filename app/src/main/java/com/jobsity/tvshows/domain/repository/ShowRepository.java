package com.jobsity.tvshows.domain.repository;

import com.jobsity.tvshows.api.service.PeopleService;
import com.jobsity.tvshows.api.service.SearchService;
import com.jobsity.tvshows.api.service.ShowService;
import com.jobsity.tvshows.domain.model.episode.Episode;
import com.jobsity.tvshows.domain.model.episode.EpisodeMapper;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.domain.model.show.ShowDescription;
import com.jobsity.tvshows.domain.model.show.ShowMapper;
import com.jobsity.tvshows.util.DBHelper;

import java.util.List;
import java.util.stream.Collectors;

import rx.Observable;

public class ShowRepository {

    private final ShowService mShowService = ShowService.getInstance();
    private final SearchService mSearchService = SearchService.getInstance();
    private final PeopleService mPeopleService = PeopleService.getInstance();
    private final DBHelper mDBHelper = DBHelper.getInstance();

    private int mPage;
    private boolean mEnded;

    public ShowRepository() {
        mPage = 1;
        mEnded = false;
    }

    public Observable<List<Show>> getShows() {
        if (!mEnded) {
            return mShowService.getShows(mPage++).map(ShowMapper::mapFromShowDto);
        }

        return Observable.just(null);
    }

    public Observable<List<Episode>> getEpisodes(int id) {
        return mShowService.getEpisodes(id).map(EpisodeMapper::map);
    }

    public Observable<List<Show>> searchShow(String search) {
        return mSearchService.getShows(search).map(ShowMapper::mapFromSearchShowDto);
    }

    public Observable<List<Show>> getPersonShows(int id) {
        return mPeopleService.getPersonShows(id).map(ShowMapper::mapFromPersonCastCreditsDto);
    }

    public ShowDescription getShowDescription(Show show) {
        return ShowMapper.mapToShowDescription(show);
    }

    public List<Episode> getEpisodesBySeasson(List<Episode> episodeList, int seasson) {
        return episodeList.stream().
                filter(episode -> episode.getSeason() == seasson).
                collect(Collectors.toList());
    }

    public List<Show> getFavoriteShows() {
        return ShowMapper.mapFromFavoriteShow(mDBHelper.getFavoriteShowList());
    }

    public boolean isFavorite(int id) {
        return mDBHelper.getFavoriteShowbyId(id) != null;
    }

    public void insertFavoriteShow(Show show) {
        mDBHelper.insertFavoriteShow(ShowMapper.mapToFavoriteShow(show));
    }

    public void removeFavoriteShow(int id) {
        mDBHelper.removeFavoriteShow(id);
    }

    public void getShowsFailed() {
        this.mPage--;
    }

    //Setters
    public void setEnded(boolean ended) {
        this.mEnded = ended;
    }

}
