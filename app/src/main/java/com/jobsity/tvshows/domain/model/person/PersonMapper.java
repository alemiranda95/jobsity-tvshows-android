package com.jobsity.tvshows.domain.model.person;

import com.jobsity.tvshows.domain.model.search.SearchPersonDto;

import java.util.List;
import java.util.stream.Collectors;

public class PersonMapper {
    private PersonMapper() {}

    public static List<Person> mapFromSearchPeopleDto(List<SearchPersonDto> searchPersonDtoList) {
        return searchPersonDtoList.stream()
                .filter(searchPersonDto -> validatePerson(searchPersonDto.getPerson()))
                .map(searchPersonDto -> createPerson(searchPersonDto.getPerson()))
                .collect(Collectors.toList());
    }

    private static boolean validatePerson(PersonDto personDto) {
        return personDto.getId() != null &&
                personDto.getName() != null &&
                personDto.getImage() != null;
    }

    private static Person createPerson(PersonDto personDto) {
        return new Person(
                personDto.getId(),
                personDto.getName(),
                personDto.getImage().getOriginal() != null ?
                        personDto.getImage().getOriginal() :
                        personDto.getImage().getMedium(),
                personDto.getImage().getMedium()
        );
    }
}
