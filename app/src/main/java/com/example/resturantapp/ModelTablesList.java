package com.example.resturantapp;

public class ModelTablesList {
    private String name;
    private String custId;
    private String waitId;
    private String status;

    public ModelTablesList(String name,String custId,String waitId,String status) {
        this.name = name;
        this.custId = custId;
        this.waitId= waitId;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustId() {
        return this.custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getWaitId() {
        return this.waitId;
    }

    public void setWaitId(String waitId) {
        this.waitId = waitId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

