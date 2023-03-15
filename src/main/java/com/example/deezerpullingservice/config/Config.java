package com.example.deezerpullingservice.config;

import com.example.deezerpullingservice.DeezerPullingJob;
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

import java.util.Properties;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Setter
public class Config {

    private int min;

    private int max;

    private int threadCount;

    private String cron;

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
        if (threadCount < 1) {
            throw new RuntimeException("Number of jobs is invalid: " + threadCount);
        }
        JobDetail[] jobDetails = new JobDetail[threadCount];
        Trigger[] triggers = new Trigger[threadCount];
        int i, j, range = Math.subtractExact(max, min) / threadCount;
        for (i = 0, j = min; i < threadCount - 1; i++, j = j + range) {
            JobDetail jobDetail = jobDetail(i + 1, j, j + range);
            jobDetails[i] = jobDetail;
            triggers[i] = trigger(i, jobDetail);
        }
        JobDetail jobDetail = jobDetail(i + 1, j, max);
        jobDetails[i] = jobDetail;
        triggers[i] = trigger(i, jobDetail);

        schedulerFactoryBean.setJobDetails(jobDetails);
        schedulerFactoryBean.setTriggers(triggers);
        Properties properties = new Properties();
        properties.setProperty(SchedulerFactoryBean.PROP_THREAD_COUNT, String.valueOf(threadCount));
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        return schedulerFactoryBean;
    }

    public JobDetail jobDetail(int i, int min, int max) {
        return JobBuilder
                .newJob(DeezerPullingJob.class)
                .withIdentity("PullingJob_" + i, "Deezer")
                .usingJobData("min", min)
                .usingJobData("max", max)
                .storeDurably()
                .build();
    }

    public Trigger trigger(int i, JobDetail jobDetail) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity("Trigger_" + i, "Deezer")
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24 * 5))
                .forJob(jobDetail)
                .startNow()
                .build();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Bean
    public GsonBuilderCustomizer gsonBuilderCustomizer() {
        return builder -> builder
                .registerTypeAdapterFactory(DateTypeAdapter.FACTORY)
                .registerTypeAdapterFactory(CountryTypeAdapter.FACTORY);
    }
}
