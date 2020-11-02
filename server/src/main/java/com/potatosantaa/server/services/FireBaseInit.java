package com.potatosantaa.server.services;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class FireBaseInit {
    @PostConstruct
    public void initialize(){
        try{
            FileInputStream serviceAccount;

            if(System.getProperty("user.dir").contains("server")){
                serviceAccount = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/jobapplicationmanager.json");

            }
            else {
                serviceAccount = new FileInputStream(System.getProperty("user.dir") + "/server/src/main/resources/jobapplicationmanager.json");
            }
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://jobapplicationmanager-6361b.firebaseio.com")
                .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}