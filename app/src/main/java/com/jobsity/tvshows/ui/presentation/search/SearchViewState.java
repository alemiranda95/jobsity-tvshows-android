package com.jobsity.tvshows.ui.presentation.search;

import com.jobsity.tvshows.domain.model.person.Person;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.presentation.ViewState;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SearchViewState extends ViewState {
    private boolean showListChanged = false;
    private boolean personListChanged = false;
    private List<Show> showList = new ArrayList<>();
    private List<Person> personList = new ArrayList<>();
}
