package com.memoworld.majama.MainActivityFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.memoworld.majama.R;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    public Home() {
        // Required empty public constructor
    }
    private arrayAdapter adapter;
    private int i;
    List<MainCardModel> al;
    SwipeFlingAdapterView flingContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        flingContainer=view.findViewById(R.id.frame);
        al = new ArrayList<>();
        al.add(new MainCardModel("php"));
        al.add(new MainCardModel("c"));
        al.add(new MainCardModel("python"));
        al.add(new MainCardModel("java"));
        al.add(new MainCardModel("javascript"));
        al.add(new MainCardModel("css"));
        al.add(new MainCardModel("html"));

        adapter = new arrayAdapter(getActivity(), R.layout.single_view_card,al);

        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                al.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(getActivity(), "Left Swiped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(getActivity(), "Right Swiped", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                al.add(new MainCardModel("XML ".concat(String.valueOf(i))));
                adapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float v) {

            }
        });
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public class arrayAdapter extends ArrayAdapter<MainCardModel> {

        public arrayAdapter(@NonNull Context context, int resource,List<MainCardModel>items) {
            super(context, resource,items);
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            MainCardModel card_item = getItem(position);
            if (convertView == null)
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.single_view_card, parent, false);
            TextView name = convertView.findViewById(R.id.pageName_text_view);
//            ImageView imageView = convertView.findViewById(R.id.card_image);
            name.setText(card_item.getPageName());
//        if (card_item.getProfileImageUrl() != null)
//            Glide.with(context).load(card_item.getProfileImageUrl()).into(imageView);
            return convertView;
        }
    }
}