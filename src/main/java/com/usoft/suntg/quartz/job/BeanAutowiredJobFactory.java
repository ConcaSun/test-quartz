package com.usoft.suntg.quartz.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * Spring bean Autowired JobFactory
 * @author suntg
 * @date 2018年10月10日16:45:45
 */
@Component
public class BeanAutowiredJobFactory extends AdaptableJobFactory {

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object instance = super.createJobInstance(bundle);
        autowireCapableBeanFactory.autowireBean(instance);
        return instance;
    }
}
