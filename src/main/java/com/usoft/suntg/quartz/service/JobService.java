package com.usoft.suntg.quartz.service;

import com.usoft.suntg.quartz.entity.JobConfigEntity;
import com.usoft.suntg.quartz.job.StandardGrpcJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private static Logger logger = LoggerFactory.getLogger(JobService.class);

    /**
     * 任务配置容器
     */
    private static Map<String, JobConfigEntity> jobConfigEntities = new HashMap<String, JobConfigEntity>();

    /**
     * 调度器
     */
    private Scheduler scheduler;

    private static final String JOB_GROUP_NAME = "MY_JOB_GROUP_NAME";
    private static final String TRIGGER_GROUP_NAME = "MY_TRIGGER_GROUP_NAME";

    /**
     * 获取调度器
     * 需要注意的是：schedulerFactoryBean.getScheduler() 获取返回的也是schedulerFactoryBean对象内的scheduler私有属性，而不是重新创建一个
     * @return
     */
    private Scheduler getScheduler() {
        if (scheduler == null) {
            scheduler = schedulerFactoryBean.getScheduler();
        }
        return scheduler;
    }

    /**
     * 添加一个调度任务
     * @param jobConfigEntity
     */
    public void add(JobConfigEntity jobConfigEntity) {
        jobConfigEntities.put(jobConfigEntity.getJobDetailId(), jobConfigEntity);
        JobDetail jobDetail = JobBuilder.newJob(StandardGrpcJob.class)
                .withIdentity(jobConfigEntity.getJobDetailId(), JOB_GROUP_NAME).build();
        jobDetail.getJobDataMap().put("jobConfigEntity", jobConfigEntity);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobConfigEntity.getCron());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobConfigEntity.getJobDetailId(), TRIGGER_GROUP_NAME)
                .withSchedule(scheduleBuilder).build();

        try {
            getScheduler().scheduleJob(jobDetail, trigger);
            if (jobConfigEntity.isEnable()) {
                getScheduler().start();
            } else {
                // 默认不启用的，需要暂停
                getScheduler().pauseJob(jobDetail.getKey());
            }
            logger.info("Update a job, jobDetail: {}, {}; trigger: {}, {}", jobDetail.getKey().getName(), jobDetail.getKey().getName(),
                    trigger.getKey().getGroup(), trigger.getKey().getName());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改调度任务的内容或配置
     * @param jobConfigEntity
     */
    public void update(JobConfigEntity jobConfigEntity) {
        JobKey jobKey = JobKey.jobKey(jobConfigEntity.getJobDetailId(), JOB_GROUP_NAME);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobConfigEntity.getJobDetailId(), TRIGGER_GROUP_NAME);

        try {
            // 移除原有任务
            Trigger oldTrigger = getScheduler().getTrigger(triggerKey);
            if (oldTrigger != null) {
                getScheduler().pauseTrigger(triggerKey);
                getScheduler().unscheduleJob(triggerKey);
            }
            getScheduler().deleteJob(jobKey);
            logger.info("Update job, delete old job: {}, {}.", jobKey.getGroup(), jobKey.getName());

            // 添加新的任务
            JobDetail jobDetail = JobBuilder.newJob(StandardGrpcJob.class)
                    .withIdentity(jobConfigEntity.getJobDetailId(), JOB_GROUP_NAME).build();
            jobDetail.getJobDataMap().put("jobConfigEntity", jobConfigEntity);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobConfigEntity.getCron());
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobConfigEntity.getJobDetailId(), TRIGGER_GROUP_NAME)
                    .withSchedule(scheduleBuilder).build();
            getScheduler().scheduleJob(jobDetail, trigger);
            if (jobConfigEntity.isEnable()) {
                getScheduler().start();
            } else {
                // 默认不启用的，需要暂停
                getScheduler().pauseJob(jobDetail.getKey());
            }
            logger.info("Update job, add new job: {}, {}.", jobKey.getGroup(), jobKey.getName());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    /**
     * 只针对已经存在的任务
     * @param jobConfigEntity
     */
    public void startOrPause(JobConfigEntity jobConfigEntity) {
        JobKey jobKey = JobKey.jobKey(jobConfigEntity.getJobDetailId(), JOB_GROUP_NAME);
        try {

            if (jobConfigEntity.isEnable()) {
                // 启动
                getScheduler().resumeJob(jobKey);
                logger.info("Resume a job,  job: {}, {}.", jobKey.getGroup(), jobKey.getName());
            } else {
                // 暂停
                getScheduler().pauseJob(jobKey);
                logger.info("Pause a job, job: {}, {}.", jobKey.getGroup(), jobKey.getName());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前的配置
     * @return
     */
    public Map<String, JobConfigEntity> getJobConfigEntities() {
        return jobConfigEntities;
    }

    /**
     * 根据id获取已经存在的job配置对象
     * @param id
     * @return
     */
    public JobConfigEntity getJobConfigEntityById(int id) {
        JobConfigEntity jobConfigEntity = new JobConfigEntity();
        jobConfigEntity.setId(id);
        jobConfigEntity = jobConfigEntities.get(jobConfigEntity.getJobDetailId());
        return jobConfigEntity;
    }

}
