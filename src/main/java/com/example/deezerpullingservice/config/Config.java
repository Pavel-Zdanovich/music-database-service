package com.example.deezerpullingservice.config;

import com.example.deezerpullingservice.deezer.DeezerJob;
import com.example.deezerpullingservice.musicbrainz.MusicBrainzJob;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.quartz.*;
import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("job")
@Setter
public class Config {

    private int min;

    private int max;

    private int numberOfJobs;

    private int deezerResponseTimeInSeconds;

    private String cron;

    private boolean overwriteExistingJobs;

    private boolean autoStartup;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(SpringBeanJobFactory springBeanJobFactory) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(springBeanJobFactory);

        if (min < 1 || max < 1 || max < min) {
            throw new RuntimeException("Range of track id is invalid: [%d, %d]".formatted(min, max));
        }
        if (numberOfJobs < 1) {
            throw new RuntimeException("Number of jobs is invalid: " + numberOfJobs);
        }
        if (deezerResponseTimeInSeconds < 1) {
            throw new RuntimeException("Deezer response time in seconds invalid: " + deezerResponseTimeInSeconds);
        }
        int threadCount = numberOfJobs * 2;
        JobDetail[] jobDetails = new JobDetail[threadCount];
        Trigger[] triggers = new Trigger[threadCount];
        int range = Math.subtractExact(max, min) / numberOfJobs;
        int delay = range * deezerResponseTimeInSeconds;
        int i, j, seconds, page;
        for (i = 0, j = min, seconds = 0, page = 0; i < threadCount - 2; i += 2, j += range, seconds += delay, page++) {
            JobDetail deezerJob = deezerJob(i + 1, j, j + range);
            jobDetails[i] = deezerJob;
            triggers[i] = deezerTrigger(i, deezerJob, LocalDateTime.now().plusSeconds(seconds));

            JobDetail musicbrainzJob = musicBrainzJob(i, page, range);
            jobDetails[i + 1] = musicbrainzJob;
            triggers[i + 1] = musicBrainzTrigger(i, musicbrainzJob, LocalDateTime.now().plusSeconds(seconds));
        }
        JobDetail deezerJob = deezerJob(i + 1, j, max);
        jobDetails[i] = deezerJob;
        triggers[i] = deezerTrigger(i, deezerJob, LocalDateTime.now().plusSeconds(seconds));

        JobDetail musicbrainzJob = musicBrainzJob(i, page, range);
        jobDetails[i + 1] = musicbrainzJob;
        triggers[i + 1] = musicBrainzTrigger(i, musicbrainzJob, LocalDateTime.now().plusSeconds(seconds));

        schedulerFactoryBean.setJobDetails(jobDetails);
        schedulerFactoryBean.setTriggers(triggers);
        Properties properties = new Properties();
        properties.setProperty(SchedulerFactoryBean.PROP_THREAD_COUNT, String.valueOf(threadCount));
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setOverwriteExistingJobs(overwriteExistingJobs);
        schedulerFactoryBean.setAutoStartup(autoStartup);
        return schedulerFactoryBean;
    }

    public JobDetail deezerJob(int i, int min, int max) {
        return JobBuilder
                .newJob(DeezerJob.class)
                .withIdentity("Job_" + i, "Deezer")
                .usingJobData("min", min)
                .usingJobData("max", max)
                .storeDurably()
                .build();
    }

    public Trigger deezerTrigger(int i, JobDetail jobDetail, LocalDateTime start) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity("Trigger_" + i, "Deezer")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .forJob(jobDetail)
                .startAt(Date.from(start.atZone(ZoneId.systemDefault()).toInstant()))
                .build();
    }

    public JobDetail musicBrainzJob(int i, int page, int size) {
        return JobBuilder
                .newJob(MusicBrainzJob.class)
                .withIdentity("Job_" + i, "MusicBrainz")
                .usingJobData("page", page)
                .usingJobData("size", size)
                .storeDurably()
                .build();
    }

    public Trigger musicBrainzTrigger(int i, JobDetail jobDetail, LocalDateTime start) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity("Trigger_" + i, "MusicBrainz")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .forJob(jobDetail)
                .startAt(Date.from(start.atZone(ZoneId.systemDefault()).toInstant()))
                .build();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Bean
    public GsonBuilderCustomizer gsonBuilderCustomizer() {
        return builder -> builder
                .registerTypeAdapterFactory(DateTypeAdapter.FACTORY);
    }
}
