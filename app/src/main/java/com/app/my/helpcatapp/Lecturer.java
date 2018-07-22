package com.app.my.helpcatapp;

// Lecturer class
//  - lecturerID = lecturer ID
//  - lecturerName = lecturer name
public class Lecturer extends Object {
    private String lecturerID;
    private String lecturerName;

    public Lecturer() {}

    public Lecturer(String lecturerID, String lecturerName) {
        this.lecturerID = lecturerID;
        this.lecturerName = lecturerName;
    }

    public String getLecturerID() {
        return lecturerID;
    }

    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }
}
