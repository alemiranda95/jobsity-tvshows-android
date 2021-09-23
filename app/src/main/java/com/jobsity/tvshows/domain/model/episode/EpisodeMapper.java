package com.jobsity.tvshows.domain.model.episode;

import com.jobsity.tvshows.domain.model.image.ImageDto;

import java.util.List;
import java.util.stream.Collectors;

public class EpisodeMapper {

    private EpisodeMapper() {}

    public static List<Episode> map(List<EpisodeDto> episodeDtoList) {
        return episodeDtoList.stream()
                .filter(EpisodeMapper::validateEpisode)
                .map(EpisodeMapper::createEpisode)
                .collect(Collectors.toList());
    }

    private static boolean validateEpisode(EpisodeDto episodeDto) {
        return episodeDto.getId() != null &&
                episodeDto.getName() != null &&
                episodeDto.getNumber() != null &&
                episodeDto.getSeason() != null &&
                episodeDto.getSummary() != null;
    }

    private static Episode createEpisode(EpisodeDto episodeDto) {
        ImageDto imageDto = episodeDto.getImage() != null ?
                episodeDto.getImage() :
                new ImageDto(null,null);
        return new Episode(
                episodeDto.getId(),
                episodeDto.getName(),
                episodeDto.getSeason(),
                episodeDto.getNumber(),
                imageDto.getMedium(),
                imageDto.getOriginal(),
                episodeDto.getSummary()
        );
    }
}
