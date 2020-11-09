package com.potatosantaa.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.io.IOException;

@Configuration
@EnableScheduling
@SpringBootApplication
public class ServerApplication {	

	public static void main(String[] args) throws Exception{
		SpringApplication.run(ServerApplication.class, args);
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(new ClassPathResource("/jobapplicationmanager.json").getInputStream()))
					.setDatabaseUrl("https://jobapplicationmanager-6361b.firebaseio.com")
					.build();
			if(FirebaseApp.getApps().isEmpty()) { //<--- check with this line
				FirebaseApp.initializeApp(options);
			}
		} catch (
				IOException e) {
			e.printStackTrace();
		}
	}
}
