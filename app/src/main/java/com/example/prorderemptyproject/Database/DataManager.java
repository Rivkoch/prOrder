package com.example.prorderemptyproject.Database;

import androidx.annotation.NonNull;

import com.example.prorderemptyproject.CallBackHandleSignInUser;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.Models.Order;
import com.example.prorderemptyproject.Models.Trainer;
import com.example.prorderemptyproject.Models.TraningTypeEnum;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataManager {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    public DataManager() {
    }

    public void HandleSignInUser(CallBackHandleSignInUser callBackHandleSignInUser) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            databaseReference.child("Users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Client str = snapshot.getValue(Client.class);

                    if(callBackHandleSignInUser != null && str != null){
                        callBackHandleSignInUser.isUserExist(true);
                    }
                    else{
                        callBackHandleSignInUser.isUserExist(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }

    public static ArrayList<Order> generateData() {
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<Trainer> trainers = new ArrayList<>();

        ArrayList<TraningTypeEnum> allThree = new ArrayList<>();
        allThree.add(TraningTypeEnum.SWIMMING);
        allThree.add(TraningTypeEnum.HIDROTHERAPY);
        allThree.add(TraningTypeEnum.IMPRUVEMENT);

        ArrayList<TraningTypeEnum> onlySwim = new ArrayList<>();
        onlySwim.add(TraningTypeEnum.SWIMMING);

        ArrayList<TraningTypeEnum> onlyHidro = new ArrayList<>();
        onlySwim.add(TraningTypeEnum.HIDROTHERAPY);

        ArrayList<TraningTypeEnum> onlyStyleImprovement = new ArrayList<>();
        onlySwim.add(TraningTypeEnum.IMPRUVEMENT);

        return orders;
    }
}
