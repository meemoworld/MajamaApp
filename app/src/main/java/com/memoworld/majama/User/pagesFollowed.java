package com.memoworld.majama.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.memoworld.majama.R;

public class pagesFollowed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages_followed);

        getSupportActionBar().setTitle("Pages");
    }
}