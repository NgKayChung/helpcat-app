package com.app.my.helpcatapp;

public class SubjectClass extends Object {
    private String day;
    private String startTime;
    private String endTime;
    private String venue;

    public SubjectClass() {}

    public SubjectClass(String day, String startTime, String endTime, String venue) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDateTime() {
        return day + ", " + startTime + " - " + endTime;
    }

    public String getVenue() {
        return venue;
    }
}