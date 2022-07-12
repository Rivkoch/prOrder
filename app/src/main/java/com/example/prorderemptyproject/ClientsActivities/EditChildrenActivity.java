package com.example.prorderemptyproject.ClientsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prorderemptyproject.Models.Child;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;

public class EditChildrenActivity extends AppCompatActivity {

    private Spinner editChild_sp_chooseChild;
    private String stringsChildren[];
    private LinearLayout editChild_layoutExperience;
    private boolean printExperience;
    private String printAge, printName, printExplain, strChildName, strHasExperience;
    private MaterialButton editChild_addAnother, editChild_BTN_edit,editChildExperience_BTN_true, editChildExperience_BTN_false, editChild_BTN_cancel, editChild_BTN_save;
    private EditText editChild_ET_age, editChild_ET_childName, editChild_ET_experience;
    private TextView editChild_txt_currentExperience;
    private Child child;
    private Bundle bundle;
    private Client client;
    private ClientMainActivity clientMainActivity;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_children);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference();
        getChildParent();
        client = Client.retrieveCurrentClient();
        clientMainActivity = new ClientMainActivity();

        strChildName ="Child Name: ";
        strHasExperience ="Has Experience: ";

        findViews();
        manageViews(View.GONE);
        setSpinner();
        setListeners();

    }

    private void manageViews(int doSomething) {
        editChild_layoutExperience.setVisibility(doSomething);
        editChild_BTN_save.setVisibility(doSomething);
        editChild_ET_childName.setVisibility(doSomething);
        editChild_ET_age.setVisibility(doSomething);
        editChild_txt_currentExperience.setVisibility(doSomething);
        editChild_ET_experience.setVisibility(doSomething);
    }

    private void setSpinner() {
        setSpinnerValues();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringsChildren);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editChild_sp_chooseChild.setAdapter(adapter);
        editChild_sp_chooseChild.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSpinnerValues() {
        // Set array size
        stringsChildren = new String[client.getMyChildren().size() + 1];
        stringsChildren[0] = "  Tap  ";
        int i = 1;

        // Fill the array
        for (HashMap<String, Object> childObject : client.getMyChildren().values()) {
            Child c = new Child(childObject);
            stringsChildren[i++] = c.getName();
        }
    }

    private void getChildParent() {

        bundle = new Bundle();
        bundle = getIntent().getExtras();
        String clientJson = bundle.getString("Client");
        client = new Gson().fromJson(clientJson, Client.class);
    }

    @SuppressLint("ResourceAsColor")
    private void setListeners() {

        editChild_addAnother.setOnClickListener(v -> {
            Intent intent = new Intent(EditChildrenActivity.this, AddAnotherChildActivity.class);
            // Put client data in bundle
            String clientJson = new Gson().toJson(client);
            intent.putExtra("Client", clientJson);
            startActivity(intent);
        });

        editChild_BTN_edit.setOnClickListener(v -> {
            if(editChild_sp_chooseChild.getSelectedItem().toString().equals("  Tap  ")){
                Toast.makeText(this, "First choose child", Toast.LENGTH_SHORT).show();
            }
            else {
                manageViews(View.VISIBLE);
                defineChild();
                initFields();
            }
        });

        editChild_BTN_save.setOnClickListener(v -> {
            applyChildChanges();
            Child.updateCurrentChild(child);
            saveChanges();
            Client.updateCurrentClient(client);
            client = Client.retrieveCurrentClient();
            initFields();
            startActivity(new Intent(EditChildrenActivity.this,ClientMainActivity.class));
        });

        editChild_BTN_cancel.setOnClickListener(v -> {

            Intent intent = new Intent(EditChildrenActivity.this, ClientMainActivity.class);

            // Put client data in bundle
            String clientJson = new Gson().toJson(client);
            intent.putExtra("Client", clientJson);
            startActivity(intent);
        });

        editChildExperience_BTN_true.setOnClickListener(v -> {
            child.setHasExperience();
            editChildExperience_BTN_false.setBackgroundColor(android.R.color.darker_gray);
            editChildExperience_BTN_true.setBackgroundColor(R.color.bottom_item_background);

            editChild_ET_experience.setVisibility(View.VISIBLE);
        });

        editChildExperience_BTN_false.setOnClickListener(v -> {
            child.setNoExperience();
            editChildExperience_BTN_true.setBackgroundColor(android.R.color.darker_gray);
            editChildExperience_BTN_false.setBackgroundColor(R.color.bottom_item_background);

            editChild_ET_experience.setVisibility(View.GONE);
        });
    }

    private void applyChildChanges() {
        if(!editChild_ET_childName.getText().toString().isEmpty()) {
            child.setName(editChild_ET_childName.getText().toString());
        }

        if(!editChild_ET_experience.getText().toString().isEmpty()) {
            child.setLittleExplain(editChild_ET_experience.getText().toString());
        }

        if(!editChild_ET_age.getText().toString().isEmpty()) {
            child.setAge(editChild_ET_age.getText().toString());
        }
    }

    private void saveChanges() {
        getStrings();

        dataRef.child("Users").child(client.getId()).child("myChildren").child(child.getId()).child("name").setValue(printName);
        dataRef.child("Users").child(client.getId()).child("myChildren").child(child.getId()).child("hasExperience").setValue(printExperience);
        dataRef.child("Users").child(client.getId()).child("myChildren").child(child.getId()).child("littleExplain").setValue(printExplain);
        dataRef.child("Users").child(client.getId()).child("myChildren").child(child.getId()).child("age").setValue(printAge);

    }

    private void defineChild() {
        client = Client.retrieveCurrentClient();
        for (HashMap<String, Object> childObject : client.getMyChildren().values()) {
            Child c = new Child(childObject);
            if(c.getName().equals(editChild_sp_chooseChild.getSelectedItem().toString())){
                child = c;
            }
        }
    }

    private void initFields() {

        getStrings();

        editChild_ET_childName.setHint(strChildName + printName);
        editChild_txt_currentExperience.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editChild_txt_currentExperience.setText(strHasExperience + printExperience);
        editChild_ET_experience.setHint(printExplain);
        editChild_ET_age.setHint(printAge);

        editChild_ET_childName.setText("");
        editChild_ET_experience.setText("");
        editChild_ET_age.setText("");
    }

    private void getStrings() {
        printName = child.getName();
        printExperience = child.getHasExperience();
        printExplain = child.getLittleExplain();
        printAge = child.getAge();
    }

    private void findViews() {
        editChild_sp_chooseChild = findViewById(R.id.editChild_sp_chooseChild);
        editChild_BTN_edit = findViewById(R.id.editChild_BTN_edit);
        editChildExperience_BTN_true = findViewById(R.id.editChildExperience_BTN_true);
        editChildExperience_BTN_false = findViewById(R.id.editChildExperience_BTN_false);
        editChild_BTN_cancel = findViewById(R.id.editChild_BTN_cancel);
        editChild_BTN_save = findViewById(R.id.editChild_BTN_save);
        editChild_addAnother = findViewById(R.id.editChild_addAnother);
        editChild_ET_experience = findViewById(R.id.editChild_ET_experience);
        editChild_ET_childName = findViewById(R.id.editChild_ET_childName);
        editChild_txt_currentExperience = findViewById(R.id.editChild_txt_currentExperience);
        editChild_ET_age = findViewById(R.id.editChild_ET_age);
        editChild_layoutExperience = findViewById(R.id.editChild_layoutExperience);
    }

    // To go back "OUT" from app with phone
    @Override
    public void onBackPressed() {
            //goToHomeScreen();
        Intent intent = new Intent(EditChildrenActivity.this, ClientMainActivity.class);

        // Put client data in bundle
        String clientJson = new Gson().toJson(client);
        intent.putExtra("Client", clientJson);
        startActivity(intent);
    }

}