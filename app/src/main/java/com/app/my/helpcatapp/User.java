package com.app.my.helpcatapp;

import com.google.firebase.database.PropertyName;

// User class
//  - ID = ID/login ID for the user
//  - emailAddress
//  - fullname
//  - password = login password
//  - nric_passport_no
//  - nationality
//  - gender
//  - phoneNumber
//  - livingAddress
//  - userType = to store user's login type (Student / Lecturer / Admin)
public class User extends Object {
    private String ID;
    private String emailAddress;
    private String fullname;
    private String password;
    private String nric_passport_no;
    private String nationality;
    private String gender;
    private String phoneNumber;
    private String livingAddress;
    private String userType;

    public User(){}

    public User(String ID, String emailAddress, String fullname, String password, String nric_passport_no, String nationality, String gender, String phoneNumber, String livingAddress) {
        this.ID = ID;
        this.emailAddress = emailAddress;
        this.fullname = fullname;
        this.password = password;
        this.nric_passport_no = nric_passport_no;
        this.nationality = nationality;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.livingAddress = livingAddress;
    }

    @PropertyName("ID")
    public String getID() {
        return this.ID;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getFullname() {
        return this.fullname;
    }

    public String getPassword() {
        return this.password;
    }

    public String getNric_passport_no() {
        return nric_passport_no;
    }

    public String getNationality() {
        return nationality;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLivingAddress() {
        return livingAddress;
    }

    public String getUserLoginType() {
        return this.userType;
    }

    @PropertyName("ID")
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNric_passport_no(String nric_passport_no) {
        this.nric_passport_no = nric_passport_no;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLivingAddress(String livingAddress) {
        this.livingAddress = livingAddress;
    }

    public void determineUserType() {
        if(ID.startsWith("C"))
            userType = "student";
        else if(ID.startsWith("I"))
            userType = "lecturer";
        else
            userType = ID;
    }
}