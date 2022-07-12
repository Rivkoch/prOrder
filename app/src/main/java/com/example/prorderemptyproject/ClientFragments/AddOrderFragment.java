package com.example.prorderemptyproject.ClientFragments;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.prorderemptyproject.Adapters.TrainerClicklistener;
import com.example.prorderemptyproject.ClientsActivities.OrderCompletionActivity;
import com.example.prorderemptyproject.Dialogs.AddOrderDialog;
import com.example.prorderemptyproject.Models.Child;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.Models.Order;
import com.example.prorderemptyproject.Models.Trainer;
import com.example.prorderemptyproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class AddOrderFragment extends Fragment implements TrainerClicklistener {

    static private final int FIELD = 1;
    private int timeField = 0, trainerField = 0, dateField = 0, currentOrder;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private Button addOrder_BTN_chooseTrainer, addOrder_BTN_chooseDate, addOrder_BTN_chooseTime, addOrder_BTN_submit;
    private TextView addOrder_txt_chooseTrainer, addOrder_txt_chooseDate, addOrder_txt_chooseTime;
    private TextView addOrder_txt_pointTime, addOrder_txt_pointDate, addOrder_txt_pointTrainer, addOrder_txt_chooseChild;
    private AddOrderDialog addOrderDialog;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Order order;
    private Calendar c;
    private String chosenTime;
    private static int dayHours = 24, minutesInterval = 30;
    private Spinner addOrder_sp_chooseChild;
    private String childrenArray[];
    private LinearLayout layout_addChildToOrder;
    private Client client;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_order,container,false);
        c = Calendar.getInstance();
        order = new Order();
        client = Client.retrieveCurrentClient();
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setOrder();
        setListeners();
        setSpinner();

    }
    private void setSpinner() {
        setSpinnerValues();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, childrenArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addOrder_sp_chooseChild.setAdapter(adapter);
        addOrder_sp_chooseChild.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        childrenArray = new String[client.getMyChildren().size() + 1];
        childrenArray[0] = "  Tap  ";
        int i = 1;

        // Fill the array
        for (HashMap<String, Object> childObject : client.getMyChildren().values()) {
            Child c = new Child(childObject);
            childrenArray[i++] = c.getName();
        }
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setListeners() {

        addOrder_BTN_submit.setOnClickListener(v -> {
            if(timeField >= FIELD && trainerField >= FIELD && dateField >= FIELD) {
                addOrder_BTN_submit.setBackgroundColor(R.color.bottom_item_background);
                setAndSaveTheOrder();
            }else{
                Toast.makeText(getContext(), "You mast fill all required data above", Toast.LENGTH_SHORT).show();
                return;
            }
            
        });

        addOrder_BTN_chooseTime.setOnClickListener(v -> {
            addOrder_BTN_chooseTime.setBackgroundColor(R.color.bottom_item_background);

            if(timePickerDialog==null) {
                timePickerDialog = new TimePickerDialog(getContext(), (timeView, hourOfDay, minute) -> {

                    if ((minute == 0 || minute == 30) && (hourOfDay >= 9 && hourOfDay < 20)) {
                        // Define the way to show the time
                        String showMinute, showHour;
                        if(minute == 0){
                            showMinute = "0" + minute;
                        }else {
                            showMinute = "" + minute;
                        }

                        if(hourOfDay < 10){
                            showHour = "0" + hourOfDay;
                        }else {
                            showHour = "" + hourOfDay;
                        }
                        // Define the order value
                        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(showHour));
                        c.set(Calendar.MINUTE, Integer.parseInt(showMinute));
                        c.set(Calendar.SECOND,0);
                        c.set(Calendar.MILLISECOND,0);
                        order.setDate(c.getTimeInMillis());

                        // Define text view
                        chosenTime = showHour + ":" + showMinute;
                        // Set text in the text view
                        ++timeField;
                        if(client.getIsParent()) {
                            addOrder_txt_chooseChild.setVisibility(View.VISIBLE);
                        }
                        addOrder_txt_chooseTime.setText("Chosen Time: " + chosenTime);

                    }
                    else {
                        Toast.makeText(getContext(), "Sorry! Minutes must be 0 or 30!!!\nAnd hours between 09 to 20 !", Toast.LENGTH_LONG).show();
                        addOrder_txt_chooseTime.setText("");
                        timeField--;
                    }

                },0,0,true);
            }
            timePickerDialog.setTitle("Select a Time");
            timePickerDialog.show();

        });

        addOrder_BTN_chooseDate.setOnClickListener(v -> {
            addOrder_BTN_chooseDate.setBackgroundColor(R.color.bottom_item_background);
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            AtomicReference<String> chosenDate = new AtomicReference<>("");

            if(datePickerDialog==null) {
                datePickerDialog = new DatePickerDialog(getContext());

                // Set minimum day that will be shown
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1001);
                //todo: date = start from TOMORROW!!
                Calendar today = Calendar.getInstance();
                today = (Calendar) today.clone();

                datePickerDialog.setOnDateSetListener((cView, year, month, dayOfMonth) -> {

                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    c.set(Calendar.MONTH,month);
                    order.setDate(c.getTimeInMillis());
                    chosenDate.set(dayOfMonth + "-" + (month + 1) + "-" + year);
                    addOrder_txt_chooseDate.setText("Chosen date: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                    ++dateField;
                    addOrder_txt_pointTime.setVisibility(View.VISIBLE);

                    datePickerDialog.dismiss();
                });
            }
            datePickerDialog.show();
        });

        addOrder_BTN_chooseTrainer.setOnClickListener(v -> {
            addOrder_BTN_chooseTrainer.setBackgroundColor(R.color.bottom_item_background);
            if(addOrderDialog==null)
                addOrderDialog = new AddOrderDialog(this);
            addOrderDialog.show(getChildFragmentManager(),"choose_trainer");

        });

    }

    private void setAndSaveTheOrder() {
        order.setId();

        if(client.getIsParent()) {
            if(addOrder_sp_chooseChild.getSelectedItem().toString().equals("  Tap  ")){
                Toast.makeText(getContext(), "Please register your child", Toast.LENGTH_SHORT).show();
            }else {
                order.setChildName(addOrder_sp_chooseChild.getSelectedItem().toString());
                saveOrderToDB();
            }
        }
        else{
            saveOrderToDB();
        }

    }

    private void saveOrderToDB() {
        client.setNumOfOrders();
        currentOrder = client.getNumOfOrders();
        databaseReference.child("Users").child(client.getId()).child("numOfOrders").setValue(currentOrder);

        // Save order as client object value
        databaseReference.child("Users").child(order.getClientId())
                .child("clientOrders").child(order.getId())
                .setValue(order).addOnFailureListener(exception -> {
                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(success -> {
                    System.out.println("Indication: order saved in client");
                });

        // Save order as trainer object value
        databaseReference.child("Trainers").child(order.getTrainerId())
                .child("trainerOrders").child(order.getId())
                .setValue(order).addOnFailureListener(exception -> {
                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(success -> {
                    System.out.println("Indication: order saved in trainer");
                });

        // Save order as table
        databaseReference.child("Orders").child(order.getId())
                .setValue(order).addOnFailureListener(exception -> {
                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(success -> {
                    Client.updateCurrentClient(client);
                    Intent i = new Intent(getContext(), OrderCompletionActivity.class);
                    i.putExtra("order",new Gson().toJson(order));
                    startActivity(i);
                    Toast.makeText(getContext(), "Order Added successfully", Toast.LENGTH_SHORT).show();
                    addOrder_txt_chooseDate.setText("");
                    addOrder_txt_chooseTrainer.setText("");
                    addOrder_txt_chooseTime.setText("");
                    addOrder_txt_pointDate.setVisibility(View.INVISIBLE);
                    addOrder_txt_pointTime.setVisibility(View.INVISIBLE);
                    addOrder_txt_chooseChild.setVisibility(View.INVISIBLE);

                });

    }

    private void setOrder() {

        order  = new Order();
        order.setClientId(Client.retrieveCurrentClient().getId());
        order.setClientName(Client.retrieveCurrentClient().getName());
    }

    private void findViews(View view) {

        addOrder_BTN_chooseTrainer = view.findViewById(R.id.addOrder_BTN_chooseTrainer);
        addOrder_txt_chooseTrainer = view.findViewById(R.id.addOrder_txt_chooseTrainer);
        addOrder_BTN_chooseDate = view.findViewById(R.id.addOrder_BTN_chooseDate);
        addOrder_txt_chooseDate = view.findViewById(R.id.addOrder_txt_chooseDate);
        addOrder_txt_chooseTime = view.findViewById(R.id.addOrder_txt_chooseTime);
        addOrder_BTN_submit = view.findViewById(R.id.addOrder_BTN_submit);
        addOrder_BTN_chooseTime = view.findViewById(R.id.addOrder_BTN_chooseTime);
        addOrder_txt_pointTime = view.findViewById(R.id.addOrder_txt_pointTime);
        addOrder_txt_pointDate = view.findViewById(R.id.addOrder_txt_pointDate);
        addOrder_txt_pointTrainer = view.findViewById(R.id.addOrder_txt_pointTrainer);
        addOrder_txt_chooseChild = view.findViewById(R.id.addOrder_txt_chooseChild);
        addOrder_txt_chooseChild.setVisibility(View.INVISIBLE);
        addOrder_txt_pointTime.setVisibility(View.INVISIBLE);
        addOrder_txt_pointDate.setVisibility(View.INVISIBLE);

        addOrder_sp_chooseChild = (Spinner) view.findViewById(R.id.addOrder_sp_chooseChild);
        layout_addChildToOrder = view.findViewById(R.id.layout_addChildToOrder);
        if(!client.getIsParent()){
            layout_addChildToOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(Trainer trainer) {
        ++trainerField;
        addOrder_txt_chooseTrainer.setText("Trainer: " + trainer.getName());
        addOrder_txt_pointDate.setVisibility(View.VISIBLE);
        order.setTrainerId(trainer.getId());
        order.setTrainerName(trainer.getName());
        addOrderDialog.dismiss();
    }

}
