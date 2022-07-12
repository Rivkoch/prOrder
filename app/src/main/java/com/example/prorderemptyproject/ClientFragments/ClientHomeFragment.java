package com.example.prorderemptyproject.ClientFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prorderemptyproject.ClientsActivities.AddTrainerActivity;
import com.example.prorderemptyproject.ClientsActivities.ShowTrainerActivity;
import com.example.prorderemptyproject.Database.ClientHomeData;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;
import com.example.prorderemptyproject.databinding.FragmentClientHomeBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientHomeFragment extends Fragment {

    private FragmentClientHomeBinding binding;
    private ClientHomeData homeData;
    private MaterialButton childrenHome_BTN_add_trainer, clientHome_BTN_showTrainer, clientHome_BTN_deleteOrder;
    private TextView clientHome_text_home, clientHome_text_viewOrder,clientHome_text_nextOrder;
    private Client currentClient;
    private ScrollView clientHome_sv_nextOrder;
    private LinearLayout clientHome_LL_viewNextOrder,clientHome_LL_adminButtons;

    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_client_home,container,false);
        binding = FragmentClientHomeBinding.inflate(getLayoutInflater());
        homeData = new ClientHomeData();
        currentClient = Client.retrieveCurrentClient();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        manageButtons();
        setListeners();

        homeData.readInHome(clientHome_text_home);
        if(currentClient.getNumOfOrders() != 0) {
            homeData.setNextOrder(clientHome_text_viewOrder);
        }
        else {
            clientHome_text_viewOrder.setText("No \"Next Orders\" exists.\nCreate new one by pressing \"+\"");
        }

    }

    private void setListeners() {
        childrenHome_BTN_add_trainer.setOnClickListener(v->{
            if (currentClient != null && currentClient.isAdmin()) {
                Toast.makeText(getActivity(), "Add pressed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ClientHomeFragment.this.getActivity(), AddTrainerActivity.class);
                startActivity(intent);
            }
        });

        clientHome_BTN_showTrainer.setOnClickListener(v->{
            if (currentClient != null && currentClient.isAdmin()) {
                Intent intent = new Intent(ClientHomeFragment.this.getActivity(), ShowTrainerActivity.class);
                startActivity(intent);
            }
        });

        clientHome_BTN_deleteOrder.setOnClickListener(v -> {
            // todo: delete shown order
            if(currentClient.getNumOfOrders() > 1) {
                homeData.deleteTheNextOrder();
                homeData.setNextOrder(clientHome_text_viewOrder);
            }
            else if(currentClient.getNumOfOrders() == 1){
                currentClient.unsetNumOfOrders();
                myRef.child("Users").child(currentClient.getId()).child("numOfOrders").setValue(currentClient.getNumOfOrders());

                homeData.deleteTheNextOrder();
                clientHome_text_viewOrder.setText("No \"Next Orders\" exists.\nCreate new one by pressing \"+\"");
            }
            else{
                Toast.makeText(getContext(), "Nothing To Delete...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manageButtons() {
        if (currentClient != null && currentClient.isAdmin()) {
            //admin
            clientHome_sv_nextOrder.setVisibility(View.GONE);
            clientHome_text_nextOrder.setVisibility(View.GONE);
            clientHome_BTN_deleteOrder.setVisibility(View.GONE);
            clientHome_LL_viewNextOrder.setVisibility(View.GONE);
        }else{
            //no admin
            childrenHome_BTN_add_trainer.setVisibility(View.GONE);
            clientHome_BTN_showTrainer.setVisibility(View.GONE);
            clientHome_LL_adminButtons.setVisibility(View.GONE);
        }
    }

    private void findViews(View view) {
        childrenHome_BTN_add_trainer = (MaterialButton) view.findViewById(R.id.clientHome_BTN_add_trainer);
        clientHome_BTN_showTrainer = (MaterialButton) view.findViewById(R.id.clientHome_BTN_showTrainer);
        clientHome_BTN_deleteOrder = (MaterialButton) view.findViewById(R.id.clientHome_BTN_deleteOredr);
        clientHome_text_home = view.findViewById(R.id.clientHome_text_home);
        clientHome_text_nextOrder = view.findViewById(R.id.clientHome_text_nextOrder);
        clientHome_text_viewOrder = view.findViewById(R.id.clientHome_text_viewOrder);
        clientHome_sv_nextOrder = view.findViewById(R.id.clientHome_sv_nextOrder);
        clientHome_LL_viewNextOrder = view.findViewById(R.id.clientHome_LL_viewNextOrder);
        clientHome_LL_adminButtons = view.findViewById(R.id.clientHome_LL_adminButtons);


    }

}
