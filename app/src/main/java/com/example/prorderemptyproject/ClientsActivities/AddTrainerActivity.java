package com.example.prorderemptyproject.ClientsActivities;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prorderemptyproject.Models.Trainer;
import com.example.prorderemptyproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTrainerActivity extends AppCompatActivity {

    private ImageView trainerData_IMG_Close;
    private TextInputEditText trainerData_Name;
    private TextInputEditText trainerData_Email;
    private TextInputEditText trainerData_phoneNumber;
    private MaterialButton trainerData_WithoutGender;
    private MaterialButton trainerData_Male;
    private MaterialButton trainerData_Female;
    private Spinner trainerData_ageSpinner;
    private MaterialButton trainerData_BTN_save;

    private String trainerAge[];

    private Trainer trainer;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trainer);
        setDatabase();
        trainer = new Trainer();
        findViews();
        setAgeSpinner();
        setListeners();
    }

    @SuppressLint("ResourceAsColor")
    private void setListeners() {

        trainerData_Male.setOnClickListener(v -> {
            trainer.setMaleGender();
            trainerData_Female.setBackgroundColor(android.R.color.darker_gray);
            trainerData_WithoutGender.setBackgroundColor(android.R.color.darker_gray);
            trainerData_Male.setBackgroundColor(R.color.bottom_item_background);
        });

        trainerData_Female.setOnClickListener(v -> {
            trainer.setFemaleGender();
            trainerData_Male.setBackgroundColor(android.R.color.darker_gray);
            trainerData_WithoutGender.setBackgroundColor(android.R.color.darker_gray);
            trainerData_Female.setBackgroundColor(R.color.bottom_item_background);

        });

        trainerData_WithoutGender.setOnClickListener(v -> {
            trainer.setNoGender();
            trainerData_Female.setBackgroundColor(android.R.color.darker_gray);
            trainerData_Male.setBackgroundColor(android.R.color.darker_gray);
            trainerData_WithoutGender.setBackgroundColor(R.color.bottom_item_background);

        });

        trainerData_IMG_Close.setOnClickListener(v -> {
            startActivity(new Intent(AddTrainerActivity.this, ClientMainActivity.class));
        });

        trainerData_BTN_save.setOnClickListener(v -> {
            getTrainerDate();
//            trainers.setNumberOfTrainers();
//            localList.add(trainer);
//            trainers.setTrainerList(localList);
            databaseReference.child("Trainers").child(trainer.getId()).setValue(trainer);
            Toast.makeText(AddTrainerActivity.this, "Trainer added successfully\n", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddTrainerActivity.this, ClientMainActivity.class));

        });
    }

    private void getTrainerDate() {
        trainer.setId();
        trainer.setName(trainerData_Name.getText().toString());
        trainer.setEmail(trainerData_Email.getText().toString());
        trainer.setPhone(trainerData_phoneNumber.getText().toString());
        getTrainerAgeFromSpinner();
    }

    private void getTrainerAgeFromSpinner() {

        Object selectedObject = trainerData_ageSpinner.getSelectedItem();
        if((selectedObject != null) ) {
            trainer.setExperience(trainerData_ageSpinner.getSelectedItem().toString());
        }else {
            Toast.makeText(AddTrainerActivity.this, "Please enter trainer's experience\n", Toast.LENGTH_LONG).show();
            return;
        }

    }

    private void setAgeSpinner() {
        setValues();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, trainerAge);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trainerData_ageSpinner.setAdapter(adapter);
        trainerData_ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setValues() {
        trainerAge = new String[102];
        trainerAge[0] = "--Select--";
        for (int i = 1; i < trainerAge.length; i++)
            trainerAge[i] = valueOf(i-1);
    }

    private void findViews() {
        trainerData_IMG_Close = findViewById(R.id.trainerData_IMG_Close);
        trainerData_Name = findViewById(R.id.trainerData_Name);
        trainerData_Email = findViewById(R.id.trainerData_Email);
        trainerData_phoneNumber = findViewById(R.id.trainerData_phoneNumber);
        trainerData_Male = findViewById(R.id.trainerData_Male);
        trainerData_Female = findViewById(R.id.trainerData_Female);
        trainerData_WithoutGender = findViewById(R.id.trainerData_WithoutGender);
        trainerData_ageSpinner = findViewById(R.id.trainerData_ageSpinner);
        trainerData_BTN_save = findViewById(R.id.trainerData_BTN_save);
    }

    private void setDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

}
