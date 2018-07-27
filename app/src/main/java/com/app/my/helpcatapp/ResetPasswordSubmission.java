package com.app.my.helpcatapp;

// ResetPasswordSubmission class
//  - ID = user's login ID
//  - status = status for the reset approval
public class ResetPasswordSubmission extends Object {
    private String ID;
    private int status;

    public ResetPasswordSubmission() {}

    public ResetPasswordSubmission(String ID, int status) {
        this.ID = ID;
        this.status = status;
    }

    public String getID() {
        return ID;
    }

    public int getStatus() {
        return status;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}