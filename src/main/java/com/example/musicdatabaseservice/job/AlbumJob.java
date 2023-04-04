package com.example.musicdatabaseservice.job;

import com.example.musicdatabaseservice.converter.AlbumConverter;
import com.example.musicdatabaseservice.deezer.DeezerService;
import com.example.musicdatabaseservice.model.Album;
import com.example.musicdatabaseservice.service.AlbumService;
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
public class AlbumJob extends QuartzJobBean {

    @Autowired
    private DeezerService deezerService;

    @Autowired
    private AlbumConverter albumConverter;

    @Autowired
    private AlbumService albumService;

    @Override
    public void executeInternal(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        int start = jobDataMap.getInt("start");
        int size = jobDataMap.getInt("size");
        int step = jobDataMap.getInt("step");

        log.info("{} of pulling by id in pages of {} starting from {} with a step of {} is started", jobKey, size, start, step);

        Page<Album> albums;
        int page = start;
        do {
            albums = albumService.findByGenreIsNull(PageRequest.of(page, size));
            for (Album album : albums) {
                deezerService.getAsync(com.example.musicdatabaseservice.deezer.model.Album.class, album.getId()).thenAccept(a -> {
                    albumService.save(albumConverter.convert(a));
                });
            }
            page = page + step;
        } while (page < albums.getTotalPages());

        log.info("{} ended", jobKey);
    }
}
