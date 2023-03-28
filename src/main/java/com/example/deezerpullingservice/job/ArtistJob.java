package com.example.deezerpullingservice.job;

import com.example.deezerpullingservice.model.Artist;
import com.example.deezerpullingservice.model.Country;
import com.example.deezerpullingservice.musicbrainz.MusicBrainzService;
import com.example.deezerpullingservice.service.ArtistService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class ArtistJob extends QuartzJobBean {

    @Autowired
    private MusicBrainzService musicBrainzService;

    @Autowired
    private ArtistService artistService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        int start = jobDataMap.getInt("start");
        int size = jobDataMap.getInt("size");
        int step = jobDataMap.getInt("step");

        log.info("{} of pulling by id in pages of {} starting from {} with a step of {} is started", jobKey, size, start, step);

        Page<Artist> artists;
        int page = start;
        do {
            artists = artistService.findByCountryIsNull(PageRequest.of(page, size));
            for (Artist artist : artists) {
                CompletableFuture<com.example.deezerpullingservice.musicbrainz.model.Artist> completableFuture =
                        musicBrainzService.getAsync(
                                com.example.deezerpullingservice.musicbrainz.model.Artist.class, artist.getName()
                        );
                com.example.deezerpullingservice.musicbrainz.model.Artist a;
                try {
                    a = completableFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error(e.toString());
                    continue;
                }
                if (a == null || a.getCountry() == null) {
                    continue;
                }
                Country country = new Country();
                country.setCode(a.getCountry());
                artist.setCountry(country);
                artistService.save(artist);
            }
            page = page + step;
        } while (page < artists.getTotalPages());

        log.info("{} ended", jobKey);
    }
}
