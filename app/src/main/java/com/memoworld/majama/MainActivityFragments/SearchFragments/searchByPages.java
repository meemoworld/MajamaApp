package com.memoworld.majama.MainActivityFragments.SearchFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.memoworld.majama.AllModals.PageInfoRealTime;
import com.memoworld.majama.R;
import com.memoworld.majama.Util.CustomLinearLayoutManager;
import com.memoworld.majama.pages.VisitingMyPage;
import com.memoworld.majama.pages.VisitingOtherPage;

import de.hdodenhof.circleimageview.CircleImageView;

public class searchByPages extends Fragment {
    private static final String TAG = "searchByPages";
    private RecyclerView recyclerView;
    private EditText searchBox;
    private FirebaseRecyclerAdapter<PageInfoRealTime, UserPagesViewHolder> adapter;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Pages");


    public searchByPages() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_by_pages, container, false);
        searchBox = view.findViewById(R.id.edit_text_search_pages);
        recyclerView = view.findViewById(R.id.recycler_view_pages_search);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getContext()));

        LoadUsers();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Query query = databaseReference.orderByChild("name").startAt(s.toString()).endAt(s.toString() + "\uf8ff")
                        .limitToFirst(15);
                FirebaseRecyclerOptions<PageInfoRealTime> options = new FirebaseRecyclerOptions.Builder<PageInfoRealTime>().setQuery(query, PageInfoRealTime.class).build();
                adapter.updateOptions(options);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    public void LoadUsers() {
        Query query = databaseReference.orderByChild("name").limitToFirst(20);
        FirebaseRecyclerOptions<PageInfoRealTime> options = new FirebaseRecyclerOptions.Builder<PageInfoRealTime>().setQuery(query, PageInfoRealTime.class).build();
        adapter = new FirebaseRecyclerAdapter<PageInfoRealTime, UserPagesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserPagesViewHolder holder, int position, @NonNull PageInfoRealTime model) {
                String pageId = getSnapshots().getSnapshot(position).getKey();
                if (model.getImageUrl() != null)
                    Glide.with(getContext()).load(model.getImageUrl()).placeholder(R.drawable.user).into(holder.profileImage);
                holder.userName.setText(model.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (model.getOwnerUid().equals(userId)) {
                            intent = new Intent(getContext(), VisitingMyPage.class);
                        } else {
                            intent = new Intent(getContext(), VisitingOtherPage.class);
                        }
                        intent.putExtra("pageId", pageId);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public UserPagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pages_single_item, parent, false);
                return new UserPagesViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private static class UserPagesViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView profileImage;
        private final TextView userName;

        public UserPagesViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.page_name_single_view);
            profileImage = itemView.findViewById(R.id.page_image_single_view);
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