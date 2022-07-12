package com.example.prorderemptyproject.ClientsActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.Models.Order;
import com.example.prorderemptyproject.R;
import com.google.gson.Gson;

import java.util.Calendar;

public class OrderCompletionActivity extends AppCompatActivity {
    private TextView orderComplete_txt_TrainerName, orderComplete_txt_ClientTitle, orderComplete_txt_childName, orderComplete_txt_date, orderComplete_BTN_BACK;
    private Client client;

    Order order;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completion);

        client = Client.retrieveCurrentClient();
        findViews();
        showOrderData();
        setListener();
    }

    private void setListener() {
        orderComplete_BTN_BACK.setOnClickListener(v -> {
            startActivity(new Intent(OrderCompletionActivity.this, ClientMainActivity.class));
        });
    }

    private void showOrderData() {
        if(getIntent()!=null) {
            String orderJson = getIntent().getStringExtra("order");
            order = new Gson().fromJson(orderJson,Order.class);
            orderComplete_txt_ClientTitle.setText("Thank you, " + order.getClientName()
                    + "\nYour Order Saved!");
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(order.getDate());
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Define the way to show the time
            String showMinute, showHour;
            if(minute < 10){
                showMinute = "0" + minute;
            }else {
                showMinute = "" + minute;
            }

            if(hour < 10){
                showHour = "0" + hour;
            }else {
                showHour = "" + hour;
            }
            orderComplete_txt_TrainerName.setText("Trainer: " + order.getTrainerName());
            orderComplete_txt_date.setText("Date: " + day + "/" + (month+1) + "/" + year + " " +  showHour + ":" + showMinute);
            String name;
            if(client.getIsParent()){
                name = order.getChildName();
            }
            else {
                name = order.getClientName();
            }

            orderComplete_txt_childName.setText("Registered: " + name);

        }

    }

    private void findViews() {

        orderComplete_txt_TrainerName = findViewById(R.id.orderComplete_txt_TrainerName);
        orderComplete_txt_ClientTitle = findViewById(R.id.orderComplete_txt_ClientTitle);
        orderComplete_txt_childName = findViewById(R.id.orderComplete_txt_childName);
        orderComplete_txt_date = findViewById(R.id.orderComplete_txt_date);
        orderComplete_BTN_BACK = findViewById(R.id.orderComplete_BTN_BACK);
    }
}
