package com.example.prorderemptyproject.ClientFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prorderemptyproject.ClientsActivities.EditChildrenActivity;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class ClientEditFragment extends Fragment {
    private static String PARENT = "Parent", ADMIN = "Administrator", CLIENT = "Client";

    private EditText edit_ET_userName,edit_ET_phoneNumber,edit_ET_age;
    private MaterialButton edit_BTN_WithoutGender,edit_BTN_Male,edit_BTN_Female;
    private MaterialButton edit_BTN_children,edit_BTN_cancel,edit_BTN_save;
    private TextView edit_txt_gender,edit_txt_userType;
    private String printName, printAge, printPhone, printGender;

    private Client client;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dataRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_client_settings,container,false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference();
        client = Client.retrieveCurrentClient();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        if(client.isAdmin() || !client.getIsParent()){
            edit_BTN_children.setVisibility(View.GONE);
        }
        initFields();
        setListeners();
    }

    @SuppressLint("ResourceAsColor")
    private void setListeners() {

        edit_BTN_children.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chose child to edit his data\n", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getContext(), EditChildrenActivity.class);

            // Put client data in bundle
            String clientJson = new Gson().toJson(client);
            intent.putExtra("Client", clientJson);
            startActivity(intent);
        });

        edit_BTN_save.setOnClickListener(v -> {
            getChangesFromClient();
            Client.updateCurrentClient(client);
            saveChanges();
            client = Client.retrieveCurrentClient();
            initFields();
        });

        edit_BTN_cancel.setOnClickListener(v -> {
            initFields();
        });

        edit_BTN_WithoutGender.setOnClickListener(v -> {
            client.setNoGender();
            edit_BTN_Male.setBackgroundColor(android.R.color.darker_gray);
            edit_BTN_Female.setBackgroundColor(android.R.color.darker_gray);
            edit_BTN_WithoutGender.setBackgroundColor(R.color.bottom_item_background);
            edit_txt_gender.setText(printGender);
        });

        edit_BTN_Male.setOnClickListener(v -> {
            client.setMaleGender();
            edit_BTN_WithoutGender.setBackgroundColor(android.R.color.darker_gray);
            edit_BTN_Female.setBackgroundColor(android.R.color.darker_gray);
            edit_BTN_Male.setBackgroundColor(R.color.bottom_item_background);
            edit_txt_gender.setText(printGender);
        });

        edit_BTN_Female.setOnClickListener(v -> {
            client.setFemaleGender();
            edit_BTN_Male.setBackgroundColor(android.R.color.darker_gray);
            edit_BTN_WithoutGender.setBackgroundColor(android.R.color.darker_gray);
            edit_BTN_Female.setBackgroundColor(R.color.bottom_item_background);
            edit_txt_gender.setText(printGender);
        });

    }

    private void getChangesFromClient() {
        if(!edit_ET_userName.getText().toString().isEmpty()) {
            client.setName(edit_ET_userName.getText().toString());
        }

        if(!edit_ET_phoneNumber.getText().toString().isEmpty()) {
            client.setPhoneNumber(edit_ET_phoneNumber.getText().toString());
        }
        if(!edit_ET_age.getText().toString().isEmpty()) {
            client.setAge(edit_ET_age.getText().toString());
        }
    }

    private void saveChanges() {
        dataRef.child("Users").child(client.getId()).child("name").setValue(client.getName());
        dataRef.child("Users").child(client.getId()).child("phoneNumber").setValue(client.getPhoneNumber());
        dataRef.child("Users").child(client.getId()).child("gender").setValue(client.getGender());
        dataRef.child("Users").child(client.getId()).child("age").setValue(client.getAge());


    }

    private void initFields() {
        printGender = "Gender: " + client.getGender();
        printPhone = "Phone: " + client.getPhoneNumber();
        printAge = "Age: " + client.getAge();
        printName = "Client name: " + client.getName();

        client = Client.retrieveCurrentClient();
        edit_ET_userName.setHint(printName);
        edit_ET_userName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setUserTypeTxt();
        edit_ET_phoneNumber.setHint(printPhone);
        edit_ET_age.setHint(printAge);
        edit_txt_gender.setText(printGender);

        edit_ET_userName.setText("");
        edit_ET_phoneNumber.setText("");
        edit_ET_age.setText("");

    }

    private void setUserTypeTxt() {
        if(client.getIsParent()){
            edit_txt_userType.setText(PARENT);
            edit_txt_userType.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else if(client.isAdmin()){
            edit_txt_userType.setText(ADMIN);
            edit_txt_userType.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        else{
            edit_txt_userType.setText(CLIENT);
            edit_txt_userType.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
    }

    private void findViews(View view) {
        edit_BTN_save = view.findViewById(R.id.edit_BTN_save);
        edit_BTN_cancel = view.findViewById(R.id.edit_BTN_cancel);
        edit_BTN_children = view.findViewById(R.id.edit_BTN_children);
        edit_BTN_Female = view.findViewById(R.id.edit_BTN_Female);
        edit_BTN_Male = view.findViewById(R.id.edit_BTN_Male);
        edit_BTN_WithoutGender = view.findViewById(R.id.edit_BTN_WithoutGender);
        edit_ET_age = view.findViewById(R.id.edit_ET_age);
        edit_ET_phoneNumber = view.findViewById(R.id.edit_ET_phoneNumber);
        edit_ET_userName = view.findViewById(R.id.edit_ET_userName);
        edit_txt_gender = view.findViewById(R.id.edit_txt_gender);
        edit_txt_userType = view.findViewById(R.id.edit_txt_userType);
    }
}