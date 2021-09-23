package com.jobsity.tvshows.domain.model.show;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Show implements Serializable {
    private final int id;
    private final String name;
    private final String posterOriginal;
    private final String posterMedium;
    private final String time;
    private final List<String> days;
    private final List<String> genre;
    private final String summary;
    private final String rating;
    private final String premier;
}
