package com.potatosantaa.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    // @Scheduled(fixedRate = 5000)
    // This piece of code will run top of every hour of every day
    @Scheduled(cron = "0 0 * * * *", zone="GMT+8:00")    
	public void execute() {
        // do the scheduled job
        log.info("The time is now {}", dateFormat.format(new Date()));
	}
}
