package com.memoworld.majama.MainActivityFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.memoworld.majama.R;

public class User extends Fragment {


    public User() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user, container, false);

        return view;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.profile_activity_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.profile_activity_menu) {
//            startActivity(new Intent(ProfileActivity.this, userSetting.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}