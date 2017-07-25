/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package intranetFileSearch;

import java.util.Date;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
/**
 *
 * @author Arpan
 */
public class FTPTriggerRunner
{
    public FTPTriggerRunner()
    {

    }

    public void task() throws SchedulerException
    {
        // Initiate a Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // Retrieve a scheduler from schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();

        // current time
        long ctime = System.currentTimeMillis();

        // Initiate JobDetail with job name, job group, and executable job class
        JobDetail jobDetail = new JobDetail("jobDetail-s1", "jobDetailGroup-s1", FTPRunnerJob.class);
        // Initiate SimpleTrigger with its name and group name
        SimpleTrigger simpleTrigger = new SimpleTrigger("simpleTrigger", "triggerGroup-s1");
        // set its start up time
        simpleTrigger.setStartTime(new Date(ctime));
        // set the interval, how often the job should run (15 mins here)
        simpleTrigger.setRepeatInterval(24*60*60000);
        // set the number of execution of this job, set to 10 times.
        // It will run 10 time and exhaust.

        
        simpleTrigger.setRepeatCount(5);   //to be changed in real time
        // set the ending time of this job.
        // We set it for 60 seconds from its startup time here
        // Even if we set its repeat count to 10,
        // this will stop its process after 6 repeats as it gets it endtime by then.
        //simpleTrigger.setEndTime(new Date(ctime + 60000L));
        // set priority of trigger. If not set, the default is 5
        //simpleTrigger.setPriority(10);
        // schedule a job with JobDetail and Trigger
        scheduler.scheduleJob(jobDetail, simpleTrigger);

        // start the scheduler
        scheduler.start();
    }

    public void FTPManualCrawlTask() throws SchedulerException
    {
        // Initiate a Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // Retrieve a scheduler from schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();

        // current time
        long ctime = System.currentTimeMillis();

        // Initiate JobDetail with job name, job group, and executable job class
        JobDetail jobDetail = new JobDetail("jobDetail-s2", "jobDetailGroup-s2", FTPManualCrawl.class);
        // Initiate SimpleTrigger with its name and group name
        SimpleTrigger simpleTrigger = new SimpleTrigger("simpleTriggerFTP", "triggerGroup-s2");
        // set its start up time
        simpleTrigger.setStartTime(new Date(ctime));
        simpleTrigger.setRepeatCount(0);   //to be changed in real time
        scheduler.scheduleJob(jobDetail, simpleTrigger);
        scheduler.start();
    }

    public void SMBManualCrawlTask() throws SchedulerException
    {
        // Initiate a Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // Retrieve a scheduler from schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();
        long ctime = System.currentTimeMillis();

        // Initiate JobDetail with job name, job group, and executable job class
        JobDetail jobDetail = new JobDetail("jobDetail-s3", "jobDetailGroup-s3", SMBManualCrawl.class);
        // Initiate SimpleTrigger with its name and group name
        SimpleTrigger simpleTrigger = new SimpleTrigger("simpleTriggerSMB", "triggerGroup-s3");
        // set its start up time
        simpleTrigger.setStartTime(new Date(ctime));
        simpleTrigger.setRepeatCount(0);   //to be changed in real time
        scheduler.scheduleJob(jobDetail, simpleTrigger);
        scheduler.start();
    }

    public void WebManualCrawlTask() throws SchedulerException
    {
        // Initiate a Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // Retrieve a scheduler from schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();
        long ctime = System.currentTimeMillis();
        JobDetail jobDetail = new JobDetail("jobDetail-s4", "jobDetailGroup-s4", WebManualCrawl.class);
        // Initiate SimpleTrigger with its name and group name
        SimpleTrigger simpleTrigger = new SimpleTrigger("simpleTriggerWeb", "triggerGroup-s4");
        // set its start up time
        simpleTrigger.setStartTime(new Date(ctime));
        simpleTrigger.setRepeatCount(0);   //to be changed in real time
        scheduler.scheduleJob(jobDetail, simpleTrigger);
        scheduler.start();
    }
    /*
    public void BlacklistTask() throws SchedulerException
    {
        // Initiate a Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // Retrieve a scheduler from schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();

        // current time
        long ctime = System.currentTimeMillis();

        // Initiate JobDetail with job name, job group, and executable job class
        JobDetail jobDetail = new JobDetail("jobDetail-s1", "jobDetailGroup-s1", FTPRunnerJob.class);
        // Initiate SimpleTrigger with its name and group name
        SimpleTrigger simpleTrigger = new SimpleTrigger("simpleTrigger", "triggerGroup-s1");
        // set its start up time
        simpleTrigger.setStartTime(new Date(ctime));
        // set the interval, how often the job should run (15 mins here)
        simpleTrigger.setRepeatInterval(24*60*60000);
        // set the number of execution of this job, set to 10 times.
        // It will run 10 time and exhaust.


        simpleTrigger.setRepeatCount(5);   //to be changed in real time
        // set the ending time of this job.
        // We set it for 60 seconds from its startup time here
        // Even if we set its repeat count to 10,
        // this will stop its process after 6 repeats as it gets it endtime by then.
        //simpleTrigger.setEndTime(new Date(ctime + 60000L));
        // set priority of trigger. If not set, the default is 5
        //simpleTrigger.setPriority(10);
        // schedule a job with JobDetail and Trigger
        scheduler.scheduleJob(jobDetail, simpleTrigger);

        // start the scheduler
        scheduler.start();
    }
     */
}
