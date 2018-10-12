package com.usoft.suntg.quartz.controller;

import com.usoft.suntg.quartz.entity.JobConfigEntity;
import com.usoft.suntg.quartz.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Job controller
 * @author suntg
 */
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 获取所有配置
     * @return
     */
    @GetMapping()
    public Map<String, JobConfigEntity> getJobs () {
        return jobService.getJobConfigEntities();
    }

    /**
     * 重启某一个任务
     */
    @GetMapping("/resume")
    public void resume() {
        JobConfigEntity jobConfigEntity = jobService.getJobConfigEntityById(2);
        jobConfigEntity.setEnable(true);
        jobService.startOrPause(jobConfigEntity);
    }

    /**
     * 暂停某一个任务
     */
    @GetMapping("/pause")
    public void pause() {
        JobConfigEntity jobConfigEntity = jobService.getJobConfigEntityById(2);
        jobConfigEntity.setEnable(false);
        jobService.startOrPause(jobConfigEntity);
    }

}
