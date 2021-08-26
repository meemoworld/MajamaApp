package com.memoworld.majama;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Account_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        getSupportActionBar().hide();
    }
}