package com.potatosantaa.server.controllers;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.potatosantaa.server.profiles.JobApp;
import com.potatosantaa.server.profiles.Task;
import com.potatosantaa.server.profiles.User;
import com.potatosantaa.server.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;

import java.util.HashMap;
import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/jobapp")
public class JobAppController {

    @Autowired
    JobService jobService;

    @GetMapping("/getAllJobs/{id}")
    public List getAllJobApps(@PathVariable("id") String userId) throws FirebaseAuthException, ExecutionException, InterruptedException {
        return jobService.getAllJobApps(userId);
    }

    @PostMapping("/createJob/{id}")
    public String createJob(@PathVariable("id") String userId, @RequestBody JobApp job)
            throws InterruptedException, ExecutionException, FirebaseAuthException {

        return jobService.addJob(userId, job);
    }

    @GetMapping("/getJob/{id}/{jobId}")
    public JobApp getJob(@PathVariable("id") String userId, @PathVariable("jobId") String jobId)
            throws InterruptedException, ExecutionException, FirebaseAuthException {
        return jobService.getJob(userId, jobId);
    }

    @PutMapping("/updateJob/{id}")
    public String updateJob(@PathVariable("id") String userId, @RequestBody JobApp job)
            throws InterruptedException, ExecutionException, FirebaseAuthException {

        return jobService.updateJob(userId, job);
    }

    @DeleteMapping("/deleteJob/{id}/{jobId}")
    public String deleteJob(@PathVariable("id") String userId, @PathVariable("jobId") String jobId)
            throws InterruptedException, ExecutionException, FirebaseAuthException {

        return jobService.deleteJob(userId, jobId);
    }

    @GetMapping("/getAllTasks/{id}")
    public List getAllTasks(@PathVariable("id") String userId) throws FirebaseAuthException, ExecutionException, InterruptedException {
        return jobService.getAllTasks(userId);
    }

    @GetMapping("/getTask/{id}/{jobId}")
    public Task getTask(@PathVariable("id") String userId, @PathVariable("jobId") String jobId)
            throws InterruptedException, ExecutionException, FirebaseAuthException {
        return jobService.getTask(userId, jobId);
    }

    @DeleteMapping("/deleteTask/{id}/{jobId}")
    public String deleteTask(@PathVariable("id") String userId, @PathVariable("jobId") String jobId)
            throws InterruptedException, ExecutionException, FirebaseAuthException {

        return jobService.deleteTask(userId, jobId);
    }


    // Mappings for HashMap methods ==> will be deleted later    
    @GetMapping("/{id}")
    public JobApp getJobAppById(@PathVariable("id") String jobAppID) {
        return jobService.getJobAppById(jobAppID);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/jobapp")
    public void addJobApp(@RequestBody JobApp jobApp) {
        jobService.addJobApp(jobApp);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/jobapp/{id}")
    public void updateJobApp(@RequestBody JobApp jobApp, @PathVariable("id") String jobAppID) {
        jobService.updateJobApp(jobApp, jobAppID);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/jobapp/{id}")
    public void deleteJobApp(@PathVariable("id") String jobAppID) {
        jobService.deleteJobApp(jobAppID);
    }
}
