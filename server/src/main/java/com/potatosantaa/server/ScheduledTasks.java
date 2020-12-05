package com.potatosantaa.server;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.google.firebase.auth.FirebaseAuthException;
import com.potatosantaa.server.services.GmailAPI;
import com.potatosantaa.server.services.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-YY");
    
    // @Scheduled(fixedRate = 5000)
    // This piece of code will run top of every hour of every day


    @Scheduled(cron = "0 0 0 * * *", zone="GMT+8:00")
	public void execute() throws FirebaseAuthException, ExecutionException, InterruptedException {
        // do the scheduled job

        GmailAPI gmailAPI = new GmailAPI();
        JobService service = new JobService();
        gmailAPI.getTasks(service);
        log.info("The time is now {}", dateFormat.format(new Date()));
	}
}
