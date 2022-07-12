package com.example.prorderemptyproject.Abstract;

import com.example.prorderemptyproject.Models.Child;

import java.util.UUID;

public abstract class AbstractAccount {

    private String id;
    private String name = "";

    public String getId() {
        return id;
    }

    public void setId(){
        this.id = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}


