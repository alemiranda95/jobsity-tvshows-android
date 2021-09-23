package com.jobsity.tvshows.domain.model.show;

import lombok.Data;

@Data
public class ShowDescription {
    private final String summary;
    private final String genres;
    private final String airTime;
    private final String poster;
}
