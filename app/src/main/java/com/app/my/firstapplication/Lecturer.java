package com.app.my.firstapplication;

public class Lecturer extends Object {
    private String lecturerID;
    private String lecturerName;
    private String lecturerPassword;

    public Lecturer() {}

    public Lecturer(String lecturerID, String lecturerName, String lecturerPassword) {
        this.lecturerID = lecturerID;
        this.lecturerName = lecturerName;
        this.lecturerPassword = lecturerPassword;
    }

    public String getLecturerID() {
        return lecturerID;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public String getLecturerPassword() {
        return lecturerPassword;
    }

    @Override
    public String toString() {
        return ("Lecturer Name : " + lecturerName + "\nLecturer ID : " + lecturerID);
    }
}
