package com.app.my.firstapplication;

import java.util.ArrayList;

public class SubjectEnrollment extends Object {
    private String studentID;
    private ArrayList<CourseSubject> subjectList;
    private boolean submitted;
    private int status;

    public SubjectEnrollment(){
        subjectList = new ArrayList<CourseSubject>();
        status = 50;
        submitted = false;
    }

    public SubjectEnrollment(String studentID, ArrayList<CourseSubject> subjectList) {
        this.studentID = studentID;
        this.subjectList = subjectList;
        status = 50;
        submitted = false;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setSubjectList(ArrayList<CourseSubject> subjectList) {
        this.subjectList = subjectList;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void addSubject(CourseSubject subject) {
        this.subjectList.add(subject);
    }

    public void removeSubject(CourseSubject subject) {
        this.subjectList.remove(subject);
    }

    public String getStudentID() {
        return studentID;
    }

    public ArrayList<CourseSubject> getSubjectList() {
        return subjectList;
    }

    public int numberOfSubject() {
        return (subjectList == null ? 0 : subjectList.size());
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}