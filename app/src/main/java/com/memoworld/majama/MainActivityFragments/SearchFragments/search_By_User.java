package com.memoworld.majama.MainActivityFragments.SearchFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.memoworld.majama.AllModals.RealTimeUser;
import com.memoworld.majama.R;
import com.memoworld.majama.Util.CustomLinearLayoutManager;
import com.memoworld.majama.pages.VisitingProfile;

import de.hdodenhof.circleimageview.CircleImageView;


public class search_By_User extends Fragment {

    private static final String TAG = "search_By_User";
    private RecyclerView recyclerView;
    private EditText searchBox;
    private FirebaseRecyclerAdapter<RealTimeUser, UserViewHolder> adapter;
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
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getContext()));
        LoadUsers();


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "onTextChanged: " + s.toString());
                Query query = databaseReference.orderByChild("username").startAt(s.toString()).endAt(s.toString() + "\uf8ff").limitToFirst(15);
                FirebaseRecyclerOptions<RealTimeUser> options = new FirebaseRecyclerOptions.Builder<RealTimeUser>().setQuery(query, RealTimeUser.class).build();
                adapter.updateOptions(options);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
//                LoadUsers(s.toString());
            }
        });
        return view;
    }

    private static class UserViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private final TextView userName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.page_name_single_view);
            profileImage = itemView.findViewById(R.id.page_image_single_view);
        }
    }

    public void LoadUsers() {
        Query query = databaseReference.orderByChild("username").limitToFirst(15);
        FirebaseRecyclerOptions<RealTimeUser> options = new FirebaseRecyclerOptions.Builder<RealTimeUser>().setQuery(query, RealTimeUser.class).build();
        adapter = new FirebaseRecyclerAdapter<RealTimeUser, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull RealTimeUser model) {
                String currentUserId = getSnapshots().getSnapshot(position).getKey();
                Log.d(TAG, "onBindViewHolder: " + model.getUsername());
                assert currentUserId != null;
                if (currentUserId.equals(userId)) {
                    holder.itemView.setVisibility(View.INVISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                } else
                    holder.userName.setText(model.getPersonalInfo().getUsername());
                Glide.with(getContext()).load(model.getPersonalInfo().getProfileImageUrl()).circleCrop().placeholder(R.drawable.coder).into(holder.profileImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), VisitingProfile.class);
                        intent.putExtra("profileId", currentUserId);
                        startActivity(intent);
                    }
                });
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