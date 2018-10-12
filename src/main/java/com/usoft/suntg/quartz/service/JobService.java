package com.usoft.suntg.quartz.service;

import com.usoft.suntg.quartz.entity.JobConfigEntity;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
        JobDetail jobDetail = JobBuilder.newJob(jobConfigEntity.getJobBean().getClass())
                .withIdentity(jobConfigEntity.getJobDetailId(), JOB_GROUP_NAME).build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobConfigEntity.getCron());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobConfigEntity.getJobDetailId(), TRIGGER_GROUP_NAME)
                .withSchedule(scheduleBuilder).build();

        try {
            getScheduler().scheduleJob(jobDetail, trigger);
            getScheduler().start();
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
            JobDetail jobDetail = JobBuilder.newJob(jobConfigEntity.getJobBean().getClass())
                    .withIdentity(jobConfigEntity.getJobDetailId(), JOB_GROUP_NAME).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobConfigEntity.getCron());
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobConfigEntity.getJobDetailId(), TRIGGER_GROUP_NAME)
                    .withSchedule(scheduleBuilder).build();
            getScheduler().scheduleJob(jobDetail, trigger);
            getScheduler().start();
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
        if (jobConfigEntity.isEnable()) {
            // 启动
        } else {
            // 暂停
        }
    }

}
