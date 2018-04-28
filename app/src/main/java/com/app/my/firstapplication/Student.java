package com.app.my.firstapplication;

public class Student extends Object {
    private String studentName;
    private String studentID;
    private String studentPassword;

    public Student() {}

    public Student(String studentName, String studentID, String studentPasword) {
        this.studentName = studentName;
        this.studentID = studentID;
        this.studentPassword = studentPasword;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    @Override
    public String toString() {
        return ("Student Name : " + studentName + "\nStudentID : " + studentID);
    }
}