package com.memoworld.majama.Login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.memoworld.majama.AllModals.UserNameDetails;
import com.memoworld.majama.R;
import com.memoworld.majama.databinding.ActivityLoginBinding;

import java.util.ArrayList;

public class login extends AppCompatActivity {

    ActivityLoginBinding binding;
    Button loginBtn, signup, forgot;
    ImageView logo;
    TextInputLayout username, password;
    TextView appName;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String nameOfUser, passwordOfUser;
    private final FirebaseFirestore ff = FirebaseFirestore.getInstance();
    public static final String TAG = "MAJAMA";

    private UserNameDetails usernameDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initialize();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, SignUpActivity.class);

                Pair<View, String>[] pairs = new Pair[6];
                pairs[0] = new Pair<>(logo, "logo_image");
                pairs[1] = new Pair<>(appName, "app_name");
                pairs[2] = new Pair<>(username, "user_mail");
                pairs[3] = new Pair<>(password, "pass_trans");
                pairs[4] = new Pair<>(loginBtn, "login_trans");
                pairs[5] = new Pair<>(signup, "sign_trans");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });

    }

    private void initialize() {
        loginBtn = findViewById(R.id.btn_login);

        signup = findViewById(R.id.btn_new_user);
        logo = findViewById(R.id.logoImage);

        username = findViewById(R.id.username_text_input_login);
        password = findViewById(R.id.password_text_input_login);
        forgot = findViewById(R.id.btn_forgot_password);
        appName = findViewById(R.id.logoName);
        usernameDetails=new UserNameDetails();
    }

    public void ForgotPassword(View view) {


    }

    public void Login(View view) {
        if (username.getEditText() != null && password.getEditText() != null) {
            nameOfUser = username.getEditText().getText().toString();
            passwordOfUser = password.getEditText().getText().toString();
        }
        if (nameOfUser.isEmpty()) {
            ShowError(username, "Please give your unique id");
        } else if (passwordOfUser.isEmpty()) {
            ShowError(password, "Please fill this field");
        } else {
            ff.collection("UserNameDetails").document(nameOfUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful() && task.getResult()!=null){
                        usernameDetails=task.getResult().toObject(UserNameDetails.class);
                        if(usernameDetails!=null && passwordOfUser.equals(usernameDetails.getPassword())){

                            // TODO : Sign in With google with given Email
                        }
                        else{
                            ShowError(password,"Please Check Your Password");
                        }
                    }
                    else{
                        ShowError(username, "Please Check Your Unique Id");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ShowError(username, "Please Check Your Unique Id");
                }
            });
        }
    }

    public void ShowError(TextInputLayout field, String message) {
        field.setError(message);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}