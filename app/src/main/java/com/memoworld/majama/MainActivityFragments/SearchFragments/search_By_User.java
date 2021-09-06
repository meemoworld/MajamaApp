package com.memoworld.majama.MainActivityFragments.SearchFragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.memoworld.majama.AllModals.UserDetailsRealtime;
import com.memoworld.majama.R;


public class search_By_User extends Fragment {

    private static final String TAG = "search_By_User";
    private RecyclerView recyclerView;
    private EditText searchBox;
    private FirebaseRecyclerAdapter<UserDetailsRealtime, UserViewHolder> adapter;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

    public search_By_User() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search__by__user, container, false);
        searchBox = view.findViewById(R.id.edit_text_search_user);
        recyclerView = view.findViewById(R.id.recyclerView_search_by_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LoadUsers("", "");


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Log.d(TAG, "afterTextChanged: " + s.toString());
//                Query query = ff.collection("Users")
//                        .orderBy("firstName", Query.Direction.ASCENDING)
//                        .startAt("firstName", s.toString());
//                FirestoreRecyclerOptions<UserDetailsFirestore> options1 = new FirestoreRecyclerOptions.Builder<UserDetailsFirestore>().setQuery(query, UserDetailsFirestore.class).build();
//                adapter.updateOptions(options1);
//
//                adapter.notifyDataSetChanged();
//                recyclerView.notify();
//                adapter.updateOptions(options);
            }
        });
        return view;
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        //        private CircleImageView profileImages;
        private TextView userName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.page_name_single_view);
        }
    }

    public void LoadUsers(String lower, String upper) {

        Query query = databaseReference.orderByChild("personalInfo/username")
                .startAt("").endAt("" + "\uf8ff");

        FirebaseRecyclerOptions<UserDetailsRealtime> options = new FirebaseRecyclerOptions.Builder<UserDetailsRealtime>().setQuery(query, UserDetailsRealtime.class).build();
        adapter = new FirebaseRecyclerAdapter<UserDetailsRealtime, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull UserDetailsRealtime model) {
                holder.userName.setText(model.getUsername());
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pages_single_item, parent, false);
                return new UserViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}