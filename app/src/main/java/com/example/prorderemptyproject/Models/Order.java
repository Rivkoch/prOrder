package com.example.prorderemptyproject.Models;

import java.util.HashMap;
import java.util.UUID;

public class Order {

    private String id;
    private String clientId = "";
    private String trainerId = "";
    private String clientName = "";
    private String trainerName = "";
    private long date;
    private String childName = "None";

    public Order() {}

    public Order(HashMap<String,Object> fields) {
        id = (String)fields.get("id");
        clientId = (String)fields.get("clientId");
        trainerId = (String)fields.get("trainerId");
        trainerName = (String)fields.get("trainerName");
        clientName = (String)fields.get("clientName");
        childName = (String)fields.get("childName");
        date = (long) fields.get("date");;
    }

    public static HashMap<String,Object> toFBObject(Order order) {
        HashMap<String,Object> cObject = new HashMap<>();
        cObject.put("id",order.id);
        cObject.put("clientId",order.clientId);
        cObject.put("trainerId",order.trainerId);
        cObject.put("clientName",order.clientName);
        cObject.put("trainerName",order.trainerName);
        cObject.put("date",order.date);
        return cObject;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getClientId() {
        return clientId;
    }

    public String getId() {
        return id;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildName() {
        return childName;
    }
}
