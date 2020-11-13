package com.potatosantaa.server.services;

import java.io.*;
import java.util.*;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;
import java.util.stream.Collectors;

public class CoreNLPJobParser {
    private Properties props;
    private StanfordCoreNLP pipeline;

    public CoreNLPJobParser() {
        // Setup StanfordCoreNLP properties
        props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,regexner");
        final String REGEX_PATH = System.getProperty("user.dir") +
            //File.separator + "server" +
            File.separator + "src" +
            File.separator + "main" +
            File.separator + "resources" +
            File.separator + "job_keywords_regex.txt";
        props.put("regexner.mapping", REGEX_PATH);
        props.setProperty("ner.fine.regexner.mapping", REGEX_PATH);

        // Setup & Init StanfordCoreNLP
        pipeline = new StanfordCoreNLP(props);
    }

    public HashMap<String, String> parseEmail(String emailBody) {
        // make an example document
        CoreDocument doc = new CoreDocument(emailBody);

        // annotate the document
        pipeline.annotate(doc);

        HashMap<String, String> result = new HashMap<String, String>();
        HashMap<String, Double> probMap = new HashMap<String, Double>();
        HashMap<String, Integer> countMap = new HashMap<String, Integer>();
        HashMap<String, ArrayList<String>> tempMap = new HashMap<String, ArrayList<String>>();

        tempMap.put("JOB_TITLE", new ArrayList<String>());
        tempMap.put("JOB_TYPE", new ArrayList<String>());
        tempMap.put("INTERVIEW_TYPE", new ArrayList<String>());
        tempMap.put("ORGANIZATION", new ArrayList<String>());
        tempMap.put("LOCATION", new ArrayList<String>());
        tempMap.put("DATE", new ArrayList<String>());
        tempMap.put("TIME", new ArrayList<String>());

        // process results
        for (CoreEntityMention em : doc.entityMentions()) {
            try {
                if(tempMap.containsKey(em.entityType())){
                    tempMap.get(em.entityType()).add(em.text());

                    // Count appearance number of em.text()
                    if(countMap.containsKey(em.text())) {
                        countMap.replace(em.text(), countMap.get(em.text()) + 1);
                    }
                    else {
                        countMap.put(em.text(), 1);
                    }

                    // Map em.text() to its probability
                    probMap.put(em.text(), (Double) em.entityTypeConfidences().values().toArray()[0]);

                    // process DATE
                    if(em.entityType().equals("DATE")) {
                        if(!result.containsKey("DATE")){
                            result.put("DATE", em.sentence().text());
                        } else {
                            if(!result.get("DATE").contains(em.sentence().text())){
                                result.put("DATE", result.get("DATE") + "\n" + em.sentence().text());
                            }
                        }
                    }

                    // process LOCATION
                    if(em.entityType().equals("LOCATION")) {
                        if(!result.containsKey("LOCATION")){
                            result.put("LOCATION", em.sentence().text());
                        } else {
                            if(!result.get("LOCATION").contains(em.sentence().text())){
                                result.put("LOCATION", result.get("LOCATION") + "\n" + em.sentence().text());
                            }
                        }
                    }

                    // process TIME
                    if(em.entityType().equals("TIME")) {
                        if(!result.containsKey("TIME")){
                            result.put("TIME", em.sentence().text());
                        } else {
                            if(!result.get("TIME").contains(em.sentence().text())){
                                result.put("TIME", result.get("TIME") + "\n" + em.sentence().text());
                            }
                        }
                    }
                }
            }
            catch (Exception e){
                System.out.println(e.toString()); continue;
            }

            // System.out.println("\tdetected entity: \t" + em.text() + "\t" + em.entityType());
            // System.out.println("\tprobability: \t" + em.entityTypeConfidences().values().toArray()[0]);


        }

        // process DATE
        if (result.get("DATE") != null){
            result.put("DATE",result.get("DATE").replaceAll("(?m)(^\\s+|[\\s&&[^\\n]](?=\\s|$)|\\s+\\z)", ""));
        }


        // process LOCATION
        //result.put("LOCATION",result.get("LOCATION").replaceAll("(?m)(^\\s+|[\\s&&[^\\n]](?=\\s|$)|\\s+\\z)", ""));

        // process TIME
        //result.put("LOCATION",result.get("LOCATION").replaceAll("(?m)(^\\s+|[\\s&&[^\\n]](?=\\s|$)|\\s+\\z)", ""));

        // process ORGANIZATION
        int tempCount = 0;
        double tempProb = 0;
        for(String s : tempMap.get("ORGANIZATION")) {
            /* LOGIC:
             *      - a string mentioned the most in email => important
             *      - prob > 50%
             * 
             */
            if(countMap.get(s) >= tempCount && probMap.get(s) > 0.50 && probMap.get(s) >= tempProb) {                
                tempProb = probMap.get(s);
                tempCount = countMap.get(s);
                result.put("ORGANIZATION", s); 
            }
        }

        // process JOB_TITLE
        if(tempMap.get("JOB_TITLE").size() == 1){
            result.put("JOB_TITLE", tempMap.get("JOB_TITLE").get(0)); 
        } else{         // This case should not happen since each job email should only have 1 job position
            for(String s : tempMap.get("JOB_TITLE")) {
                if(countMap.get(s) >= tempCount && probMap.get(s) > 0.50 && probMap.get(s) >= tempProb) {                
                    tempProb = probMap.get(s);
                    tempCount = countMap.get(s);
                    result.put("JOB_TITLE", s); 
                }
            }
        }

        // process JOB_TYPE
        if(tempMap.get("JOB_TYPE").size() == 1){
            result.put("JOB_TYPE", tempMap.get("JOB_TYPE").get(0)); 
        } else {        // This case should not happen since each job position is either Full Time/Part Time/Intern
            for(String s : tempMap.get("JOB_TYPE")) {
                if(countMap.get(s) >= tempCount && probMap.get(s) > 0.50 && probMap.get(s) >= tempProb) {                
                    tempProb = probMap.get(s);
                    tempCount = countMap.get(s);
                    result.put("JOB_TYPE", s); 
                }
            }
        }

        // process INTERVIEW_TYPE
        if(tempMap.get("INTERVIEW_TYPE").size() == 1){
            result.put("INTERVIEW_TYPE", tempMap.get("INTERVIEW_TYPE").get(0)); 
        } else {        // This case should not happen since each job email should discuss about each interview round
            for(String s : tempMap.get("INTERVIEW_TYPE")) {
                if(countMap.get(s) >= tempCount && probMap.get(s) > 0.50 && probMap.get(s) >= tempProb) {                
                    tempProb = probMap.get(s);
                    tempCount = countMap.get(s);
                    result.put("INTERVIEW_TYPE", s); 
                }
            }
        }

        return result;
    }


