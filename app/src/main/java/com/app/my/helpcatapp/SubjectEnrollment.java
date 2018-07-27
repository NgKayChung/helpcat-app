package com.app.my.helpcatapp;

import java.util.ArrayList;

// SubjectEnrollment class
//  - studentID = ID of the student
//  - subjectList = list of the subjects in enrollment
//  - submitted = indication for enrollment submitted
//  - status = status for the enrollment, success/failure
//  - remarks = message for the enrollment
public class SubjectEnrollment extends Object {
    private String studentID;
    private ArrayList<CourseSubject> subjectList;
    private boolean submitted;
    private int status;
    private String remarks;

    public SubjectEnrollment() {
        subjectList = new ArrayList<CourseSubject>();
        status = 100;
        submitted = false;
        remarks = "";
    }

    public SubjectEnrollment(String studentID, ArrayList<CourseSubject> subjectList) {
        this.studentID = studentID;
        this.subjectList = subjectList;
        status = 100;
        submitted = false;
        remarks = "";
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

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getRemarks() {
        return remarks;
    }
}