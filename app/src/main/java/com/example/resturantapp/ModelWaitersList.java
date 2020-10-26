package com.example.resturantapp;

public class ModelWaitersList {
    private String name;
    private String email;
    private String phone;
    private String id;
    private String aadhaar;
    private String pass;
    //private String pan;
    public ModelWaitersList(String name, String email, String phone,String id,String aadhaar,String pass) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.aadhaar = aadhaar;
        this.pass = pass;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAadhaar() {
        return this.aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    /*public String getPan() {
        return this.pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }*/
}

