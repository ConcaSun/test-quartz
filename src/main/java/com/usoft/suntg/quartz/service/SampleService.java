package com.usoft.suntg.quartz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * sampleService
 * @author suntg
 * @date 2018年10月10日11:08:08
 */
@Component
public class SampleService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sayHello() {
        logger.info("Hello world.");
    }
}
