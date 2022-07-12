package com.example.prorderemptyproject.ClientsActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prorderemptyproject.Abstract.AbstractAddChild;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class AddAnotherChildActivity extends AbstractAddChild {

    private ImageView anotherChild_IMG_Close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_another_child);

        getChildParent();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        findViews();
        setSpinner();
        setListeners();
        addChildToClient(childId);
        saveChildsData();
        anotherChild_IMG_Close = findViewById(R.id.anotherChild_IMG_Close);
        anotherChild_IMG_Close.setOnClickListener(v -> {
            Client.updateCurrentClient(client);
            // Put client data in bundle
            Intent intent = new Intent(AddAnotherChildActivity.this, ClientMainActivity.class);
            String clientJson = new Gson().toJson(client);
            intent.putExtra("Client", clientJson);
            startActivity(intent);
        });

    }

    // To go back "OUT" from app with phone
    @Override
    public void onBackPressed() {
        Toast.makeText(AddAnotherChildActivity.this, "You cant go back, this is important data!", Toast.LENGTH_SHORT).show();
    }

}