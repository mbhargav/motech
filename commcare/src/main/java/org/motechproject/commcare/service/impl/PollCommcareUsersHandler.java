package org.motechproject.commcare.service.impl;
import org.motechproject.commcare.service.CommcareUserService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  Job responsible for querying CommcareHQ to retrieve User list and raise an event.
 *  This is going to listen to all the events of subject POLL_COMMCAREUSERS_SUBJECT, 
 *  when it receives corresponding event, it will request commcarehq for list of users 
 *  and then raise an event with that data.
 */
@Service
public class PollCommcareUsersHandler {
    private final Logger log = LoggerFactory.getLogger(PollCommcareUsersHandler.class);

    public static final String POLL_COMMCAREUSERS_SUBJECT = "PollCommcareUsersScheduledJob";
    
    @Autowired
    private CommcareUserService commcareUserService;
    
    @Autowired
    private EventRelay eventRelay;
    
    @MotechListener (subjects = { POLL_COMMCAREUSERS_SUBJECT })
    public void handle(MotechEvent event) {
        log.error("CommcareHQ returned:     " + commcareUserService.getAllUsers().toString());
        
        eventRelay.sendEventMessage(new MotechEvent("test event"));
        log.error("Sent Event");
    }    
}