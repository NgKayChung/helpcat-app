package com.app.my.helpcatapp;

import java.util.ArrayList;

// Student class
//  - enrolledSubjects = list of subjects enrolled for the semester
//  - enrolledProgramme = name of student registered programme
//  - intake = intake month and year
public class Student extends User {
    private ArrayList<CourseSubject> enrolledSubjects;
    private String enrolledProgramme;
    private String intake;

    public Student() {}

    public Student(String ID, String emailAddress, String fullname, String password, String nric_passport_no, String nationality, String gender, String phoneNumber, String livingAddress, String enrolledProgramme, String intake) {
        super(ID, emailAddress, fullname, password, nric_passport_no, nationality, gender, phoneNumber, livingAddress);
        this.enrolledProgramme = enrolledProgramme;
        this.intake = intake;
        this.enrolledSubjects = null;
    }

    public Student(String ID, String emailAddress, String fullname, String password, String nric_passport_no, String nationality, String gender, String phoneNumber, String livingAddress, String enrolledProgramme, String intake, ArrayList<CourseSubject> enrolledSubjects) {
        super(ID, emailAddress, fullname, password, nric_passport_no, nationality, gender, phoneNumber, livingAddress);
        this.enrolledProgramme = enrolledProgramme;
        this.intake = intake;
        this.enrolledSubjects = enrolledSubjects;
    }

    public String getEnrolledProgramme() {
        return enrolledProgramme;
    }

    public String getIntake() {
        return intake;
    }

    public int numberOfSubjectsEnrolled() {
        return (enrolledSubjects == null ? 0 : enrolledSubjects.size());
    }

    public ArrayList<CourseSubject> getEnrolledSubjects() {
        return enrolledSubjects;
    }
}