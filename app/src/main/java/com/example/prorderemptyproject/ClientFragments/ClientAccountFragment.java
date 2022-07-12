package com.example.prorderemptyproject.ClientFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prorderemptyproject.Database.ClientAccountData;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;

public class ClientAccountFragment extends Fragment {
    private ClientAccountData accountData;
    private Client currentClient;
    private TextView account_txt_viewAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_client_account, container, false);
        accountData = new ClientAccountData();
        currentClient = Client.retrieveCurrentClient();

        findViews(rootView);
        return rootView;
    }

    private void findViews(ViewGroup rootView) {
        account_txt_viewAccount = rootView.findViewById(R.id.account_txt_viewAccount);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Read from DB User's data
        accountData.readAccount(account_txt_viewAccount);
    }
}