package com.usoft.suntg.quartz.entity;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 任务配置实体
 * @author suntg
 * @date 2018年10月10日16:57:03
 */
public class JobConfigEntity {

    private static int ID_COUNTER = 1;
    private static final String MYJOB_PREFX = "MYJOB_";

    /**
     * ID 自动生成
     */
    private int id = ID_COUNTER ++;
    /**
     * 名称
     */
    private String name;
    /**
     * cron 表达式
     */
    private String cron;
    /**
     * 是否启用
     */
    private boolean enable;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 自动生成调度任务
     * @return
     */
    public QuartzJobBean getJobBean() {
        return new QuartzJobBean() {
            @Override
            protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                System.out.println(id + "_" + name + "_" + cron);
            }
        };
    }

    /**
     * 加入到调度器的jobDetail的名称
     * @return
     */
    public String getJobDetailId() {
        return MYJOB_PREFX + this.id;
    }
}
