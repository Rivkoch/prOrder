package com.example.prorderemptyproject.Database;

import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.Models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClientHomeData {

    private String welcomeString = "", clientName = "";
    private StringBuffer bufferOrders;
    private Client currentClient =  Client.retrieveCurrentClient();
    private Order order, nextOrder;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = firebaseDatabase.getReference();

    public void readInHome(TextView txt) {
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                getMessageInfo();
                txt.setText("\n" + welcomeString + " " + clientName);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });
    }

    private void getMessageInfo() {
        // Get current time
        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        // Build basic of the message
        if (hour24hrs >= 17 && hour24hrs <= 23)
            welcomeString = "Good Evening";
        else if (hour24hrs < 17 && hour24hrs > 12)
            welcomeString = "Good Afternoon";
        else if (hour24hrs <= 6 && hour24hrs >= 12)
            welcomeString = "Good Morning";
        else if (hour24hrs >= 00 && hour24hrs < 6)
            welcomeString = "Good Morning";
        else
            welcomeString = "Hi";

        // Get the name of the client who enters to the app
        if (currentClient != null)
            clientName = currentClient.getName();
    }

    public void setNextOrder(TextView txt) {
        myRef.child("Users").child(currentClient.getId()).child("clientOrders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Order> allOrders = new ArrayList<Order>();

                //get form DB
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    allOrders.add(order);
                }

                bufferOrders = new StringBuffer("\n\nYour next order : \n\n");

                // The view how children will be printed
                nextOrder = allOrders.get(0);
                for (int i = 1; i < allOrders.toArray().length; i++) {
                    order = allOrders.get(i);

                    if (order.getDate() < nextOrder.getDate()) {
                        nextOrder = order;
                    }
                }


                String dateString = DateFormat.format("dd/MM/yyyy", new Date(nextOrder.getDate())).toString();
                String timeString = DateFormat.format("HH:mm", new Date(nextOrder.getDate())).toString();

                bufferOrders.append("Order date: " + dateString + "\n"
                        + "Order time: " + timeString + "\n"
                        + "Trainer: " + nextOrder.getTrainerName() + "\n\n");

                txt.setText(bufferOrders.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteTheNextOrder() {
        myRef.child("Orders").child(nextOrder.getId()).removeValue();
        myRef.child("Users").child(currentClient.getId()).child("clientOrders").child(nextOrder.getId()).removeValue();
        myRef.child("Trainers").child(nextOrder.getTrainerId()).child("trainerOrders").child(nextOrder.getId()).removeValue();


    }
}


