package com.example.resturantapp;

public class ModelCategorywiseItem {
    private String category;
    private String cost;
    private String itemId;
    private String name;
    public ModelCategorywiseItem(){

    }
    public ModelCategorywiseItem(String name,String category,String cost,String itemId) {
        this.name = name;
        this.category = category;
        this.itemId= itemId;
        this.cost = cost;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCost() {
        return this.cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
