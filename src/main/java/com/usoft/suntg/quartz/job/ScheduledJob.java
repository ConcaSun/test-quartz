package com.usoft.suntg.quartz.job;

import com.usoft.suntg.quartz.service.SampleService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * sample scheduled job
 * @author suntg
 * @date 2018年10月10日15:52:18
 */
@Component
public class ScheduledJob extends QuartzJobBean {

    @Autowired
    private SampleService sampleService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        sampleService.sayHello();
    }
}
