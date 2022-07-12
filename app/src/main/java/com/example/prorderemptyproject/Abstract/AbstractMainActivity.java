package com.example.prorderemptyproject.Abstract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.prorderemptyproject.R;

public abstract class AbstractMainActivity extends AppCompatActivity {
    private int counterBack = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragment(getFragment());
        setContentView(getLayoutResourceId());
    }

    protected abstract int getLayoutResourceId();
    protected abstract Fragment getFragment();

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // Activate the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeMenu_logout: {
                Intent intent = setNewIntent();
                startActivity(intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract Intent setNewIntent();

    // To go back "OUT" from app with phone
    @Override
    public void onBackPressed() {
        counterBack++;
        if(counterBack == 2){
            //super.onBackPressed();
            goToHomeScreen();
        }
    }

    // "BACK" main page activity
    public void goToHomeScreen() {
        counterBack=0;
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}