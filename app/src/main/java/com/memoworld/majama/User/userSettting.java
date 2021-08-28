package com.memoworld.majama.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.memoworld.majama.R;

public class userSettting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settting);

        getSupportActionBar().setTitle(" Settings");
        getSupportActionBar().setLogo(R.drawable.ic_baseline_settings_24);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}