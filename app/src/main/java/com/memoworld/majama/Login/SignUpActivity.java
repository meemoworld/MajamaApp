package com.memoworld.majama.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.memoworld.majama.R;
import com.memoworld.majama.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_sign_up);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
    }
}