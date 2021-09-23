package com.jobsity.tvshows.domain.model.show;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jobsity.tvshows.R;
import com.jobsity.tvshows.TvShowApp;
import com.jobsity.tvshows.domain.model.personcastcredits.PersonCastCreditsDto;
import com.jobsity.tvshows.domain.model.search.SearchShowDto;
import com.jobsity.tvshows.util.FormatUtils;
import com.jobsity.tvshows.util.constant.AppConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShowMapper {

    private ShowMapper() {}

    public static List<Show> mapFromShowDto(List<ShowDto> showDtoList) {
        return showDtoList.stream()
                .filter(ShowMapper::validShow)
                .map(ShowMapper::createShowFromDto)
                .collect(Collectors.toList());
    }

    public static List<Show> mapFromSearchShowDto(List<SearchShowDto> searchShowDtoList) {
        return searchShowDtoList.stream()
                .filter(searchShowDto -> validShow(searchShowDto.getShow()))
                .map(searchShowDto -> createShowFromDto(searchShowDto.getShow()))
                .collect(Collectors.toList());
    }

    public static List<Show> mapFromPersonCastCreditsDto(List<PersonCastCreditsDto> personCastCreditsDtoList) {
        return personCastCreditsDtoList.stream()
                .filter(searchShowDto -> validShow(searchShowDto.get_embedded().getShow()))
                .map(searchShowDto -> createShowFromDto(searchShowDto.get_embedded().getShow()))
                .collect(Collectors.toList());
    }

    public static List<Show> mapFromFavoriteShow(List<FavoriteShow> favoriteShowList) {
        return favoriteShowList.stream()
                .map(ShowMapper::createShowFromFavorite)
                .collect(Collectors.toList());
    }

    private static Show createShowFromFavorite(FavoriteShow favoriteShow) {
        return new Show(
                favoriteShow.getId(),
                favoriteShow.getName(),
                favoriteShow.getPosterOriginal(),
                favoriteShow.getPosterMedium(),
                favoriteShow.getTime(),
                FormatUtils.stringToStringList(favoriteShow.getDays()),
                FormatUtils.stringToStringList(favoriteShow.getGenre()),
                favoriteShow.getSummary(),
                favoriteShow.getRating(),
                favoriteShow.getPremier()
        );
    }

    private static boolean validShow(ShowDto showDto) {
        return showDto.getId() != null
                && showDto.getName() != null
                && showDto.getImage() != null
                && showDto.getSchedule() != null
                && showDto.getSchedule().getDays() != null
                && showDto.getSchedule().getTime() != null
                && showDto.getSummary() != null
                && showDto.getPremiered() != null;
    }

    private static Show createShowFromDto(ShowDto showDto) {
        return new Show(
                showDto.getId(),
                showDto.getName(),
                showDto.getImage().getOriginal() != null ?
                        showDto.getImage().getOriginal() : showDto.getImage().getMedium(),
                showDto.getImage().getMedium(),
                showDto.getSchedule().getTime(),
                showDto.getSchedule().getDays(),
                showDto.getGenres(),
                showDto.getSummary(),
                showDto.getRating().getAverage(),
                FormatUtils.getYearFromDate(showDto.getPremiered())
        );
    }

    public static ShowDescription mapToShowDescription(Show show) {
        StringBuilder genreBuilder = new StringBuilder();
        List<String> genres = show.getGenre();
        for (String genre : genres) {
            genreBuilder.append(genre);
            if (genres.indexOf(genre) != genres.size() -1) {
                genreBuilder.append(AppConstants.VERTICAL_VAR_SEPARATOR);
            }
        }
        String genre = genreBuilder.toString();

        StringBuilder daysBuilder = new StringBuilder();
        List<String> days = show.getDays();
        for (String day : days) {
            daysBuilder.append(day);
            if (days.indexOf(day) != days.size() -1) {
                daysBuilder.append(AppConstants.COMMA_STRING);
            }
        }
        String airTime = String.format(
                TvShowApp.getRes().getString(R.string.show_info_air_time),
                daysBuilder.toString(),
                show.getTime()
        );

        return new ShowDescription(
                FormatUtils.formatHtml(show.getSummary()),
                genre,
                FormatUtils.formatHtml(airTime),
                show.getPosterOriginal()
        );
    }

    public static FavoriteShow mapToFavoriteShow(Show show) {
        return new FavoriteShow(
                show.getId(),
                show.getName(),
                show.getPosterOriginal(),
                show.getPosterMedium(),
                show.getTime(),
                FormatUtils.stringlistToString(show.getDays()),
                FormatUtils.stringlistToString(show.getGenre()),
                show.getSummary(),
                show.getRating(),
                show.getPremier()
        );
    }
}
