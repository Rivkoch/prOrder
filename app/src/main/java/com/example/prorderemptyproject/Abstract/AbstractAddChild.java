package com.example.prorderemptyproject.Abstract;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prorderemptyproject.ClientsActivities.AddAnotherChildActivity;
import com.example.prorderemptyproject.ClientsActivities.ClientMainActivity;
import com.example.prorderemptyproject.Models.Child;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAddChild extends  AppCompatActivity{

    protected EditText childrenData_childName;
    protected EditText childExperience_experience;
    protected MaterialButton childrenData_Male;
    protected MaterialButton childrenData_Female;
    protected MaterialButton childrenData_WithoutGender;
    protected MaterialButton childrenData_experience_true;
    protected MaterialButton childrenData_experience_false;
    protected MaterialButton childrenData_BTN_addChild;
    protected MaterialButton childrenData_BTN_save;
    protected Child child;
    protected Client client;
    protected String name;

    //Spinner
    protected String childAge[];// = {"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"};
    protected Spinner childrenData_spinner;

    protected Bundle bundle;
    protected String childId;

    protected FirebaseDatabase firebaseDatabase;
    protected DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_data);

        getChildParent();

        child = new Child();
        childId = child.setId();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        findViews();
        setSpinner();
        setListeners();
    }

    protected void getChildParent() {

        bundle = new Bundle();
        bundle = getIntent().getExtras();
        String clientJson = bundle.getString("Client");
        client = new Gson().fromJson(clientJson, Client.class);
    }


    protected void setSpinner() {
        setSpinnerValues();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, childAge);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childrenData_spinner.setAdapter(adapter);
        childrenData_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //delete this>>>> child.setAge(childrenData_spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    protected void setSpinnerValues() {
        childAge = new String[15];
        for (int j = 0, i = 4; i <= 18; i++, j++){
            childAge[j] = valueOf(i);
        }
    }

    @SuppressLint("ResourceAsColor")
    protected void setListeners() {

        childrenData_Male.setOnClickListener(v -> {
            child.setMaleGender();
            childrenData_Female.setBackgroundColor(android.R.color.darker_gray);
            childrenData_WithoutGender.setBackgroundColor(android.R.color.darker_gray);
            childrenData_Male.setBackgroundColor(R.color.bottom_item_background);
        });

        childrenData_Female.setOnClickListener(v -> {
            child.setFemaleGender();
            childrenData_Male.setBackgroundColor(android.R.color.darker_gray);
            childrenData_WithoutGender.setBackgroundColor(android.R.color.darker_gray);
            childrenData_Female.setBackgroundColor(R.color.bottom_item_background);

        });

        childrenData_WithoutGender.setOnClickListener(v -> {
            child.setNoGender();
            childrenData_Female.setBackgroundColor(android.R.color.darker_gray);
            childrenData_Male.setBackgroundColor(android.R.color.darker_gray);
            childrenData_WithoutGender.setBackgroundColor(R.color.bottom_item_background);

        });

        childrenData_experience_true.setOnClickListener(v -> {
            child.setHasExperience();
            childrenData_experience_false.setBackgroundColor(android.R.color.darker_gray);
            childrenData_experience_true.setBackgroundColor(R.color.bottom_item_background);
            childExperience_experience.setVisibility(View.VISIBLE);
        });

        childrenData_experience_false.setOnClickListener(v -> {
            child.setNoExperience();
            childrenData_experience_true.setBackgroundColor(android.R.color.darker_gray);
            childrenData_experience_false.setBackgroundColor(R.color.bottom_item_background);
            childExperience_experience.setVisibility(View.GONE);
        });

        childrenData_BTN_addChild.setOnClickListener(v -> {
            saveChildsData();

            Intent intent = new Intent(this, AddAnotherChildActivity.class);

            // Put client data in bundle
            String clientJson = new Gson().toJson(client);
            intent.putExtra("Client", clientJson);
            startActivity(intent);
//            startActivity(new Intent(ChildrenDataActivity.this, AddAnotherChildActivity.class));

        });

        childrenData_BTN_save.setOnClickListener(v -> {
            saveChildsData();
            Client.updateCurrentClient(client);
            startActivity(new Intent(this, ClientMainActivity.class));

        });
    }

    protected void saveChildsData() {

        if(childExperience_experience.getVisibility() == View.VISIBLE){
            child.setLittleExplain(childExperience_experience.getText().toString());
        }

        child.setName(childrenData_childName.getText().toString());
        child.setAge(childrenData_spinner.getSelectedItem().toString());

        // Add child to current client
        addChildToClient(childId);
        Client.updateCurrentClient(client);

        databaseReference.child("Users").child(client.getId()).child("myChildren").child(child.getId()).setValue(child);
    }

    protected void addChildToClient(String childId) {

        if(child != null){
            Log.d("pttt", "Child is: " + child.toString());
            Map<String, HashMap<String,Object>> myChildren = client.getMyChildren();
            myChildren.put(childId, Child.toFBObject(child));
            client.setMyChildren((HashMap<String, HashMap<String,Object>>) myChildren);

            Client theClient = Client.retrieveCurrentClient();
            theClient.setMyChildren((HashMap<String, HashMap<String,Object>>) myChildren);
            Client.updateCurrentClient(theClient);
            String str = databaseReference.child("Users").child(client.getId()).child("Children").child(child.getId()).child(child.getName()).toString();

            Toast.makeText(this, "Child added successfully", Toast.LENGTH_SHORT).show();
            // TODO : CHECK IF WE GET CHILD>> Log.d("pttt", "Child is: " );
        }
    }

    protected void findViews() {

        childrenData_childName = findViewById(R.id.childrenData_childName);
        childrenData_Male = findViewById(R.id.childrenData_Male);
        childrenData_Female = findViewById(R.id.childrenData_Female);
        childrenData_WithoutGender = findViewById(R.id.childrenData_WithoutGender);
        childrenData_spinner = (Spinner) findViewById(R.id.childrenData_spinner); //Age
        childrenData_experience_true = findViewById(R.id.childrenData_experience_true);
        childrenData_experience_false = findViewById(R.id.childrenData_experience_false);
        childExperience_experience = findViewById(R.id.childExperience_experience);
        childExperience_experience.setVisibility(View.GONE);
        childrenData_BTN_addChild = findViewById(R.id.childrenData_BTN_addChild);
        childrenData_BTN_save = findViewById(R.id.childrenData_BTN_save);
    }

}