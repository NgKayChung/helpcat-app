package com.app.my.helpcatapp;

// CourseSubject class
//  - subjectCode = subject ID
//  - subjectTitle = subject name
public class CourseSubject extends Object {
    private String subjectCode;
    private String subjectTitle;

    public CourseSubject() {}

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
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof CourseSubject)) {
            return false;
        }

        CourseSubject otherSubject = (CourseSubject) otherObject;

        return otherSubject.getSubjectCode().equals(getSubjectCode());
    }

    @Override
    public String toString() {
        return this.subjectCode + " " + this.subjectTitle;
    }
}