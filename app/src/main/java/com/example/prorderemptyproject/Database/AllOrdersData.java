package com.example.prorderemptyproject.Database;

import android.text.format.DateFormat;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.Models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllOrdersData {
    private Client client;
    private Order order;
    private StringBuffer bufferOrders;
    private String orderSrtData;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference ref = firebaseDatabase.getReference();

    public void readAllOrders(TextView txt) {
        client = Client.retrieveCurrentClient();

        // Define the exact ref to reach
        if(client.isAdmin()){
            ref = ref.child("Orders");
        }
        else {
            ref = ref.child("Users").child(client.getId()).child("clientOrders");
        }

        // Collect orders from DB to print in TextView
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Create new array
                List<Order> allOrders = new ArrayList<Order>();

                // Get orders form DB
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    allOrders.add(order);
                }

                // Set Title
                if(client.isAdmin()){
                    bufferOrders = new StringBuffer("\n\nAll Orders : \n\n");
                }else {
                    bufferOrders = new StringBuffer("\n\nYour Orders : \n\n");
                }

                // Define the view how children will be printed
                for (int i = 0; i < allOrders.toArray().length; i++) {
                    order = allOrders.get(i);

                    String dateString = DateFormat.format("dd/MM/yyyy", new Date(order.getDate())).toString();
                    String timeString = DateFormat.format("HH:mm", new Date(order.getDate())).toString();

                    bufferOrders.append(i + 1 + ") "
                            + "Order date: " + dateString + "\n"
                            + "Order time: " + timeString + "\n"
                            + "Trainer: " + order.getTrainerName());


                    if(client.getIsParent()){
                        bufferOrders.append("\n" + "Registered: " + order.getChildName());
                    }
                    else if(client.isAdmin()){
                        if(order.getChildName().equals("None")){
                            bufferOrders.append("\n" + "Registered (client): " + order.getClientName());

                        }
                        else {
                            bufferOrders.append("\n" + "Registered (child): " + order.getChildName());
                        }
                    }
                    else {
                        bufferOrders.append("\n" + "Registered: " + order.getClientName());
                    }

                    bufferOrders.append("\n\n");

                }

                if(allOrders.size() == 0){
                    bufferOrders.append("No orders exists yet...");
                }

                txt.setText(bufferOrders.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
