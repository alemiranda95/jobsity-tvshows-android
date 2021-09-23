package com.jobsity.tvshows.domain.model.episode;

import lombok.Data;

@Data
public class Episode {
    private final int id;
    private final String name;
    private final int season;
    private final int number;
    private final String posterMedium;
    private final String posterOriginal;
    private final String summary;
}
