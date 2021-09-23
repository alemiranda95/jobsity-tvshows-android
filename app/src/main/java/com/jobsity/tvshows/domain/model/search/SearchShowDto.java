package com.jobsity.tvshows.domain.model.search;

import com.jobsity.tvshows.domain.model.show.ShowDto;

import lombok.Value;

@Value
public class SearchShowDto {
    double score;
    ShowDto show;
}
