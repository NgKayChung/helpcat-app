package com.app.my.firstapplication;

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