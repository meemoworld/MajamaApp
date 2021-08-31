package com.memoworld.majama.User;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.memoworld.majama.LoginInstructionSplash.NewLogin;
import com.memoworld.majama.R;

import java.util.HashMap;
import java.util.Map;

public class UserNameInput extends AppCompatActivity {

    private TextInputLayout inputUserName;
    private MaterialButton save;
    Map<String, Object> usernameMap;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_input);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        inputUserName = findViewById(R.id.input_username);

        save = findViewById(R.id.save_username_birthday);
        FirebaseFirestore.getInstance().collection("AllNames").document("users").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    usernameMap = task.getResult().getData();
                    if (usernameMap == null)
                        usernameMap = new HashMap<>();
                }
            }
        });
        if (inputUserName.getEditText() != null)
            inputUserName.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    username = s.toString();
                    for (Map.Entry<String, Object> entry : usernameMap.entrySet()) {
                        if (entry.getValue().equals(s.toString())) {
                            Toast.makeText(UserNameInput.this, "This username already exist", Toast.LENGTH_SHORT).show();
                            username = null;

                        }
//                            usernameMap.put("username", true);
                    }
                }
            });
        Thread updateUsernameMap = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore.getInstance().collection("AllNames").document("users").set(usernameMap);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username == null)
                    Toast.makeText(UserNameInput.this, "Please user another username", Toast.LENGTH_SHORT).show();
                else {
                    usernameMap.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), username);
                    updateUsernameMap.start();
                    Intent intent = new Intent(UserNameInput.this, Account_Details.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(UserNameInput.this, NewLogin.class));
            finish();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(UserNameInput.this, NewLogin.class));
            finish();
        }

    }
}