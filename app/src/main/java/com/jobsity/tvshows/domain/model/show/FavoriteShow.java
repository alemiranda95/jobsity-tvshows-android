package com.jobsity.tvshows.domain.model.show;

import lombok.Data;

@Data
public class FavoriteShow {
    private final int id;
    private final String name;
    private final String posterOriginal;
    private final String posterMedium;
    private final String time;
    private final String days;
    private final String genre;
    private final String summary;
    private final String rating;
    private final String premier;
}
