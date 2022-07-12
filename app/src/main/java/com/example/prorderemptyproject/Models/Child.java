package com.example.prorderemptyproject.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Child implements Serializable {

    private HashMap<String, Order> childOrders = new HashMap<>();

    static private Child currentChild;

    private static final String MALE = "Male";
    private static final String FEMALE = "Female";
    private static final String WITHOUT = "Without";
    private static final boolean YES_EXPERIENCE = true;
    private static final boolean NO_EXPERIENCE = false;

    private String id;
    private String name = "";
    private String gender = "";
    private String age = "";
    private boolean hasExperience = false;
    private String littleExplain = "None";
    //todo - ask if important
    /*private String explain = "";*/

    public Child() {    }

    public Child(HashMap<String,Object> fields) {
        id = (String)fields.get("id");
        name = (String)fields.get("name");
        gender = (String)fields.get("gender");
        age = (String)fields.get("age");
        littleExplain = (String)fields.get("littleExplain");
        hasExperience =  (boolean)fields.get("hasExperience")  ;
    }

    public static HashMap<String,Object> toFBObject(Child child) {
        HashMap<String,Object> cObject = new HashMap<>();
        cObject.put("id",child.id);
        cObject.put("name",child.name);
        cObject.put("gender",child.gender);
        cObject.put("age",child.age);
        cObject.put("littleExplain",child.littleExplain);
        cObject.put("hasExperience",child.hasExperience);
        return cObject;
    }

    public String getId() {
        return id;
    }

    public String setId() {
        id = UUID.randomUUID().toString();
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setMaleGender() {
        this.gender = MALE;
    }
    public void setFemaleGender() {
        this.gender = FEMALE;
    }
    public void setNoGender() {
        this.gender = WITHOUT;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean getHasExperience() {
        return hasExperience;
    }

    public void setHasExperience() {
        this.hasExperience = YES_EXPERIENCE;
    }

    public void setNoExperience() {
        this.hasExperience = NO_EXPERIENCE;
    }

    public String getLittleExplain() {
        return littleExplain;
    }

    public void setLittleExplain(String littleExplain) {
        this.littleExplain = littleExplain;
    }

    public static Child retrieveCurrentChild() {
        return currentChild;
    }

    public static void updateCurrentChild(Child currentChild) {
        Child.currentChild = currentChild;
    }
}
