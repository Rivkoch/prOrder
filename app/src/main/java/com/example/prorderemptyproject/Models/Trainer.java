package com.example.prorderemptyproject.Models;

import com.example.prorderemptyproject.Abstract.AbstractAccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Trainer extends AbstractAccount implements Serializable {

    private HashMap<String, Order> trainerOrders = new HashMap<>();


    static private Trainer currentTrainer;

    private static final String MALE = "Male";
    private static final String FEMALE = "Female";
    private static final String WITHOUT = "Without";

    private String gender = "";
    private String orderId; // todo: UUID
    private String Email = "";
    private String Phone = "";
    private String Experience = "";

    private ArrayList<TraningTypeEnum> types = new ArrayList<>();


    public Trainer() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public ArrayList<TraningTypeEnum> getType() {
        return types;
    }

    public void setType(ArrayList<TraningTypeEnum> types) {
        this.types = types;
    }

    public static Trainer retrieveCurrentTrainer() {
        return currentTrainer;
    }

    public static void updateCurrentTrainer(Client currentClient) {
        Trainer.currentTrainer = currentTrainer;
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

}
