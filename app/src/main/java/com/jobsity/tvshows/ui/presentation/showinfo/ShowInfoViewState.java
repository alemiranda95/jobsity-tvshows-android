package com.jobsity.tvshows.ui.presentation.showinfo;

import com.jobsity.tvshows.domain.model.episode.Episode;
import com.jobsity.tvshows.ui.presentation.ViewState;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ShowInfoViewState extends ViewState {
    private boolean episodeListChanged = false;
    private boolean seassonsSetted = false;
    private boolean favorite = false;
    private int lastSeasson = 0;
    private List<Episode> episodeList = new ArrayList<>();
}
