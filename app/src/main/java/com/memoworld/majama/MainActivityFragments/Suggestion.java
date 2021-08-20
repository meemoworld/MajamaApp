package com.memoworld.majama.MainActivityFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.memoworld.majama.MainActivityFragments.SuggestionFragments.SuggestionsAdapter;
import com.memoworld.majama.R;

public class Suggestion extends Fragment {

public static final String TAG="TAG";

    public Suggestion() {
        // Required empty public constructor
    }

    TabLayout tabLayout;

    ViewPager2 viewPager2;
    SuggestionsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_suggestion, container, false);
        tabLayout=view.findViewById(R.id.tabLayout);
        viewPager2=view.findViewById(R.id.viewPagerSuggestion);

        if(getFragmentManager()==null){
            Log.i(TAG, "onCreateView: NULL found");
        }
        adapter=new SuggestionsAdapter(getChildFragmentManager(),getLifecycle());
        viewPager2.setAdapter(adapter);
tabLayout.addTab(tabLayout.newTab().setText("Movies"));
        tabLayout.addTab(tabLayout.newTab().setText("Date"));

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