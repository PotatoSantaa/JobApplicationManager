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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/jobapp")
public class JobAppController {

    @Autowired
    JobService jobService;

    @GetMapping("/{id}")
    public JobApp getJobAppById(@PathVariable("id") String jobAppID) {
        return jobService.getJobAppById(jobAppID);
    }

    // Mappings for HashMap methods ==> will be deleted later
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

    @GetMapping("/getAllJobs")
    public List getAllJobApps() throws FirebaseAuthException, ExecutionException, InterruptedException {
        return jobService.getAllJobApps();
    }

    @GetMapping("/getAllTasks")
    public List getAllTasks() throws FirebaseAuthException, ExecutionException, InterruptedException {
        return jobService.getAllTasks();
    }

    @GetMapping("/getTask/{id}")
    public Task getTask(@PathVariable("id") String jobId)
            throws InterruptedException, ExecutionException, FirebaseAuthException {
        return jobService.getTask(jobId);
    }

    @DeleteMapping("/deleteTask")
    public String deleteTask(@RequestParam String jobId)
            throws InterruptedException, ExecutionException, FirebaseAuthException {

        return jobService.deleteTask(jobId);
    }

    // Mappings for Firebase database
    // @GetMapping("/getJob")
    // public JobApp getJob(@RequestParam String jobId)
    // throws InterruptedException, ExecutionException, FirebaseAuthException {
    // return jobService.getJob(jobId);
    // }

    @GetMapping("/getJob/{id}")
    public JobApp getJob(@PathVariable("id") String jobId)
            throws InterruptedException, ExecutionException, FirebaseAuthException {
        return jobService.getJob(jobId);
    }

    @PostMapping("/createJob")
    public String createJob(@RequestBody JobApp job)
            throws InterruptedException, ExecutionException, FirebaseAuthException {

        return jobService.addJob(job);
    }

    @PutMapping("/updateJob")
    public String updateJob(@RequestBody JobApp job)
            throws InterruptedException, ExecutionException, FirebaseAuthException {

        return jobService.updateJob(job);
    }

    @DeleteMapping("/deleteJob")
    public String deleteJob(@RequestParam String jobId)
            throws InterruptedException, ExecutionException, FirebaseAuthException {

        return jobService.deleteJob(jobId);
    }

}
