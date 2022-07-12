package com.example.prorderemptyproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.prorderemptyproject.ClientsActivities.ClientMainActivity;
import com.example.prorderemptyproject.ClientsActivities.SetClientDataActivity;
import com.example.prorderemptyproject.Database.DataManager;
import com.example.prorderemptyproject.Models.Client;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private MaterialButton welcome_BTN_clients;
    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private DataManager dataManager;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
        dataManager = new DataManager();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        findViews();
        setListeners();
    }

    private void setListeners() {

        welcome_BTN_clients.setOnClickListener(v->{
            signIn();
        });
    }

    private void findViews() {
        welcome_BTN_clients = findViewById(R.id.welcome_BTN_clients);
    }

    private void signIn() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.img_logo)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        if(result.getResultCode() == RESULT_OK){
            // check if user exists
            getClient();
            FirebaseUser user = mAuth.getCurrentUser();
            DatabaseReference db = databaseReference.child("Users").child(user.getUid());
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    client = snapshot.getValue(Client.class);
                    Client.updateCurrentClient(client);

                    db.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void getClient() {
        dataManager.HandleSignInUser(new CallBackHandleSignInUser() {
            @Override
            public void isUserExist(boolean isExist) {

                if(!isExist) {
                    Intent intent = new Intent(WelcomeActivity.this, SetClientDataActivity.class);

                    startActivity(intent);
                }
                else{

                    Intent intent = new Intent(WelcomeActivity.this, ClientMainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void updateUI() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

        FirebaseUser user = mAuth.getCurrentUser();

        String str = "USER:\n";
        if (user != null) {
            str += "\n" + user.getUid();
            str += "\n" + user.getDisplayName();
            str += "\n" + user.isEmailVerified();
            str += "\n" + user.getPhoneNumber();
        }
    }

}