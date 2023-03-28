package com.example.deezerpullingservice.job;

import com.example.deezerpullingservice.converter.TrackConverter;
import com.example.deezerpullingservice.deezer.DeezerService;
import com.example.deezerpullingservice.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrackJob extends QuartzJobBean {

    @Autowired
    private DeezerService deezerService;

    @Autowired
    private TrackConverter trackConverter;

    @Autowired
    private TrackService trackService;

    @Override
    public void executeInternal(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        int start = jobDataMap.getInt("start");
        int end = jobDataMap.getInt("end");
        int step = jobDataMap.getInt("step");

        log.info("{} of pulling by id from {} to {} with a step of {} is started", jobKey, start, end, step);

        for (int id = start + step; id <= end; id = id + step) {
            deezerService.getAsync(com.example.deezerpullingservice.deezer.model.Track.class, id).thenAccept(track -> {
                if (track != null && track.getPreview() != null && !track.getPreview().isEmpty()) {
                    trackService.save(trackConverter.convert(track));
                }
            });
        }

        log.info("{} ended", jobKey);
    }
}
