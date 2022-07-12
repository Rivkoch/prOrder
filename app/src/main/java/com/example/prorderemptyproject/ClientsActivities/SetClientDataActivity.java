package com.example.prorderemptyproject.ClientsActivities;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.Validator;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

//import com.example.prorderemptyproject.Fragments.ClientFragments.BlankFragment;
//import com.example.prorderemptyproject.Fragments.ClientFragments.ClientChildsFragment;
//import com.example.prorderemptyproject.Fragments.ClientFragments.ClientMoreDataFragment;
import com.example.prorderemptyproject.Models.Child;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;
import com.example.prorderemptyproject.SplashScreenActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SetClientDataActivity extends AppCompatActivity{

    private TextInputEditText clientData_phoneNumber;
    private LinearLayout clientData_LL_Age,clientData_LL_Gender;
    private MaterialButton clientData_Male, clientData_Female, clientData_WithoutGender, clientData_BTN_save;
    private RadioButton clientData_isParent,clientData_notParent;

    private RadioGroup clientData_RBG_yes_no;

    private Spinner clientData_ageSpinner;
    private String clientAge[];

    private Client client;
    private int counter = 0;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private Bundle bundle;
    public static Activity singleSetClientActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleSetClientActivity =this;
        setContentView(R.layout.activity_set_client_data);

        setDatabase();

        setNewClientAndBundle();

        findViews();
        setListeners();
        setAgeSpinner();
    }

    private void setAgeSpinner() {
        setValues();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, clientAge);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientData_ageSpinner.setAdapter(adapter);
        clientData_ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setValues() {
        clientAge = new String[100];
        clientAge[0] = "--Select--";
        for (int i = 1; i < clientAge.length; i++)
            clientAge[i] = valueOf(18 + i);
     }

    private void showChildren() {
        databaseReference.child("Users").child(client.getId()).child("myChildren").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Child> allChildren = new ArrayList<Child>();

                //get form DB
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Child child = dataSnapshot.getValue(Child.class);
                    allChildren.add(child);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void setListeners() {

        clientData_Male.setOnClickListener(v -> {
            client.setMaleGender();
            clientData_Female.setBackgroundColor(android.R.color.darker_gray);
            clientData_WithoutGender.setBackgroundColor(android.R.color.darker_gray);
            clientData_Male.setBackgroundColor(R.color.bottom_item_background);
        });

        clientData_Female.setOnClickListener(v -> {
            client.setFemaleGender();
            clientData_Male.setBackgroundColor(android.R.color.darker_gray);
            clientData_WithoutGender.setBackgroundColor(android.R.color.darker_gray);
            clientData_Female.setBackgroundColor(R.color.bottom_item_background);

        });

        clientData_WithoutGender.setOnClickListener(v -> {
            client.setNoGender();
            clientData_Female.setBackgroundColor(android.R.color.darker_gray);
            clientData_Male.setBackgroundColor(android.R.color.darker_gray);
            clientData_WithoutGender.setBackgroundColor(R.color.bottom_item_background);

        });

        clientData_RBG_yes_no.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.clientData_isParent:
                        client.setIsParent();
                        client.setDefaultAge();
                        client.setNoGender();

                        // Extra data not required
                        clientData_LL_Age.setVisibility(View.GONE);
                        clientData_LL_Gender.setVisibility(View.GONE);

                        break;
                    case R.id.clientData_notParent:
                        client.setNotParent();
                        clientData_LL_Age.setVisibility(View.VISIBLE);
                        clientData_LL_Gender.setVisibility(View.VISIBLE);
                        break;
                }
            }

        });

        clientData_BTN_save.setOnClickListener(v -> {
            getClientData();
            Client.updateCurrentClient(client);

            databaseReference.child("Users").child(firebaseUser.getUid()).setValue(client);

            nextIntent();
        });
    }


    private void getClientData() {
        client.setPhoneNumber(clientData_phoneNumber.getText().toString());

        if(clientData_LL_Age.getVisibility() == View.VISIBLE) {
            getClientAgeFromSpinner();
        }
    }

    private void getClientAgeFromSpinner() {

        Object selectedObject = clientData_ageSpinner.getSelectedItem();
        if((selectedObject != null && selectedObject.toString().equals("--Select--")) ) {
            Toast.makeText(SetClientDataActivity.this, "Please your age\n", Toast.LENGTH_LONG).show();
            return;
        }else if(selectedObject != null){
            client.setAge(clientData_ageSpinner.getSelectedItem().toString());
        }

    }

    private void nextIntent() {
        if(clientData_LL_Age.getVisibility() == View.VISIBLE && clientData_LL_Gender.getVisibility() == View.VISIBLE) {
            // Define next intent when client is not parent
            Toast.makeText(SetClientDataActivity.this, "User registered successfully\n", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(SetClientDataActivity.this, ClientMainActivity.class);
            SetClientDataActivity.singleSetClientActivity.finish();
            startActivity(intent);
        }
        else{

            // Define next intent when client is parent
            Toast.makeText(SetClientDataActivity.this, "Please add child data\n", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(SetClientDataActivity.this, ChildrenDataActivity.class);
            //CircleSplashActivity
            SetClientDataActivity.singleSetClientActivity.finish();
            // Put client data in bundle
            String clientJson = new Gson().toJson(client);
            intent.putExtra("Client", clientJson);
            startActivity(intent);
        }

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void findViews() {
        // Basic view
        clientData_phoneNumber = findViewById(R.id.clientData_phoneNumber);
        clientData_RBG_yes_no = findViewById(R.id.clientData_RBG_yes_no);
        clientData_isParent= findViewById(R.id.clientData_isParent);
        //clientData_notParent= findViewById(R.id.clientData_notParent);
        clientData_BTN_save = findViewById(R.id.clientData_BTN_save);

        // If the user is the client no child
        clientData_Male = findViewById(R.id.clientData_Male);
        clientData_Female = findViewById(R.id.clientData_Female);
        clientData_WithoutGender= findViewById(R.id.clientData_WithoutGender);

        clientData_ageSpinner= findViewById(R.id.clientData_ageSpinner);

        // "Extra" Layouts
        clientData_LL_Age = findViewById(R.id.clientData_LL_Age);
        clientData_LL_Gender = findViewById(R.id.clientData_LL_Gender);

        // Set as default
        clientData_LL_Age.setVisibility(View.GONE);
        clientData_LL_Gender.setVisibility(View.GONE);
    }

    private void setNewClientAndBundle() {
        client = new Client();

        client.setId(firebaseUser.getUid());
        client.setName(firebaseUser.getDisplayName());
        client.setEmail(firebaseUser.getEmail());

        bundle = new Bundle();
    }

    private void setDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    // To go back "OUT" from app with phone
    @Override
    public void onBackPressed() {
        Toast.makeText(SetClientDataActivity.this, "You cant go back, this is important data!", Toast.LENGTH_SHORT).show();
    }

}