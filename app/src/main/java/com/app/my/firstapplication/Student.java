package com.app.my.firstapplication;

public class Student extends User {
    private int subjectNumber;

    public Student() {}

    public Student(String ID, String fullname, String password, int subjectNumber) {
        super(ID, fullname, password);
        this.subjectNumber = subjectNumber;
    }

    public String getStudentName() {
        return super.getFullname();
    }

    public String getStudentID() {
        return super.getID();
    }

    public String getStudentPassword() {
        return super.getPassword();
    }

    public int getSubjectNumber() {
        return this.subjectNumber;
    }

    @Override
    public String toString() {
        return ("Student Name : " + super.getFullname() + "\nStudentID : " + super.getID() + "\nNum of Subjects : " + this.subjectNumber);
    }
}