package com.memoworld.majama;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.memoworld.majama.databinding.ActivityLoginBinding;
import com.memoworld.majama.databinding.ActivitySignUpBinding;

public class login extends AppCompatActivity {

    ActivityLoginBinding binding;
    Button loginBtn , signup;
    ImageView logo;
    TextInputLayout username , password;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login);
        signup = findViewById(R.id.newUser);
        logo = findViewById(R.id.logo);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this , SignUpActivity.class);

                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View,String>(logo , "logo_image");
                pairs[1] = new Pair<View,String>(name , "app_name");
                pairs[2] = new Pair<View,String>(username , "user_trans");
                pairs[3] = new Pair<View,String>(password , "pass_trans");
                pairs[4] = new Pair<View,String>(loginBtn , "login_trans");
                pairs[5] = new Pair<View,String>(signup , "sign_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this,pairs);
                startActivity(intent , options.toBundle());

            }
        });

    }
}