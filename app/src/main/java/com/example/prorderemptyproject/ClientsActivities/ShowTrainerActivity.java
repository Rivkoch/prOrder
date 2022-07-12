package com.example.prorderemptyproject.ClientsActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.prorderemptyproject.Models.Trainer;
import com.example.prorderemptyproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowTrainerActivity extends AppCompatActivity {

    private TextView showTrainers_TV_allTrainers;
    private Trainer trainer;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trainer);

        setDatabase();
        showTrainers_TV_allTrainers = findViewById(R.id.showTrainers_TV_allTrainers);
        showTrainers();
    }

    private void showTrainers() {
        databaseReference.child("Trainers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Trainer> allTrainers = new ArrayList<>();

                //get form DB
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Trainer trainer = dataSnapshot.getValue(Trainer.class);
                    allTrainers.add(trainer);
                }
                StringBuffer sb = new StringBuffer("\n\nAll Trainers In Business:\n\n");

                // Way all trainers will be shown
                for (int i = 0 ; i < allTrainers.size(); i++) {
                    trainer = allTrainers.get(i);

                    sb.append(i + 1 + ") "
                            + "Trainer Name: " +  trainer.getName() + "\n"
                            + "Gender: " +  trainer.getGender() + "\n"
                            + "Phone: " +  trainer.getPhone() + "\n"
                            + "Experience: " + trainer.getExperience()  + " years\n\n");

                    showTrainers_TV_allTrainers.setText(sb.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


}