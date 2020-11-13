package com.potatosantaa.server.services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.jar.JarOutputStream;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.core.ApiFuture;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.model.Thread;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.FirestoreClient;
import com.potatosantaa.server.profiles.JobApp;
import com.potatosantaa.server.profiles.Task;
import com.potatosantaa.server.profiles.User;
import io.restassured.path.json.JsonPath;
import org.codehaus.groovy.util.ListHashMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

public class GmailAPI {
    private static final String APPLICATION_NAME = "JobApplicationManager";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String USER_ID = "me";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    //private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
//    private static final String CREDENTIALS_FILE_PATH =
//            System.getProperty("user.dir") + "/server/src/main/resources/credentials/credentials.json";
//
//    private static final String TOKENS_DIRECTORY_PATH = System.getProperty("user.dir") +
//            File.separator + "server" +
//            File.separator + "src" +
//            File.separator + "main" +
//            File.separator + "resources" +
//            File.separator + "credentials";

    private static final String CREDENTIALS_FILE_PATH =
            System.getProperty("user.dir") + "/src/main/resources/credentials/credentials.json";

    private static final String TOKENS_DIRECTORY_PATH = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "main" +
            File.separator + "resources" +
            File.separator + "credentials";


    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(new File(CREDENTIALS_FILE_PATH));
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(5500).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public static Gmail getService() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }

    public static List<Message> listMessagesMatchingQuery(Gmail service, String userId,
                                                          String query) throws IOException {

        ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }

        }
        System.out.println("list length: " + messages.size());
        for (Message count : messages) {
            System.out.println(count);
        }

        return messages;
    }

    public static Message getMessage(Gmail service, String userId, List<Message> messages, int index)
            throws IOException {
        Message message = service.users().messages().get(userId, messages.get(index).getId()).execute();
//        System.out.println("Message to string: ");
//        System.out.println(message.toPrettyString());
//        System.out.println("------------------------------------");
        return message;
    }

    public static List<HashMap<String, String>> getGmailData(String query) {
        try {
            Gmail service = getService();
            List<Message> messages = listMessagesMatchingQuery(service, USER_ID, query);
            List<HashMap<String, String>> hmSet = new ArrayList<>();
            for (int index = 0; index < messages.size(); index++) {
                Message message = getMessage(service, USER_ID, messages, index); //get the first one
                //messages.set(index, message);
                JsonPath jp = new JsonPath(message.toString());
                String subject = jp.getString("payload.headers.find { it.name == 'Subject' }.value");
                String body = "";
                if (jp.getString("payload.parts[0].body.data") != null) {
                    body = new String(Base64.getDecoder().decode(jp.getString("payload.parts[0].body.data").replace('-', '+').replace('_', '/')));
                }


                String link = null;
                String arr[] = body.split("\n");
                for (String s : arr) {
                    s = s.trim();
                    if (s.startsWith("http") || s.startsWith("https")) {
                        link = s.trim();
                    }
                }

                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("subject", subject);
                hm.put("body", body);
                hm.put("link", link);

                hmSet.add(hm);


            }

            return hmSet;
        } catch (Exception e) {
            System.out.println("email not found....");
            throw new RuntimeException(e);
        }
    }

    public static int getTotalCountOfMails() {
        int size;
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            List<Thread> threads = service.
                    users().
                    threads().
                    list("me").
                    execute().
                    getThreads();
            size = threads.size();
        } catch (Exception e) {
            System.out.println("Exception log " + e);
            size = -1;
        }
        return size;
    }

    public static boolean isMailExist(String messageTitle) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            ListMessagesResponse response = service.
                    users().
                    messages().
                    list("me").
                    setQ("subject:" + messageTitle).
                    execute();
            List<Message> messages = getMessages(response);
            return messages.size() != 0;
        } catch (Exception e) {
            System.out.println("Exception log" + e);
            return false;
        }
    }

    private static List<Message> getMessages(ListMessagesResponse response) {
        List<Message> messages = new ArrayList<Message>();
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            while (response.getMessages() != null) {
                messages.addAll(response.getMessages());
                if (response.getNextPageToken() != null) {
                    String pageToken = response.getNextPageToken();
                    response = service.users().messages().list(USER_ID)
                            .setPageToken(pageToken).execute();
                } else {
                    break;
                }
            }
            return messages;
        } catch (Exception e) {
            System.out.println("Exception log " + e);
            return messages;
        }
    }


    public static void firebaseInit() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("/jobapplicationmanager.json").getInputStream()))
                    .setDatabaseUrl("https://jobapplicationmanager-6361b.firebaseio.com")
                    .build();
            if (FirebaseApp.getApps().isEmpty()) { //<--- check with this line
                FirebaseApp.initializeApp(options);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap getTasks(JobService jobService) throws FirebaseAuthException, ExecutionException, InterruptedException {
        firebaseInit(); //idk if we have to do this, but i get an error if we don't
        //jobService = new JobService();
        CoreNLPJobParser myParser = new CoreNLPJobParser();

        HashMap<String, Task> listOfTasks = new HashMap<String, Task>();

        for (Object job : jobService.getAllJobApps()) {
            String companyChecking = ((JobApp) job).getCompany();
            System.out.println("-------------------------" + companyChecking + "-------------------------");
            List<HashMap<String, String>> hmSet = getGmailData(companyChecking);
            for (HashMap hm : hmSet) {
                //System.out.println(hm.get("body"));
                try {
                    HashMap<String, String> result = myParser.parseEmail((String) hm.get("body"));
                    //System.out.println(result);
                    if (result.get("DATE") != null || result.get("INTERVIEW_TYPE") != null) { //need to create a new task

                        Task newTask = new Task(((JobApp) job).getJobID(), ((JobApp) job).getCompany(), "", "");

                        System.out.println("=========DATE=========");
                        if (result.get("DATE") == null) {
                            System.out.println("Not found.");
                        } else {
                            System.out.println(result.get("DATE"));
                            newTask.setTaskDate(result.get("DATE"));
                        }

                        System.out.println("=========INTERVIEW TYPE========");
                        if (result.get("INTERVIEW_TYPE") == null) {
                            System.out.println("Not found.");
                        } else {
                            System.out.println(result.get("INTERVIEW_TYPE"));
                            newTask.setTaskKeyword(result.get("INTERVIEW_TYPE"));
                        }

                        listOfTasks.put(newTask.getJobID(), newTask);

                    }
                }
                catch(Exception e){
                    System.out.println(e.toString()); continue;
                }
            }
        }
        return listOfTasks;


    }


    public static void main(String[] args) throws IOException, GeneralSecurityException, FirebaseAuthException, ExecutionException, InterruptedException {
        //here you choose what to search your gmail for. We want to search for specific companies after a certain day (since the last time i opened app..?)
        //List<HashMap<String, String>> hmSet = getGmailData("from:firebase-noreply@google.com");
        firebaseInit(); //idk if we have to do this, but i get an error if we don't
        JobService jobService = new JobService();
        CoreNLPJobParser myParser = new CoreNLPJobParser();

        HashMap<String, Task> listOfTasks = new HashMap<String, Task>();

        for (Object job : jobService.getAllJobApps()) {
            try{
                String companyChecking = ((JobApp) job).getCompany();
                System.out.println("-------------------------" + companyChecking + "-------------------------");
                List<HashMap<String, String>> hmSet = getGmailData(companyChecking);
                for (HashMap hm : hmSet) {
                    //System.out.println(hm.get("body"));
                    HashMap<String, String> result = myParser.parseEmail((String) hm.get("body"));
                    //System.out.println(result);
                    if (result.get("DATE") != null || result.get("INTERVIEW_TYPE") != null) { //need to create a new task

                        Task newTask = new Task(((JobApp) job).getJobID(), ((JobApp) job).getCompany(), "", "");

                        System.out.println("=========DATE=========");
                        if (result.get("DATE") == null) {
                            System.out.println("Not found.");
                        } else {
                            System.out.println(result.get("DATE"));
                            newTask.setTaskDate(result.get("DATE"));
                        }

                        System.out.println("=========INTERVIEW TYPE========");
                        if (result.get("INTERVIEW_TYPE") == null) {
                            System.out.println("Not found.");
                        } else {
                            System.out.println(result.get("INTERVIEW_TYPE"));
                            newTask.setTaskKeyword(result.get("INTERVIEW_TYPE"));
                        }

                        listOfTasks.put(newTask.getJobID(), newTask);

                    }

                }
            }catch (Exception e){
                System.out.println("Fail"); continue;
            }

        }
    }

}