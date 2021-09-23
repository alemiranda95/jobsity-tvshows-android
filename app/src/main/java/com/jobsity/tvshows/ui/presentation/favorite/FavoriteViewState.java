package com.jobsity.tvshows.ui.presentation.favorite;

import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.presentation.ViewState;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FavoriteViewState extends ViewState {
    private boolean showListChanged = false;
    private List<Show> showList = new ArrayList<>();
}
