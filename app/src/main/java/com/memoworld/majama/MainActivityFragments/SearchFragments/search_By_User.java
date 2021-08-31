package com.memoworld.majama.MainActivityFragments.SearchFragments;

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

import static android.content.ContentValues.TAG;


public class search_By_User extends Fragment {






    public search_By_User() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search__by__user, container, false);



        return view;
    }
}