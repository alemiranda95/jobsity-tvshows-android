package com.jobsity.tvshows.domain.model.episode;

import com.jobsity.tvshows.domain.model.image.ImageDto;

import lombok.Value;

@Value
public class EpisodeDto {
    Integer id;
    String url;
    String name;
    Integer season;
    Integer number;
    String type;
    ImageDto image;
    String summary;
}
