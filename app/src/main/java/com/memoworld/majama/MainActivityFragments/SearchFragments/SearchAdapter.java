package com.memoworld.majama.MainActivityFragments.SearchFragments;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.memoworld.majama.MainActivityFragments.SuggestionFragments.DateFragment;
import com.memoworld.majama.MainActivityFragments.SuggestionFragments.MovieFragment;

public class SearchAdapter extends FragmentStateAdapter {
    public SearchAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new search_By_User();
            case 1:
                return new searchByPages();
        }
        return new search_By_User();
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
