
package com.example.prorderemptyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {
    public static Activity singleSplashActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleSplashActivity =this;
        setContentView (R.layout.activity_splash);

        Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
        singleSplashActivity.finish();
        startActivity(intent);
    }



}
