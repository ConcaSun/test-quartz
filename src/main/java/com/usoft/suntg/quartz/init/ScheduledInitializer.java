package com.usoft.suntg.quartz.init;

import com.usoft.suntg.quartz.entity.JobConfigEntity;
import com.usoft.suntg.quartz.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 程序启动执行
 * @author suntg
 */
@Component
public class ScheduledInitializer implements CommandLineRunner {

    @Autowired
    private JobService jobService;

    @Override
    public void run(String... args) throws Exception {
        JobConfigEntity jobConfigEntity = new JobConfigEntity();
        jobConfigEntity.setName("定时取消超时未付款的订单");
        jobConfigEntity.setCron("0/5 * * * * ? ");
        jobConfigEntity.setEnable(true);
        jobService.add(jobConfigEntity);

        jobConfigEntity = new JobConfigEntity();
        jobConfigEntity.setName("定时刷新计数器结果");
        jobConfigEntity.setCron("0/10 * * * * ? ");
        jobConfigEntity.setEnable(false);
        jobService.add(jobConfigEntity);

        jobConfigEntity = new JobConfigEntity();
        jobConfigEntity.setName("定时刷新B2B可送货状态");
        jobConfigEntity.setCron("0/3 * * * * ? ");
        jobConfigEntity.setEnable(true);
        jobService.add(jobConfigEntity);
    }
}
