package com.potatosantaa.server.services;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

@Service
public class FireBaseInit {
    @PostConstruct
    public void initialize(){
        try{
            System.out.println(this.getClass().getResourceAsStream("/jobapplicationmanager.json"));
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(this.getClass().getResourceAsStream("/jobapplicationmanager.json")))
                .setDatabaseUrl("https://jobapplicationmanager-6361b.firebaseio.com")
                .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}