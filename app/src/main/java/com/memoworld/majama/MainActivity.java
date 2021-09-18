package com.memoworld.majama;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.memoworld.majama.MainActivityFragments.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private long pressedTime = 0;

    ViewPager viewPager;
    BottomNavigationView bottomNavigationBar;
    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_menu:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.suggestions_menu:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.search_menu:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.user_menu:
                    viewPager.setCurrentItem(3);
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationBar = findViewById(R.id.bottom_nav_main);
        bottomNavigationBar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        viewPager = findViewById(R.id.mainActivityPager);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationBar.getMenu().findItem(R.id.home_menu).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationBar.getMenu().findItem(R.id.suggestions_menu).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationBar.getMenu().findItem(R.id.search_menu).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationBar.getMenu().findItem(R.id.user_menu).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}