package com.memoworld.majama.MainActivityFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.memoworld.majama.R;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private static final String TAG = "Home";
    private arrayAdapter adapter;
    private int i;
    List<MainCardModel> al;
    SwipeFlingAdapterView flingContainer;

    private final DatabaseReference postReference = FirebaseDatabase.getInstance().getReference().child("Posts");
    private final DatabaseReference pageReference = FirebaseDatabase.getInstance().getReference().child("Pages");

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        flingContainer = view.findViewById(R.id.frame);
        Toolbar toolbar = view.findViewById(R.id.main_appBar_home);

        toolbar.inflateMenu(R.menu.topbar_main_menu);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);


        al = new ArrayList<>();

        al.add(new MainCardModel("php"));
        al.add(new MainCardModel("c"));
        LoadData();
//        al.add(new MainCardModel("python"));
//        al.add(new MainCardModel("java"));
//        al.add(new MainCardModel("javascript"));
//        al.add(new MainCardModel("css"));
//        al.add(new MainCardModel("html"));

        adapter = new arrayAdapter(getActivity(), R.layout.single_view_card, al);

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

    public void LoadData() {
        postReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String pageId = snapshot.child("pageId").getValue().toString();
                pageReference.child(pageId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String name=dataSnapshot.child("name").getValue().toString();
                        Log.d(TAG, "onSuccess: "+name);

                        al.add(new MainCardModel(name));
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public class arrayAdapter extends ArrayAdapter<MainCardModel> {

        public arrayAdapter(@NonNull Context context, int resource, List<MainCardModel> items) {
            super(context, resource, items);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.chat_main_menu) {
            Toast.makeText(getContext(), "In progress..", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}