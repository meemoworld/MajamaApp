package com.memoworld.majama.MainActivityFragments.SearchFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.memoworld.majama.AllModals.UserDetailsFirestore;
import com.memoworld.majama.R;


public class search_By_User extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchBox;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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


        Query query = ff.collection("Users");
        FirestoreRecyclerOptions<UserDetailsFirestore> options = new FirestoreRecyclerOptions.Builder<UserDetailsFirestore>().setQuery(query, UserDetailsFirestore.class).build();
        adapter = new FirestoreRecyclerAdapter<UserDetailsFirestore, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull UserDetailsFirestore model) {
                String snapShotId = getSnapshots().getSnapshot(position).getId();
                if (snapShotId.equals(userId)) {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
                else
                holder.userName.setText(model.getFirstName());

            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.pages_single_item, parent, false);

                return new UserViewHolder(view1);
            }
        };
        recyclerView.setAdapter(adapter);
        return view;
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        //        private CircleImageView profileImages;
        private TextView userName;
        private RelativeLayout relativeLayout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.page_name_single_view);
            relativeLayout = itemView.findViewById(R.id.relative_layout_single_view);
        }
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