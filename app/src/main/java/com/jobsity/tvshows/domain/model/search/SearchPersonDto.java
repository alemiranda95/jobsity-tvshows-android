package com.jobsity.tvshows.domain.model.search;

import com.jobsity.tvshows.domain.model.person.PersonDto;

import lombok.Value;

@Value
public class SearchPersonDto {
    double score;
    PersonDto person;
}
