package com.fsb.e_commerceapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.fsb.e_commerceapplication.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try {
            Thread.sleep(2000);
            startActivity(new Intent(this,HomeActivity.class));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}