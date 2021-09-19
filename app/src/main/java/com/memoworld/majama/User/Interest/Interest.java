package com.memoworld.majama.User.Interest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.memoworld.majama.AllModals.EachTag;
import com.memoworld.majama.AllModals.Tag;
import com.memoworld.majama.MainActivity;
import com.memoworld.majama.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interest extends AppCompatActivity implements InterestTagItemListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private InterestAdapter interestAdapter;
    private ChipGroup chipGroup;
    private EditText userInput;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Tag tag = new Tag();
    private ArrayList<EachTag> tagArrayList = new ArrayList<>();
    private List<String> userInterest;
    private HashMap<String, String> hashMapList;
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrest);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userInterest = new ArrayList<>();
        recyclerView = findViewById(R.id.interestRecyclerView);
        userInput = findViewById(R.id.edit_text_search_tag);
        chipGroup = findViewById(R.id.interest_chip_group);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        getContacts();
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userString = s.toString();
                List<EachTag> newTags = new ArrayList<>();
                for (EachTag allTags : tagArrayList) {
                    if (allTags.getTagName().contains(userString)) {
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
            } else if (userInterest.size() > 8) {
                Toast.makeText(this, "You can select a maximum of 8 tags only", Toast.LENGTH_SHORT).show();
                return;
            } else
                userInterest.add(tagName);
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
        firebaseFirestore.collection("Tag").document("tag").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    tag = documentSnapshot.toObject(Tag.class);
                    if (tag != null) {
                        hashMapList = tag.getTagmap();
                        for (Map.Entry<String, String> entry : hashMapList.entrySet()) {
                            tagArrayList.add(new EachTag(entry.getKey(), entry.getValue()));
                        }
                        interestAdapter = new InterestAdapter(Interest.this, tagArrayList);
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

    public void ContinueInterest(View View) {

        if (userInterest.size() < 5) {
            Toast.makeText(this, "Please Select atleast 5 tags ", Toast.LENGTH_SHORT).show();
            return;
        }
        int i = 0;
        StringBuilder builder = new StringBuilder();
        for (i = 0; i < userInterest.size() - 1; i++) {
            builder.append(userInterest.get(i));
            builder.append(",");
        }
        builder.append(userInterest.get(i));

        Map<String, Object> interest = new HashMap<>();
        interest.put("interest", builder);

        Thread updateInterest = new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("Users").document(mUser.getUid()).update("interest", userInterest);
                database.getReference("Users").child(mUser.getUid()).child("personalInfo").updateChildren(interest);
            }
        });
        updateInterest.setPriority(1);
        updateInterest.start();
        startActivity(new Intent(Interest.this, MainActivity.class));
        finish();

    }
}