package com.example.prorderemptyproject.ClientFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prorderemptyproject.Database.AllOrdersData;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;

public class ClientAllOrdersFragment extends Fragment {
    private Client currentClient;
    private TextView orders_txt_viewOrders;
    private AllOrdersData ordersData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_client_orders,container,false);
        ordersData = new AllOrdersData();
        currentClient = Client.retrieveCurrentClient();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orders_txt_viewOrders = view.findViewById(R.id.orders_txt_viewOrders);

        // Admin reads Every thing , simple user reads his only
        ordersData.readAllOrders(orders_txt_viewOrders);


    }
}
