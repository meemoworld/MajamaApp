package com.memoworld.majama.MainActivityFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.memoworld.majama.MainActivityFragments.SearchFragments.SearchAdapter;
import com.memoworld.majama.R;

import static android.content.ContentValues.TAG;

public class Search extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    SearchAdapter adapter;

    public Search() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);;

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPagerSearch);

        if(getFragmentManager()==null){
            Log.i(TAG, "onCreateView: NULL found");
        }

        adapter = new SearchAdapter(getChildFragmentManager(),getLifecycle());

        viewPager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("People"));
        tabLayout.addTab(tabLayout.newTab().setText("Pages"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return view;
    }
}