package com.med_presc_pat.GetSet;

/**
 * Created by ABHISHEK on 5/19/2016.
 */
public class PatientRegistrationGetSet {
    String Name,Dob,Email,mobile,Address,State,Dtsrict;
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDtsrict() {
        return Dtsrict;
    }

    public void setDtsrict(String dtsrict) {
        Dtsrict = dtsrict;
    }

    }
