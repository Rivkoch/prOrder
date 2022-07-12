package com.example.prorderemptyproject.Models;

import java.io.Serializable;
import java.util.HashMap;

public class Client implements Serializable {

    static private Client currentClient;
    private static final Boolean IS_PARENT = true;
    private static final Boolean NOT_PARENT = false;

    private static final String MALE = "Male";
    private static final String FEMALE = "Female";
    private static final String WITHOUT = "Without";

    private static final String BLANK = "-";

    private String id;
    private String name = "";
    private String Email = "";
    private String PhoneNumber = "";
    private String gender = "";
    private String age = "";
    private int numOfOrders = 0;
    private Boolean isParent;
    private boolean admin = false;
    private int currentLessonNumber = 0;
    private HashMap<String, HashMap<String,Object>> myChildren = new HashMap<>();
    private HashMap<String, HashMap<String,Object>> clientOrders = new HashMap<>();

    public Client(){ }

    public Client(HashMap<String,Object> fields) {
        id = (String)fields.get("id");
        name = (String)fields.get("name");
        Email = (String)fields.get("Email");
        PhoneNumber = (String)fields.get("PhoneNumber");
        gender = (String)fields.get("PhoneNumber");
        age = (String) fields.get("age");;
        numOfOrders = (int) fields.get("numOfOrders");
        isParent = (boolean) fields.get("isParent");;
    }

    public static HashMap<String,Object> toFBObject(Client client) {
        HashMap<String,Object> cObject = new HashMap<>();
        cObject.put("id",client.id);
        cObject.put("name",client.name);
        cObject.put("Email",client.Email);
        cObject.put("PhoneNumber",client.PhoneNumber);
        cObject.put("age",client.age);
        cObject.put("numOfOrders",client.numOfOrders);
        cObject.put("isParent",client.isParent);
        return cObject;
    }


    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getNumOfOrders() {
        return numOfOrders;
    }

    public void setNumOfOrders() {
        this.numOfOrders = ++numOfOrders;
    }

    public void unsetNumOfOrders() {
        this.numOfOrders = --numOfOrders;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent() {
        this.isParent = IS_PARENT;
    }

    public void setNotParent() {
        this.isParent = NOT_PARENT;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
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

    public void setDefaultAge() {
        this.age = BLANK;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public HashMap<String, HashMap<String,Object>> getMyChildren() {
        return myChildren;
    }

    public void setMyChildren(HashMap<String, HashMap<String,Object>> myChildren) {
        this.myChildren = myChildren;
    }

    public HashMap<String, HashMap<String, Object>> getClientOrders() {
        return clientOrders;
    }

    public void setClientOrders(HashMap<String, HashMap<String, Object>> clientOrders) {
        this.clientOrders = clientOrders;
    }

    public int getCurrentLessonNumber() {
        return currentLessonNumber;
    }

    public void setCurrentLessonNumber() {
        this.currentLessonNumber = currentLessonNumber++;
    }

    public static Client retrieveCurrentClient() {
        return currentClient;
    }

    public static void updateCurrentClient(Client currentClient) {
        Client.currentClient = currentClient;
    }
}
