package com.jobsity.tvshows.ui.presentation.showinfo;

import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.presentation.ViewState;

import java.util.ArrayList;
import java.util.List;

public class PersonaInfoState extends ViewState {
    private boolean personShowListLoaded = false;
    private List<Show> personShowList = new ArrayList<>();
}
