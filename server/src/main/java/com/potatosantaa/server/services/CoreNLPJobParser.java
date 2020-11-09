package com.potatosantaa.server.services;

import java.io.*;
import java.util.*;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;
import java.util.stream.Collectors;

public class CoreNLPJobParser {
    private HashMap<String, Integer> map;

    public CoreNLPJobParser() {
        map = new HashMap<String, Integer>();
        map.put("TITLE", 0);
        map.put("ORGANIZATION", 1);
        map.put("LOCATION", 2);
        map.put("DATE", 3);
        map.put("TIME", 4);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // String dir = System.getProperty("user.dir");
        // String textDir = dir + "/server/src/main/resources/sample_english.txt";
        // String propDir = dir + "/server/src/main/resources/english.properties";

        // String[] englishArgs = new String[]{"-file", textDir, "-outputFormat",
        // "text", "-props", propDir};
        // StanfordCoreNLP.main(englishArgs);

        String str = "Hi Long,\n\nThanks for connecting with me through Handshake! It’s awesome to see students like you who are preparing for exciting careers in tech. We are looking for product developers that want to solve complex problems and deliver meaningful innovation to our customers."
                + "\n\nIf you are ready to complete the application for one of our 2020 Software Engineer Intern roles, get started by completing a technical challenge with our partners at Karat."
                + "\n\nTAKE THE CODING CHALLENGE NOW\n\n"
                + "To ensure consideration, please complete the challenge no later than November 26, 2019. The link above is specifically for you, so please don’t share with anyone else."
                + "\n\nFeel free to reach out with any questions, and In the meantime, learn more about Intuit below!\n\n"
                + "_________________________________________________________________________________\n\n"
                + "At Intuit, we build intuitive web, mobile and cloud solutions that enable 50 million consumers and small businesses around the world to take charge of their money and do more of what they love."
                + "\n\nWhat sets Intuit apart? The right combination of meaningful mission, innovation & technology, and a great place to work culture."
                + "No surprise here! Our internship program was named one of WayUp's 2019 Top 100 Internship programs.\n"
                + "Six 2019 Intuit interns share reflections on what makes for a rewarding internship.\n"
                + "Our early career program is best in class! Interns and new college grads have an impact from day one - jumping in as full members of their team and working on real projects. Just watch.\n"
                + "Intuit’s culture & values make us who we are - they’ve allowed us to thrive for decades as we work on behalf of our 50 million customers.\n\n"
                + "Learn more on our early careers page or find us on Handshake.\n\n" + "Cheers,\n\n" + "Gabby Woody\n"
                + "Diversity Recruiting Lead\n" + "gabrielle_woody@intuit.com\n" + "650.564.2114\n" + "LinkedIn";

        CoreNLPJobParser myParser = new CoreNLPJobParser();
        HashMap<String, String> result = myParser.parseEmail(str);

        System.out.println(result);


        // Properties props = new Properties();
        // props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");

        // // set up pipeline
        // StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // // make an example document
        // CoreDocument doc = new CoreDocument(str);

        // // annotate the document
        // pipeline.annotate(doc);

        // // view results
        // System.out.println("---");
        // System.out.println("entities found");
        // for (CoreEntityMention em : doc.entityMentions()) {
        //     System.out.println("\tdetected entity: \t" + em.text() + "\t" + em.entityType());
        //     System.out.println("\tprobability: \t" + em.entityTypeConfidences().values().toArray()[0]);
        // }
    }

    public HashMap<String, String> parseEmail(String emailBody) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");

        // set up pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // make an example document
        CoreDocument doc = new CoreDocument(emailBody);

        // annotate the document
        pipeline.annotate(doc);

        HashMap<String, String> result = new HashMap<String, String>();
        HashMap<String, Double> probMap = new HashMap<String, Double>();
        HashMap<String, Integer> countMap = new HashMap<String, Integer>();
        HashMap<String, ArrayList<String>> tempMap = new HashMap<String, ArrayList<String>>();

        tempMap.put("TITLE", new ArrayList<String>());
        tempMap.put("ORGANIZATION", new ArrayList<String>());
        tempMap.put("LOCATION", new ArrayList<String>());
        tempMap.put("DATE", new ArrayList<String>());
        tempMap.put("TIME", new ArrayList<String>());


        // process results
        for (CoreEntityMention em : doc.entityMentions()) {
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
            }   
        }

        // process TITLE
        int tempCount = 0;
        double tempProb = 0;
        for(String s : tempMap.get("TITLE")) {
            if(countMap.get(s) >= tempCount && probMap.get(s) > 0.50) {    
                if(s.length() - s.replaceAll(" ", "").length() > 1){
                    tempProb = probMap.get(s);
                    tempCount = countMap.get(s);
                    result.put("TITLE", s); 
                }      
            }
        }

        // process ORGANIZATION
        tempCount = 0;
        tempProb = 0;
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

        // process LOCATION

        // process DATE
        
        // process TIME

        return result;
    }
}