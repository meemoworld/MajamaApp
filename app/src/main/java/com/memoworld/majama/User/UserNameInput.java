package com.memoworld.majama.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.memoworld.majama.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserNameInput extends AppCompatActivity {

    private TextInputLayout inputUserName;
    private MaterialButton btnDatePicker,save;
    Map<String, Object> usernameMap;
    String username,birthday=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_input);

        inputUserName=findViewById(R.id.input_username);
        btnDatePicker=findViewById(R.id.btn_date_pick);
        save=findViewById(R.id.save_username_birthday);
        FirebaseFirestore.getInstance().collection("AllNames").document("users").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    usernameMap=task.getResult().getData();
                    if(usernameMap==null)
                        usernameMap=new HashMap<>();
                }
            }
        });
        if(inputUserName.getEditText()!=null)
        inputUserName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(usernameMap.containsKey(s.toString())){
                    Toast.makeText(UserNameInput.this, "This username already exist", Toast.LENGTH_SHORT).show();
                    username=null;
                }
                else{
                    username=s.toString();
                }

            }
        });



        Calendar calendar=Calendar.getInstance();

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentYear=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(UserNameInput.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthday=SimpleDateFormat.getDateInstance().format(calendar.getTime());
                        btnDatePicker.setText(birthday);

                    }
                },currentYear,month,day);
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username==null)
                    Toast.makeText(UserNameInput.this,"Please user another username",Toast.LENGTH_SHORT).show();
                else if(birthday==null)
                {
                    Toast.makeText(UserNameInput.this, "Please select the birthday", Toast.LENGTH_SHORT).show();
                }
                else{
                    // TODO: Save to database
                }
            }
        });
    }
}