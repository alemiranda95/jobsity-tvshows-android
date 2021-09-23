package com.jobsity.tvshows.domain.model.show;

import com.jobsity.tvshows.domain.model.image.ImageDto;

import java.util.List;

import lombok.Value;

@Value
public class ShowDto {
    Integer id;
    String url;
    String name;
    String type;
    String language;
    String premiered;
    List<String> genres;
    String status;
    ShowSchedulDto schedule;
    ImageDto image;
    String summary;
    ShowRating rating;

    @Value
    static class ShowSchedulDto {
        String time;
        List<String> days;
    }

    @Value
    static class ShowRating {
        String average;
    }
}
