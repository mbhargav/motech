package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.service.CommcareUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PollCommcareUsersScheduledJob implements Job {
	private final Logger log = LoggerFactory.getLogger(PollCommcareUsersScheduledJob.class);
	
	@Autowired
	private CommcareUserService userService;
	
    @Override
    @SuppressWarnings("unchecked")
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.error("executing PollCommcareUsersScheduledJob");

        try {
            // Fetch commcare user list and fire an event
        	String response = userService.getAllUsers().toString();
        	
        	// Raise an event for the user details received
        	
            
        } catch (Exception e) {
            log.error("Job execution failed.", e);
        }
    }
}
