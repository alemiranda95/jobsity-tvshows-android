package com.jobsity.tvshows.domain.model.person;

import com.jobsity.tvshows.domain.model.image.ImageDto;

import lombok.Value;

@Value
public class PersonDto {
    Integer id;
    String url;
    String name;
    ImageDto image;
}
