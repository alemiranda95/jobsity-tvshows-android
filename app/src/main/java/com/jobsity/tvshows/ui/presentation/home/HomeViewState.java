package com.jobsity.tvshows.ui.presentation.home;

import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.presentation.ViewState;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class HomeViewState extends ViewState {
    private boolean showListChanged = false;
    private int previousShowListSize = 0;
    private List<Show> showList = new ArrayList<>();
}
