package com.example.deezerpullingservice.musicbrainz;

import com.example.deezerpullingservice.model.Artist;
import com.example.deezerpullingservice.model.Country;
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

@Slf4j
@Component
public class MusicBrainzJob extends QuartzJobBean {

    @Autowired
    private MusicBrainzService musicBrainzService;

    @Autowired
    private ArtistService artistService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        int page = jobDataMap.getInt("page");
        int size = jobDataMap.getInt("size");

        log.info("{} [{}, {}] started", jobKey, page, size);

        Page<Artist> artists = artistService.findAll(PageRequest.of(page, size));

        for (Artist artist : artists) {
            musicBrainzService.getAsync(
                    com.example.deezerpullingservice.musicbrainz.model.Artist.class, artist.getName()
            ).thenAccept(a -> {
                Country country = new Country();
                country.setCode(a.getCountry());
                artist.setCountry(country);
                artistService.save(artist);
            });
        }

        log.info("{} ended", jobKey);
    }
}
