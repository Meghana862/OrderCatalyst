package com.example.resturantapp;

import java.util.ArrayList;

public class WaitersList {
    private static WaitersList instance;
    final ArrayList<ModelWaitersList> waiters = new ArrayList<>();

    private WaitersList() {
    }

    static WaitersList getInstance() {
        if (instance == null) {
            instance = new WaitersList();
        }
        return instance;
    }
}

