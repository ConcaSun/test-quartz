package com.usoft.suntg.quartz.job;

import com.usoft.suntg.quartz.entity.JobConfigEntity;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 执行GRPC接口的任务
 * @author suntg
 */
public class StandardGrpcJob extends QuartzJobBean {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobConfigEntity jobConfigEntity = (JobConfigEntity) context.getMergedJobDataMap().get("jobConfigEntity");
        // 根据jobConfigEntity 的配置信息发起Grpc请求
        logger.info("StandardGrpcJob executeInternal, job id: {}, job name: {}, job cron: {}",
                jobConfigEntity.getJobDetailId(), jobConfigEntity.getName(), jobConfigEntity.getCron());
    }
}
