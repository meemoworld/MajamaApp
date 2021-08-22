package com.memoworld.majama.User;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.memoworld.majama.AllModals.Tag;
import com.memoworld.majama.R;

import java.util.ArrayList;
import java.util.List;

public class Interest extends AppCompatActivity implements InterestTagItemListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private InterestAdapter interestAdapter;
    private List<String> tags;
    private ChipGroup chipGroup;
    private EditText userInput;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private Tag tag = new Tag();
    private List<String> userInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrest);
//        if(getSupportActionBar()!=null){
//            getSupportActionBar().hide();
//        }
        userInterest = new ArrayList<>();
        recyclerView = findViewById(R.id.interestRecyclerView);
        userInput = findViewById(R.id.edit_text_search_tag);
        chipGroup = findViewById(R.id.interest_chip_group);
        tags = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerView.setHasFixedSize(true);
        getContacts();
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userString = s.toString();
                List<String> newTags = new ArrayList<>();
                for (String allTags : tags) {
                    if (allTags.contains(userString)) {
                        newTags.add(allTags);
                    }
                }
                interestAdapter = new InterestAdapter(Interest.this, newTags);
                recyclerView.setAdapter(interestAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public void onItemSelected(String tagName) {
        if (!userInterest.isEmpty()) {
            if (userInterest.contains(tagName)) {
                Toast.makeText(this, "You have already selected " + tagName, Toast.LENGTH_SHORT).show();
                return;
            } else userInterest.add(tagName);
        } else {
            userInterest.add(tagName);
        }
        Chip chip = new Chip(this);
        chip.setText(tagName);
        chip.setCloseIconVisible(true);
        chip.setCheckable(true);
//        chip.setBackgroundColor(getColor(R.color.lightBlue));
        chip.setClickable(false);
        chipGroup.addView(chip);
        chip.setOnCloseIconClickListener(this);
        chipGroup.setVisibility(View.VISIBLE);
    }

    public void getContacts() {

        firebaseFirestore.collection("Tags").document("tags").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    tag = documentSnapshot.toObject(Tag.class);
                    if (tag != null) {
                        tags = tag.getTagArray();
                        recyclerView.setAdapter(interestAdapter);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Chip chip = (com.google.android.material.chip.Chip) v;
        chipGroup.removeView(chip);
        userInterest.remove(chip.getText().toString());
    }

    public void Continue(View View){
        // TODO: This is to be completed
        Toast.makeText(this, "To complete", Toast.LENGTH_SHORT).show();
    }
}