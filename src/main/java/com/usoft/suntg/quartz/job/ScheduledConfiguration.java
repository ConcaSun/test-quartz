package com.usoft.suntg.quartz.job;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * ScheduledConfiguration
 * @author suntg
 * @date 2018年10月10日16:46:15
 */
@Configuration
public class ScheduledConfiguration {

    @Autowired
    private BeanAutowiredJobFactory beanAutowiredJobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean () {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        // 能够获取到自动注入的spring bean
        schedulerFactoryBean.setJobFactory(beanAutowiredJobFactory);
        return schedulerFactoryBean;
    }
}
