package com.jobsity.tvshows.ui.presentation.personinfo;

import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.presentation.ViewState;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PersonInfoViewState extends ViewState {
    private boolean personShowListChanged = false;
    private List<Show> personShowList = new ArrayList<>();
}
