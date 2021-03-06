package com.app.my.helpcatapp;

// Message class
// for retrieving messages submitted by lecturers
public class Message extends Object {
    private String content;
    private long postTimeMillis;
    private Lecturer lecturer;
    private CourseSubject subject;

    public Message() {}

    public Message(String content, long postTimeMillis, String lecturerID, String lecturerName, String subjectCode, String subjectTitle) {
        this.content = content;
        this.postTimeMillis = postTimeMillis;
        this.lecturer = new Lecturer(lecturerID, lecturerName);
        this.subject = new CourseSubject(subjectCode, subjectTitle);
    }

    public Message(String content, long postTimeMillis, Lecturer lecturer, CourseSubject subject) {
        this.content = content;
        this.postTimeMillis = postTimeMillis;
        this.lecturer = lecturer;
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public long getPostTimeMillis() {
        return postTimeMillis;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public CourseSubject getSubject() {
        return subject;
    }
}