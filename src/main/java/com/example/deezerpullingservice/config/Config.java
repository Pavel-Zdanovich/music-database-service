package com.example.deezerpullingservice.config;

import com.example.deezerpullingservice.converter.AlbumConverter;
import com.example.deezerpullingservice.converter.ArtistConverter;
import com.example.deezerpullingservice.converter.GenreConverter;
import com.example.deezerpullingservice.converter.TrackConverter;
import com.example.deezerpullingservice.job.AlbumJob;
import com.example.deezerpullingservice.job.ArtistJob;
import com.example.deezerpullingservice.job.TrackJob;
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

import java.util.Map;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("jobs")
@Setter
public class Config {

    private Map<String, String> albumJob;

    private Map<String, String> artistJob;

    private Map<String, String> trackJob;

    private boolean overwriteExistingJobs;

    private boolean autoStartup;

    private JobDetail[] jobDetails;

    private Trigger[] triggers;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(SpringBeanJobFactory springBeanJobFactory) {
        int threadCount = parallelize();
        Properties properties = new Properties();
        properties.setProperty(SchedulerFactoryBean.PROP_THREAD_COUNT, String.valueOf(threadCount));
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(springBeanJobFactory);
        schedulerFactoryBean.setJobDetails(jobDetails);
        schedulerFactoryBean.setTriggers(triggers);
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setOverwriteExistingJobs(overwriteExistingJobs);
        schedulerFactoryBean.setAutoStartup(autoStartup);
        return schedulerFactoryBean;
    }

    private int parallelize() {
        int numberOfAlbumJobs = Integer.parseInt(albumJob.get("number"));
        if (numberOfAlbumJobs < 0 || numberOfAlbumJobs > 3) {
            throw new RuntimeException("Number of album jobs is invalid: " + numberOfAlbumJobs);
        }
        int sizeOfAlbumPage = Integer.parseInt(albumJob.get("pageSize"));
        if (sizeOfAlbumPage < 1 || sizeOfAlbumPage > 100) {
            throw new RuntimeException("Size of album page is invalid: " + sizeOfAlbumPage);
        }
        String cronOfAlbumJob = albumJob.get("cron");

        int numberOfArtistJobs = Integer.parseInt(artistJob.get("number"));
        if (numberOfArtistJobs < 0 || numberOfArtistJobs > 3) {
            throw new RuntimeException("Number of artist jobs is invalid: " + numberOfArtistJobs);
        }
        int sizeOfArtistPage = Integer.parseInt(artistJob.get("pageSize"));
        if (sizeOfArtistPage < 1 || sizeOfArtistPage > 100) {
            throw new RuntimeException("Size of artist page is invalid: " + sizeOfArtistPage);
        }
        String cronOfArtistJob = artistJob.get("cron");

        int start = Integer.parseInt(trackJob.get("start"));
        int end = Integer.parseInt(trackJob.get("end"));
        if (start < 1 || end <= start) {
            throw new RuntimeException("Range of track id is invalid: [%d, %d]".formatted(start, end));
        }
        int numberOfTrackJobs = Integer.parseInt(trackJob.get("number"));
        if (numberOfTrackJobs < 0 || numberOfTrackJobs > 5) {
            throw new RuntimeException("Number of track jobs is invalid: " + numberOfTrackJobs);
        }
        String cronOfTrackJob = trackJob.get("cron");

        int threadCount = numberOfAlbumJobs + numberOfArtistJobs + numberOfTrackJobs;
        jobDetails = new JobDetail[threadCount];
        triggers = new Trigger[threadCount];

        int index = 0;
        for (int i = 0; i < numberOfAlbumJobs; i++, index++) {
            schedule(index, AlbumJob.class, i + 1,
                    Map.of(
                            "start", i * numberOfAlbumJobs,
                            "size", sizeOfAlbumPage,
                            "step", (i + 1) * numberOfAlbumJobs
                    ),
                    cronOfAlbumJob
            );
        }
        for (int i = 0; i < numberOfArtistJobs; i++, index++) {
            schedule(index, ArtistJob.class, i + 1,
                    Map.of(
                            "start", i * numberOfArtistJobs,
                            "size", sizeOfArtistPage,
                            "step", (i + 1) * numberOfArtistJobs
                    ),
                    cronOfArtistJob
            );
        }
        for (int i = 0; i < numberOfTrackJobs; i++, index++) {
            schedule(index, TrackJob.class, i + 1,
                    Map.of(
                            "start", start + i,
                            "end", end,
                            "step", numberOfTrackJobs
                    ),
                    cronOfTrackJob
            );
        }

        return threadCount;
    }

    private void schedule(int index, Class<? extends Job> jobClass, int id, Map<?, ?> data, String cron) {
        JobDetail jobDetail = jobDetail(jobClass, id, data);
        jobDetails[index] = jobDetail;
        Trigger trigger = trigger(jobDetail, cron);
        triggers[index] = trigger;
    }

    private JobDetail jobDetail(Class<? extends Job> jobClass, int id, Map<?, ?> data) {
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName() + id)
                .setJobData(new JobDataMap(data))
                .storeDurably()
                .build();
    }

    private Trigger trigger(JobDetail jobDetail, String cron) {
        return TriggerBuilder
                .newTrigger()
                .forJob(jobDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
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

    @Bean
    public AlbumConverter albumConverter(GenreConverter genreConverter) {
        return new AlbumConverter(genreConverter);
    }

    @Bean
    public ArtistConverter artistConverter() {
        return new ArtistConverter();
    }

    @Bean
    public GenreConverter genreConverter() {
        return new GenreConverter();
    }

    @Bean
    public TrackConverter trackConverter(AlbumConverter albumConverter, ArtistConverter artistConverter) {
        return new TrackConverter(albumConverter, artistConverter);
    }
}
