package org.motechproject.commcare.service.impl;


import java.util.Map;
import org.eclipse.gemini.blueprint.service.importer.OsgiServiceLifecycleListener;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for scheduling commcare job using MotechSchedulerService
 * @author mimansha
 *
 */
@Component
public class CommcareCronScheduler implements OsgiServiceLifecycleListener {

    private static final Logger LOG = LoggerFactory.getLogger(CommcareCronScheduler.class);
    
    @Autowired
    @Qualifier("commcareAPISettings")
    private SettingsFacade settingsFacade;
    
    private MotechSchedulerService motechSchedulerService;
    private final Object lock = new Object();
    
    public void doScheduling() {
        synchronized (lock) {
            if (motechSchedulerService != null) {                
                scheduleCommcareUsersPolling();                
            }
        }
    }
    
    public void doUnScheduling() {
        synchronized (lock) {
            if (motechSchedulerService != null) {                
                unschedulePollCommcareUsers();                
            }
        }
    }

    /**
     * This will raise an event of type POLL_COMMCAREUSERS_SUBJECT every min by using cron scheduler
     */
    private void scheduleCommcareUsersPolling() {
        LOG.error("Scheduling pollcommcareusers job");

        unschedulePollCommcareUsers();
        
        MotechEvent event = new MotechEvent(PollCommcareUsersHandler.POLL_COMMCAREUSERS_SUBJECT);
        String propertyValue = settingsFacade.getProperty("pollCommcareUsersMins");
        int mins = Integer.parseInt(propertyValue);
        String cronExpressionEveryMin = "0 0/" + mins + " * 1/1 * ? *";        
        CronSchedulableJob pollingJob = new CronSchedulableJob(event, cronExpressionEveryMin);
        
        motechSchedulerService.safeScheduleJob(pollingJob);

        LOG.error("PollCommcareUsers job scheduled");
    }

    private void unschedulePollCommcareUsers() {
        motechSchedulerService.unscheduleAllJobs(PollCommcareUsersHandler.POLL_COMMCAREUSERS_SUBJECT);
        LOG.error("Unscheduled PollCommcareUsers job");
    }

    @Override
    public void bind(Object service, Map properties) {
        if (service instanceof MotechSchedulerService) {
            LOG.error("CommcareScheduler service bound for mins: " + settingsFacade.getProperty("pollCommcareUsersMins"));
            motechSchedulerService = (MotechSchedulerService) service;            
        }
    }

    @Override
    public void unbind(Object service, Map properties) {
        if (service instanceof MotechSchedulerService) {
            LOG.error("Scheduler service unbound");
            motechSchedulerService = null;
        }
    }
}
