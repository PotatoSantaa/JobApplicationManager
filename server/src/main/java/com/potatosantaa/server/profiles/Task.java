package com.potatosantaa.server.profiles;

public class Task {
    private String jobID;
    private String company;
    private String taskDate;
    private String taskKeyword;

    public Task(String jobID, String company, String taskDate, String taskKeyword){
        this.jobID = jobID;
        this.company = company;
        if (taskDate == null){
            System.out.println("NO TASK DATE");
            this.taskDate = "";
        }
        else{
            this.taskDate = taskDate;
        }
        if (taskDate == null){
            System.out.println("NO TASK KEYWORDS");
            this.taskKeyword = "";
        }
        else{
            this.taskKeyword = taskKeyword;
        }

    }

    public Task(){
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskKeyword() {
        return taskKeyword;
    }

    public void setTaskKeyword(String taskKeyword) {
        this.taskKeyword = taskKeyword;
    }
}
