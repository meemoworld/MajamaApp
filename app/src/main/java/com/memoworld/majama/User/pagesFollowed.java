package com.memoworld.majama.User;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.memoworld.majama.R;

public class pagesFollowed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages_followed);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Pages");
    }
}