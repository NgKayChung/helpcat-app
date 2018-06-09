package com.app.my.firstapplication;

public class CourseSubject extends Object {
    private String subjectCode;
    private String subjectTitle;

    public CourseSubject(){}

    public CourseSubject(String subjectCode, String subjectTitle) {
        this.subjectCode = subjectCode;
        this.subjectTitle = subjectTitle;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectCode() {
        return this.subjectCode;
    }

    public String getSubjectTitle() {
        return this.subjectTitle;
    }

    @Override
    public String toString() {
        return this.subjectCode + " " + this.subjectTitle;
    }
}