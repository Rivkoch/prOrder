package com.example.prorderemptyproject.Database;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.prorderemptyproject.Models.Child;
import com.example.prorderemptyproject.Models.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientAccountData {
    private Client client;
    private Child child;
    private List<Child> allChildren;
    private String childrenData = "";
    private StringBuffer clientData;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = firebaseDatabase.getReference();

    public void readAccount(TextView txt) {
        client = Client.retrieveCurrentClient();

        if (client != null) {
            clientData = new StringBuffer( "Your Name: " +  client.getName() + "\n"
                    + "Email: " +  client.getEmail() + "\n"
                    + "Phone Number: " +  client.getPhoneNumber() + "\n");

            if (client.getIsParent()) {
                // Get Client Data

                // Get Children Data:
                getChildrenData(txt);

            } else {
                clientData.append("Gender: " +  client.getGender() + "\n"
                                + "Age: " +  client.getAge() + "\n");
                txt.setText(clientData.toString());
            }
        }

    }

    private void getChildrenData(TextView txt) {

        myRef.child("Users").child(client.getId()).child("myChildren").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Child> allChildren = new ArrayList<Child>();

                //get form DB
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Child child = dataSnapshot.getValue(Child.class);
                    allChildren.add(child);
                }
                StringBuffer sbChildren = new StringBuffer("\n\nYour Children : \n\n");

                // The view how children will be printed
                for (int i = 0; i < allChildren.toArray().length; i++) {
                    child = allChildren.get(i);

                    // Set specific text for value "experience"
                    String experience = "";
                    if(child.getHasExperience()){
                        experience = "Has Experience";
                    }
                    else {
                        experience = "None" ;
                    }

                    sbChildren.append(i + 1 + ") "
                            + "Child Name: " +  child.getName() + "\n"
                            + "Gender: " +  child.getGender() + "\n"
                            + "Age: " +  child.getAge() + "\n"
                            + "Has Experience: " + experience  + "\n"
                            + "Explain: " + child.getLittleExplain()  + "\n"
                            + "\n\n");
                }

                txt.setText(clientData + sbChildren.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
