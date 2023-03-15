package com.example.deezerpullingservice;

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
public class DeezerPullingJob extends QuartzJobBean {

    @Autowired
    private DeezerPullingService deezerPullingService;

    @Autowired
    private TrackService service;

    @Override
    public void executeInternal(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        int min = jobDataMap.getInt("min");
        int max = jobDataMap.getInt("max");

        log.info("{} [{}, {}] started", jobKey, min, max);

        for (int id = min; id <= max; id++) {
            deezerPullingService.track(id).thenAccept(track -> {
                if (track != null) {
                    service.save(track);
                }
            });
        }

        log.info("{} ended", jobKey);
    }
}
