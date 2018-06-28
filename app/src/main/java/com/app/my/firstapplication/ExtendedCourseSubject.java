package com.app.my.firstapplication;

import java.util.ArrayList;

public class ExtendedCourseSubject extends CourseSubject {
    private String lecturerID;
    private ArrayList<SubjectClass> classTime = new ArrayList<>();

    public ExtendedCourseSubject() {}

    public ExtendedCourseSubject(String subjectCode, String subjectTitle, String lecturerID, ArrayList<SubjectClass> classTime) {
        super(subjectCode, subjectTitle);
        this.lecturerID = lecturerID;
        this.classTime = classTime;
    }

    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }

    public void setClassTime(ArrayList<SubjectClass> classTime) {
        this.classTime = classTime;
    }

    public String getLecturerID() {
        return lecturerID;
    }

    public ArrayList<SubjectClass> getClassTime() {
        return classTime;
    }

    public int numberOfClasses() {
        return classTime.size();
    }

    public ArrayList<SubjectClass> classList() {
        return this.classTime;
    }

    public SubjectClass getClass(int index) {
        return classTime.get(index);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}