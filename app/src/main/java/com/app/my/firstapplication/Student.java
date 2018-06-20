package com.app.my.firstapplication;

import java.util.ArrayList;

public class Student extends User {
    private ArrayList<CourseSubject> enrolledSubjects;

    public Student() {}

    public Student(String ID, String emailAddress, String fullname, String password) {
        super(ID, emailAddress, fullname, password);
        this.enrolledSubjects = null;
    }

    public Student(String ID, String emailAddress, String fullname, String password, ArrayList<CourseSubject> enrolledSubjects) {
        super(ID, emailAddress, fullname, password);
        this.enrolledSubjects = enrolledSubjects;
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
        return (enrolledSubjects == null ? 0 : enrolledSubjects.size());
    }

    public ArrayList<CourseSubject> getEnrolledSubjects() {
        return enrolledSubjects;
    }

    @Override
    public String toString() {
        String retString = "Student ID : " + super.getID() + "\nEmail Address : " + super.getEmailAddress() + "\nStudent Name : " + super.getFullname() + "\n";

        for (CourseSubject subject : enrolledSubjects) {
            retString += subject + "\n";
        }
        retString += "Num of Subjects : " + enrolledSubjects.size();
        return retString;
    }
}