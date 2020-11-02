package com.potatosantaa.server.services;
import com.google.firebase.auth.FirebaseAuthException;
import com.potatosantaa.server.profiles.JobApp;
import com.potatosantaa.server.profiles.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import java.util.ArrayList;
import java.util.HashMap;
// import java.util.List;
// import java.util.function.Predicate;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
// import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;

// CRUD Operations Service
@Service
public class JobService {


    public final String COL_NAME=  "/jobApps";
    User user;


    private HashMap<String, JobApp> listOfJobApps = new HashMap<String, JobApp>() {
        {
            put("10000", new JobApp("10000", "Software Engineer", "Google", "Code some stuff.", true));
            put("10001",new JobApp("10001","Software Engineer", "Amazon", "Do things.", true));
            put("10002",new JobApp("10002","Software Developer", "Northrop Grumman", "Save the world.", true));
            put("10003",new JobApp("10003","Software Engineering Intern", "Disney", "Solve problems.", true));
        }
    };

    public JobService() throws FirebaseAuthException {
    }

    private User getUser() throws FirebaseAuthException {
        if (user == null){
            user = new User();
        }

        return user;
    }





    public HashMap getAllJobApps(){
        return listOfJobApps;
    }

    public JobApp getJobAppById(String id){
      //  Predicate<JobApp> byId = jobApp -> jobApp.getJobID().equals(id);
        return listOfJobApps.get(id);
    }

    public void addJobApp(JobApp jobApp){
        listOfJobApps.put(jobApp.getJobID(), jobApp);
    }

    public void updateJobApp(JobApp jobApp, String id){
        listOfJobApps.put(id, jobApp);
    }

    public void deleteJobApp(String id){
        listOfJobApps.remove(id);
    }

    public String addJob(JobApp job) throws InterruptedException, ExecutionException, FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Users/" + getUser().getUID() + COL_NAME).document(job.getJobID()).set(job);
        return writeResult.get().getUpdateTime().toString();
    }

    public JobApp getJob(String jobId) throws InterruptedException, ExecutionException, FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Users/" + getUser().getUID() + COL_NAME).document(jobId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        
        JobApp newJob = null;
        if(doc.exists()){
            newJob = doc.toObject(JobApp.class);
            return newJob;
        } else{
            return null;
        }

    }

    public String updateJob(JobApp job) throws InterruptedException, ExecutionException, FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Users/" + getUser().getUID() + COL_NAME).document(job.getJobID()).set(job);
        return writeResult.get().getUpdateTime().toString();
    }

    public String deleteJob(String jobId) throws FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Users/" + getUser().getUID() + COL_NAME).document(jobId).delete();

        return "Document with Job ID " + jobId + " has been deleted";
    }


}