    public static void main(String[] args) {
        // Testing functions => Will be delete before deployment
        CoreNLPJobParser myJobParser = new CoreNLPJobParser();

        // Change the value of the string to test the function parseEmail()
        String str =
        "Thanks for connecting with me through Handshake! It’s awesome to see students like you who are preparing for exciting careers in tech. We are looking for product developers that want to solve complex problems and deliver meaningful innovation to our customers.\n\n"+
        "If you are ready to complete the application for one of our 2020 Software Engineer Intern roles, get started by completing a technical challenge with our partners at Karat.\n\n"+
        "TAKE THE CODING CHALLENGE NOW\n\n"+
        "To ensure consideration, please complete the challenge no later than November 26, 2019. The link above is specifically for you, so please don’t share with anyone else.\n\n"+
        "Feel free to reach out with any questions, and In the meantime, learn more about Intuit below!\n"+
        "_________________________________________________________________________________\n\n"+
        "At Intuit, we build intuitive web, mobile and cloud solutions that enable 50 million consumers and small businesses around the world to take charge of their money and do more of what they love.\n\n"+
        "What sets Intuit apart? The right combination of meaningful mission, innovation & technology, and a great place to work culture.\n\n"+        
        "\tNo surprise here! Our internship program was named one of WayUp's 2019 Top 100 Internship programs.\n"+
        "\tSix 2019 Intuit interns share reflections on what makes for a rewarding internship.\n"+
        "\tOur early career program is best in class! Interns and new college grads have an impact from day one - jumping in as full members of their team and working on real projects. Just watch.\n"+
        "\tIntuit’s culture & values make us who we are - they’ve allowed us to thrive for decades as we work on behalf of our 50 million customers.\n\n"+
        "Learn more on our early careers page or find us on Handshake.\n\n"+ 
        "Cheers,\n\n";

        HashMap<String, String> result = myJobParser.parseEmail(str);

        System.out.println("Result: " + result);
        System.out.println();
        System.out.println(result.get("DATE"));
    }
}