package com.jobsity.tvshows.ui.interfaces;

import com.jobsity.tvshows.domain.model.show.Show;

public interface SHowAdapterInterface {
    void onShowClick(Show show);

    void onLastItemReached();
}
