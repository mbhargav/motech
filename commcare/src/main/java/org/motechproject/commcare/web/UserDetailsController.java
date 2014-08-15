package org.motechproject.commcare.web;

import javax.servlet.http.HttpServletRequest;

import org.motechproject.commcare.service.CommcareUserService;
import org.motechproject.commcare.service.impl.CommcareCronScheduler;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserDetailsController {
	private static final Logger LOG = LoggerFactory.getLogger(UserDetailsController.class);
	
	@Autowired
	private CommcareUserService userService;
	@Autowired
	private CommcareCronScheduler scheduler;
	
    /**
     * This method is responsible for retrieving list of users from CommCareHQ and displaying this 
     * on webpage.
     */
    @RequestMapping(value = "/userdetails")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getDeails(@RequestBody String body, HttpServletRequest request) {
    	LOG.info("Entering getDetails");
    	String response = userService.getAllUsers().toString();
    	LOG.info("Service response: " + response);
    	return response;
    }
    
    @RequestMapping(value = "/schedule")
    @ResponseStatus(HttpStatus.OK)
    public void schdule() {
        if(scheduler != null) {
            scheduler.doScheduling();
        }
    }
    
    @RequestMapping(value = "/unschedule")
    @ResponseStatus(HttpStatus.OK)
    public void unschdule() {
        if(scheduler != null) {
            scheduler.doUnScheduling();
        }
    }
}
