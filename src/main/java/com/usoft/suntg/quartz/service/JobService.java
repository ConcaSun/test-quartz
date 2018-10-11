package com.usoft.suntg.quartz.service;

import com.usoft.suntg.quartz.entity.JobConfigEntity;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.HashMap;
import java.util.Map;

public class JobService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

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
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
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
