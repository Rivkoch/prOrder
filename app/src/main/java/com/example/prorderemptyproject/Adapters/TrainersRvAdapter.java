package com.example.prorderemptyproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prorderemptyproject.Models.Trainer;
import com.example.prorderemptyproject.R;

import java.util.List;

public class TrainersRvAdapter extends RecyclerView.Adapter<TrainersRvAdapter.TrainersViewHolder> {


    private List<Trainer> trainerList;
    private TrainerClicklistener trainerClicklistener;

    public TrainersRvAdapter(List<Trainer> trainerList,TrainerClicklistener trainerClicklistener) {
        this.trainerList = trainerList;
        this.trainerClicklistener = trainerClicklistener;
    }

    @NonNull
    @Override
    public TrainersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrainersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrainersViewHolder holder, int position) {
        Trainer trainer = trainerList.get(position);
        holder.bind(trainer,trainerClicklistener);
    }

    @Override
    public int getItemCount() {
        return trainerList.size();
    }

    static class TrainersViewHolder extends RecyclerView.ViewHolder {
        private TextView trainerRow_txt_name, trainerRow_txt_gender;

        public TrainersViewHolder(@NonNull View itemView) {
            super(itemView);
            trainerRow_txt_name = itemView.findViewById(R.id.trainerRow_txt_name);
            trainerRow_txt_gender = itemView.findViewById(R.id.trainerRow_txt_gender);
        }
        public void bind(Trainer trainer,TrainerClicklistener trainerClicklistener) {
            trainerRow_txt_name.setText("Trainer's Name: " + trainer.getName());
            trainerRow_txt_gender.setText("Gender: " + trainer.getGender());
            itemView.setOnClickListener(view -> trainerClicklistener.onClick(trainer));
        }
    }
}
