package com.memoworld.majama.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.memoworld.majama.R;

public class NewLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        getSupportActionBar().hide();
    }
}