package com.memoworld.majama.MainActivityFragments.SuggestionFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SuggestionsAdapter extends FragmentStateAdapter {
    public SuggestionsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MovieFragment();
            case 1:
                return new DateFragment();
        }
        return new MovieFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
