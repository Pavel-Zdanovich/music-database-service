package com.example.deezerpullingservice.deezer;

import com.example.deezerpullingservice.service.TrackService;
import com.example.deezerpullingservice.model.Track;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeezerJob extends QuartzJobBean {

    @Autowired
    private DeezerService deezerService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private TrackService trackService;

    @Override
    public void executeInternal(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        int min = jobDataMap.getInt("min");
        int max = jobDataMap.getInt("max");

        log.info("{} [{}, {}] started", jobKey, min, max);

        for (int id = min; id <= max; id++) {
            deezerService.getAsync(com.example.deezerpullingservice.deezer.model.Track.class, id).thenAccept(track -> {
                if (track != null && track.getReadable() && track.getPreview() != null) {
                    trackService.save(conversionService.convert(track, Track.class));
                }
            });
        }

        log.info("{} ended", jobKey);
    }
}
