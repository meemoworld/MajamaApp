package com.memoworld.majama.Util;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomLinearLayoutManager extends LinearLayoutManager {
    private static final String TAG = "CustomLinearLayout";
    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Inconsistency detected");
        }

    }
}
