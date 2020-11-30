package com.potatosantaa.server.services;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuthException;
import com.potatosantaa.server.profiles.JobApp;
import com.potatosantaa.server.profiles.Task;
import com.potatosantaa.server.profiles.User;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import java.util.Arrays;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
// import java.util.List;
// import java.util.function.Predicate;

import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
// import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

// CRUD Operations Service
@Service
public class JobService {


    private final String COL_NAME=  "/jobApps";
    private static final String TASK_NAME=  "/tasks";

    public JobService() throws FirebaseAuthException {}

    /* Get all job apps by user id */
    public List getAllJobApps(String userId) throws FirebaseAuthException, ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = db.collection("Users/" + userId + COL_NAME).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List serializedDocs = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            JobApp newJob = null;
            if (document.exists()) {
                newJob = document.toObject(JobApp.class);
                serializedDocs.add(newJob);
            } else {
                return null;
            }
        }
        System.out.println(serializedDocs.getClass());
        return serializedDocs;
    }

    public String addJob(String userId, JobApp job) throws InterruptedException, ExecutionException, FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Users/" + userId + COL_NAME).document(job.getJobID()).set(job);
        return writeResult.get().getUpdateTime().toString();
    }

    public JobApp getJob(String userId, String jobId) throws InterruptedException, ExecutionException, FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Users/" + userId + COL_NAME).document(jobId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();

        JobApp newJob = null;
        if (doc.exists()) {
            newJob = doc.toObject(JobApp.class);
            return newJob;
        } else {
            return null;
        }
    }

    public String updateJob(String userId, JobApp job) throws InterruptedException, ExecutionException, FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Users/" + userId + COL_NAME).document(job.getJobID()).set(job);
        return writeResult.get().getUpdateTime().toString();
    }

    public String deleteJob(String userId, String jobId) throws FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Users/" + userId + COL_NAME).document(jobId).delete();

        return "Document with Job ID " + jobId + " has been deleted";
    }

    /* Get all tasks by user id */
    public List getAllTasks(String userId) throws FirebaseAuthException, ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = db.collection("Users/" + userId + TASK_NAME).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List serializedDocs = new ArrayList();
        for (QueryDocumentSnapshot document : documents) {
            Task newTask = null;
            if(document.exists()){
                newTask = document.toObject(Task.class);
                serializedDocs.add(newTask);
                System.out.println(newTask.getClass());
                //serializedDocs.put(newTask.getJobID(), newTask);
            } else{
                return null;
            }
        }
        System.out.println(serializedDocs.getClass());
        return serializedDocs;
    }
    public Task getTask(String userId, String jobId) throws InterruptedException, ExecutionException, FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Users/" + userId + TASK_NAME).document(jobId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();

        Task newTask = null;
        if(doc.exists()){
            newTask = doc.toObject(Task.class);
            return newTask;
        } else{
            return null;
        }
    }
    public String deleteTask(String userId ,String jobId) throws FirebaseAuthException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection("Users/" + userId + TASK_NAME).document(jobId).delete();
        return "Task Document with Job ID " + jobId + " has been deleted";
    }


    private HashMap<String, JobApp> listOfJobApps = new HashMap<String, JobApp>() {
        {
            put("10000", new JobApp("10000", "Software Engineer", "Google", "Code some stuff.", true));
            put("10001",new JobApp("10001","Software Engineer", "Amazon", "Do things.", true));
            put("10002",new JobApp("10002","Software Developer", "Northrop Grumman", "Save the world.", true));
            put("10003",new JobApp("10003","Software Engineering Intern", "Disney", "Solve problems.", true));
        }
    };
    public JobApp getJobAppById(String id){
        //  Predicate<JobApp> byId = jobApp -> jobApp.getJobID().equals(id);
        return listOfJobApps.get(id);
    }
    public void addJobApp(JobApp jobApp) {
        listOfJobApps.put(jobApp.getJobID(), jobApp);
    }
    public void updateJobApp(JobApp jobApp, String id) {
        listOfJobApps.put(id, jobApp);
    }
    public void deleteJobApp(String id) {
        listOfJobApps.remove(id);
    }


}
