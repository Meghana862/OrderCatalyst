package com.example.resturantapp;

public class ModelSessionsList {
    private String date;
    private String cust;
    private String waitId;

    public ModelSessionsList(String date,String cust,String waitId) {
        this.date = date;
        this.cust = cust;
        this.waitId= waitId;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCust() {
        return this.cust;
    }

    public void setCust(String cust) {
        this.cust = cust;
    }

    public String getWaitId() {
        return this.waitId;
    }

    public void setWaitId(String waitId) {
        this.waitId = waitId;
    }

}

