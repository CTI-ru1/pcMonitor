package eu.uberdust.pcmonitor.task;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 3/29/12
 * Time: 5:34 PM
 */
public class UnixTask extends AbstractOsTask {
    /**
     * LOGGER.
     */
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(UnixTask.class);
    private static final long TIME_OFFSET1 = 5000;

    public UnixTask() {
        final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        final Scheduler sched;
        final List<JobDetail> jobs = new ArrayList<JobDetail>();
        final List<Trigger> triggers = new ArrayList<Trigger>();
        try {
            sched = schedulerFactory.getScheduler();

            ArrayList<Class> list = getJobClasses("unix");


            for (Class aClass : list) {
                LOGGER.info(aClass);


                jobs.add(newJob((Class<? extends Job>) aClass)
                        .withIdentity(aClass + "Job", aClass + "group")
                        .build());
                triggers.add(newTrigger()
                        .withIdentity(aClass + "Trigger", aClass + "group")
                        .startAt(new Date(System.currentTimeMillis() + TIME_OFFSET1))
                        .withSchedule(simpleSchedule()
                                .withIntervalInMinutes(30)
                                .repeatForever()).build());


            }
            for (int i = 0; i < jobs.size(); i++) {
                sched.scheduleJob(jobs.get(i), triggers.get(i));
            }

            LOGGER.info("jobcount:" + sched.getCurrentlyExecutingJobs().size());
            sched.start();

        } catch (SchedulerException e) {
            LOGGER.error(e);
        }
    }

}
