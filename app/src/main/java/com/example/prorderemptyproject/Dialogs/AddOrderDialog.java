package com.example.prorderemptyproject.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prorderemptyproject.Adapters.TrainerClicklistener;
import com.example.prorderemptyproject.Adapters.TrainersRvAdapter;
import com.example.prorderemptyproject.Models.Trainer;
import com.example.prorderemptyproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddOrderDialog extends DialogFragment {

    private RecyclerView rvTrainers;
    private TrainersRvAdapter rvAdapter;
    private TrainerClicklistener trainerClicklistener;

    // Constructor
    public AddOrderDialog(TrainerClicklistener trainerClicklistener) {
        this.trainerClicklistener = trainerClicklistener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.choose_instructor_dialog,null,false);

        // Set the view in alert dialog
        rvTrainers = v.findViewById(R.id.rvTrainersChoice);
        rvTrainers.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fill Trainer's array
        FirebaseDatabase.getInstance()
                .getReference("Trainers")
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<Trainer > trainerList = new ArrayList<>();
                    for(DataSnapshot trainerSnap : dataSnapshot.getChildren()) {
                        trainerList.add(trainerSnap.getValue(Trainer.class));
                    }
                    rvAdapter = new TrainersRvAdapter(trainerList,trainerClicklistener);
                    rvTrainers.setAdapter(rvAdapter);
                }).addOnFailureListener(exception -> Toast.makeText(getContext(),exception.getMessage(),Toast.LENGTH_SHORT).show());

        return new AlertDialog.Builder(getContext())
                .setTitle("prOrder")
                .setView(v)
                .create();
    }
}
