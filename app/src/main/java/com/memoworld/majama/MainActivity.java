package com.memoworld.majama;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.memoworld.majama.MainActivityFragments.Home;
import com.memoworld.majama.MainActivityFragments.Search;
import com.memoworld.majama.MainActivityFragments.Suggestion;
import com.memoworld.majama.MainActivityFragments.User;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationBar;
    private Fragment mainFragment = null;
    private FragmentTransaction fragmentTransaction;

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_menu:
                    mainFragment = new Home();
                    switchFragment(mainFragment);
                    return true;
                case R.id.suggestions_menu:
                    mainFragment = new Suggestion();
                    switchFragment(mainFragment);
                    return true;
                case R.id.search_menu:
                    mainFragment = new Search();
                    switchFragment(mainFragment);
                    return true;
                case R.id.user_menu:
                    mainFragment = new User();
                    switchFragment(mainFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bottomNavigationBar = findViewById(R.id.bottom_nav_main);
        bottomNavigationBar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        switchFragment(new Home());
    }

    private void switchFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}