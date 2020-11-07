package com.example.tasknotification;

class Task {
    private String task;
    private String time;

    private Task(){}

    private Task(String task,String time){
        this.task = task;
        this.time = time;
    }
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
