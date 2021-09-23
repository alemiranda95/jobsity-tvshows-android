package com.jobsity.tvshows.domain.model.person;

import java.io.Serializable;

import lombok.Data;

@Data
public class Person implements Serializable {
    private final int id;
    private final String name;
    private final String photoOriginal;
    private final String photoMedium;
}
