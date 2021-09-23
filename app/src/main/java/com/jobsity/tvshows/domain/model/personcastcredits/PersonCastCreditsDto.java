package com.jobsity.tvshows.domain.model.personcastcredits;

import com.jobsity.tvshows.domain.model.show.ShowDto;

import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class PersonCastCreditsDto {
    EmbeddedShowDto _embedded;

    @NoArgsConstructor
    public static class EmbeddedShowDto {
        private ShowDto show;

        public ShowDto getShow(){
            return show;
        }
    }
}
