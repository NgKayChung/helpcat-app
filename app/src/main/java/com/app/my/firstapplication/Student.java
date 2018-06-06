package com.app.my.firstapplication;

import java.util.ArrayList;

public class Student extends User {
    private ArrayList<CourseSubject> subjects;

    public Student() {}

    public Student(String ID, String emailAddress, String fullname, String password) {
        super(ID, emailAddress, fullname, password);
        this.subjects = null;
    }

    public Student(String ID, String emailAddress, String fullname, String password, ArrayList<CourseSubject> subjects) {
        super(ID, emailAddress, fullname, password);
        this.subjects = subjects;
    }

    public String getStudentID() {
        return super.getID();
    }

    public String getStudentEmailAddress() {
        return super.getEmailAddress();
    }

    public String getStudentName() {
        return super.getFullname();
    }

    public String getStudentPassword() {
        return super.getPassword();
    }

    public int getSubjectNumber() {
        return (subjects == null ? 0 : subjects.size());
    }

    public ArrayList<CourseSubject> getSubjects() {
        return subjects;
    }

    @Override
    public String toString() {
        String retString = "Student ID : " + super.getID() + "\nEmail Address : " + super.getEmailAddress() + "\nStudent Name : " + super.getFullname() + "\n";

        for (CourseSubject subject : subjects) {
            retString += subject + "\n";
        }
        retString += "Num of Subjects : " + subjects.size();
        return retString;
    }
}