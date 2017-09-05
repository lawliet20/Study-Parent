/**
 * @Description:
 *
 * @Title: QuartzManager.java
 * @Package com.joyce.quartz
 * @Copyright: Copyright (c) 2014 
 *
 * @author Comsys-LZP 
 * @date 2014-6-26 下午03:15:52 
 * @version V2.0
 */
package com.wwj.timeTask;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

/**
 * @author Comsys-LZP
 * @version V2.0
 * @Description: 定时任务管理类
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2014
 * @date 2016年4月15日14:09:52
 */
public class QuartzManager {
    private static SchedulerFactory sf = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

    public static void main(String[] args) {
        String a = String.format("servers/%s/status", "192.168.0.1");
        System.out.println(a);
    }

    /**
     * 添加一个定时任务(默认jobGroup,默认triggerGroup)
     *
     * @param jobName
     * @param cls
     * @param time
     */
    @SuppressWarnings("unchecked")
    public static void addJob(String jobName, Class cls, String time) {
        try {
            JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
            JobDetail job = JobBuilder.newJob(cls).withIdentity(jobKey).build();
            job.getJobDataMap().put("name", "sherry");
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME).withSchedule(CronScheduleBuilder.cronSchedule(time)).build();

            Scheduler scheduler = sf.getScheduler();

            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个定时任务(默认jobGroup,默认triggerGroup),并在默认jobGroup添加一个监听
     *
     * @param jobName
     * @param cls
     * @param time
     */
    @SuppressWarnings("unchecked")
    /*public static void addJob(String jobName, Class cls, String time, JobListener jobListener) {
        try {
            JobKey jobKey = new JobKey(jobName, JOB_GROUP_NAME);
            JobDetail job = JobBuilder.newJob(cls).withIdentity(jobKey).build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME).withSchedule(CronScheduleBuilder.cronSchedule(time)).build();

            Scheduler scheduler = sf.getScheduler();
            // Listener attached to jobKey
            scheduler.getListenerManager().addJobListener(jobListener, KeyMatcher.keyEquals(jobKey));

            scheduler.getListenerManager().addTriggerListener(new TriggerListener());
            // Listener attached to group named "group 1" only.
            // scheduler.getListenerManager().addJobListener(
            // new HelloJobListener(), GroupMatcher.jobGroupEquals("group1")
            // );

            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 添加一个定时任务,并在jobGroup添加一个监听
     *
     * @param jobName
     * @param cls
     * @param time
     */
    public static void addJob(String jobName, String jobGroup, String triggerGroup, Class cls, String time, JobListener jobListener) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            JobDetail job = JobBuilder.newJob(cls).withIdentity(jobKey).build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, triggerGroup).withSchedule(CronScheduleBuilder.cronSchedule(time)).build();

            Scheduler scheduler = sf.getScheduler();
            // Listener attached to jobKey
            scheduler.getListenerManager().addJobListener(jobListener, KeyMatcher.keyEquals(jobKey));

            // Listener attached to group named "group 1" only.
            // scheduler.getListenerManager().addJobListener(
            // new HelloJobListener(), GroupMatcher.jobGroupEquals("group1")
            // );

            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}