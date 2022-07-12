package com.example.prorderemptyproject.ClientsActivities;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prorderemptyproject.Models.Child;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;
import com.example.prorderemptyproject.SplashScreenActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ChildrenDataActivity extends AppCompatActivity {

    private EditText childrenData_childName,childExperience_experience;
    private MaterialButton childrenData_Male,childrenData_Female,childrenData_WithoutGender,childrenData_experience_true;
    private MaterialButton childrenData_experience_false,childrenData_BTN_addChild,childrenData_BTN_save;
    private Child child;
    private Client client;
    private String name;

    //Spinner
    private String childAge[];// = {"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"};
    private Spinner childrenData_spinner;

    private Bundle bundle;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static Activity singleChildActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_data);
        singleChildActivity = this;
        getChildParent();

        child = new Child();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        findViews();
        setSpinner();
        setListeners();
    }

    private void getChildParent() {

        bundle = new Bundle();
        bundle = getIntent().getExtras();
        String clientJson = bundle.getString("Client");
        client = new Gson().fromJson(clientJson, Client.class);
    }


    private void setSpinner() {
        setSpinnerValues();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, childAge);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childrenData_spinner.setAdapter(adapter);
        childrenData_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSpinnerValues() {
        childAge = new String[15];
        for (int j = 0, i = 4; i <= 18; i++, j++){
            childAge[j] = valueOf(i);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setListeners() {

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
            childrenData_Male.setBackgroundColor(android.R.color.darker_gray);
            childrenData_Female.setBackgroundColor(android.R.color.darker_gray);
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
            saveChildrenData();

            Intent intent = new Intent(ChildrenDataActivity.this, AddAnotherChildActivity.class);
            // Put client data in bundle
            String clientJson = new Gson().toJson(client);
            intent.putExtra("Client", clientJson);
            startActivity(intent);
//            startActivity(new Intent(ChildrenDataActivity.this, AddAnotherChildActivity.class));

        });

        childrenData_BTN_save.setOnClickListener(v -> {

            saveChildrenData();
            saveChildrenData();
            Client.updateCurrentClient(client);

            Intent intent = new Intent(ChildrenDataActivity.this, SplashScreenActivity.class);

            SplashScreenActivity.singleSplashActivity.finish();
            ChildrenDataActivity.singleChildActivity.finish();
            // Put client data in bundle
            String clientJson = new Gson().toJson(client);
            intent.putExtra("Client", clientJson);
            startActivity(intent);


            SplashScreenActivity.singleSplashActivity.finish();
            ChildrenDataActivity.singleChildActivity.finish();
//            startActivity(new Intent(ChildrenDataActivity.this, ClientMainActivity.class));

        });
    }

    private void startSplashActivity() {
        Intent splashIntent = new Intent (this, SplashScreenActivity.class);
        startActivity (splashIntent);
    }

    private void saveChildrenData() {

        if(childExperience_experience.getVisibility() == View.VISIBLE){
            child.setLittleExplain(childExperience_experience.getText().toString());
        }

        String childId = child.setId();
        child.setName(childrenData_childName.getText().toString());
        child.setAge(childrenData_spinner.getSelectedItem().toString());

        // Add child to current client
        addChildToClient(childId);
        Client.updateCurrentClient(client);

        databaseReference.child("Users").child(client.getId()).child("myChildren").child(child.getId()).setValue(child);
    }

    private void addChildToClient(String childId) {
        if(child != null){
            Log.d("pttt", "Child is: " + child.toString());
            Map<String, HashMap<String,Object>> myChildren = new HashMap<>();
            myChildren.put(childId, Child.toFBObject(child));
            client.setMyChildren((HashMap<String, HashMap<String, Object>>) myChildren);
//            client.setNumOfChildren(myChildren.size());

            Client theClient = Client.retrieveCurrentClient();
            theClient.setMyChildren((HashMap<String, HashMap<String, Object>>) myChildren);



            String str = databaseReference.child("Users").child(client.getId()).child("myChildren").child(child.getId()).child(child.getName()).toString();

            Toast.makeText(this, "Child added successfully", Toast.LENGTH_SHORT).show();
            // TODO : CHECK IF WE GET CHILD>> Log.d("pttt", "Child is: " );
        }

    }

    private void getTheName() {
        name = childrenData_childName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            childrenData_childName.setError("Email cannot be empty");
            childrenData_childName.requestFocus();
        }
    }

    private void findViews() {

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

    // To go back "OUT" from app with phone
    @Override
    public void onBackPressed() {
        Toast.makeText(ChildrenDataActivity.this, "You cant go back, this is important data!", Toast.LENGTH_SHORT).show();
    }

}