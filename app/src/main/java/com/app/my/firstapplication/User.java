package com.app.my.firstapplication;

public class User extends Object {
    private String ID;
    private String fullname;
    private String password;
    private String loginType;

    public User(){}

    public User(String ID, String fullname, String password) {
        this.ID = ID;
        this.fullname = fullname;
        this.password = password;
    }

    public String getID() {
        return this.ID;
    }

    public String getFullname() {
        return this.fullname;
    }

    public String getPassword() {
        return this.password;
    }

    public String getLoginType() {
        return this.loginType;
    }

    public void determineLogin() {
        if(ID.startsWith("C"))
            loginType = "student";
        else if(ID.startsWith("I"))
            loginType = "lecturer";
        else
            loginType = ID;
    }

    @Override
    public String toString() {
        return ID + " " + fullname + " " + password + " " + loginType;
    }
}