package com.potatosantaa.server.controllers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/home")
    public String getHome() {
        return "Testing controller";
    }

    @GetMapping("/indeed")
    public List<ArrayList<String>> getSoftwareJobs() throws IOException {
        // web scrapes software engineering jobs from Indeed using jsoup, by Stephen
        // Tayag
        Document doc = Jsoup.connect("https://www.indeed.com/q-Software-Engineer-jobs.html").get();
        System.out.println((doc.title()));

        Elements newsHeadlines = doc.select("#resultsCol div h2 a");

        List<ArrayList<String>> softwareJobs = new ArrayList<>();

        for (Element headline : newsHeadlines) {
            // softwareJobs.add(headline.attr("title") + ": " + headline.absUrl("href"));
            softwareJobs.add(new ArrayList<>() {
                {
                    add(headline.attr("title"));
                    add(headline.absUrl("href"));
                }
            });

            System.out.println((headline.attr("title") + ": " + headline.absUrl("href")));

        }
        System.out.println(softwareJobs);
        return softwareJobs;
    }

}