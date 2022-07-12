package com.example.prorderemptyproject.ClientsActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.prorderemptyproject.ClientFragments.AddOrderFragment;
import com.example.prorderemptyproject.ClientFragments.ClientAccountFragment;
import com.example.prorderemptyproject.ClientFragments.ClientHomeFragment;
import com.example.prorderemptyproject.ClientFragments.ClientAllOrdersFragment;
import com.example.prorderemptyproject.ClientFragments.ClientEditFragment;
import com.example.prorderemptyproject.ClientFragments.ManageRequestsFragment;
import com.example.prorderemptyproject.Abstract.AbstractMainActivity;
import com.example.prorderemptyproject.Models.Client;
import com.example.prorderemptyproject.R;
import com.example.prorderemptyproject.SplashScreenActivity;
import com.example.prorderemptyproject.WelcomeActivity;
import com.example.prorderemptyproject.databinding.ActivityClientMainBinding;


public class ClientMainActivity extends AbstractMainActivity {

    private ActivityClientMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_client_main);
        setBottomNavigation();

    }

    private void setBottomNavigation() {
        binding = ActivityClientMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.clientMainBottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.allOrders:
                    replaceFragment(new ClientAllOrdersFragment());
                    break;
                case R.id.myAccount:
                    replaceFragment(new ClientAccountFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new ClientEditFragment());
                    break;
                case R.id.home: {
                    replaceFragment(new ClientHomeFragment());
                }
                    break;
            }

            return true;
        });

        binding.clientMainFloatingActionButtonLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client client = Client.retrieveCurrentClient();
                if(!client.isAdmin()) {
                    replaceFragment(new AddOrderFragment());
                }
                else {
                    replaceFragment(new ManageRequestsFragment());
                }
            }
        });
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_client_main;
    }

    @Override
    protected Fragment getFragment() {
        return new ClientHomeFragment();
    }

    @Override
    protected Intent setNewIntent() {
        return new Intent(ClientMainActivity.this, SplashScreenActivity.class);
    }
}
