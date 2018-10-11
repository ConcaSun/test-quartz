package com.usoft.suntg.quartz.init;

import com.usoft.suntg.quartz.job.ScheduledJob;
import com.usoft.suntg.quartz.service.SampleService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class ScheduledInitializer implements CommandLineRunner {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public void run(String... args) throws Exception {
        // 创建scheduler
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // 创建jobDetail，可以指定名称和分组，也可以不指定，quartz会自动生成
        JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).build();

        // 创建 trigger，这里创建的是simple trigger，名称和分组可以指定也可以不指定，不指定或自动生成
        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1)
                .repeatForever()).startNow().build();

        // scheduler 加载jobDetail 和 trigger
        scheduler.scheduleJob(jobDetail, trigger);

        // scheduler 启动，按照trigger的规则触发调用jobDetail的executeInternal方法
        scheduler.start();
    }
}
