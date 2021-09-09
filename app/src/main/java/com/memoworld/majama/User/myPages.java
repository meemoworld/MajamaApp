package com.memoworld.majama.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.memoworld.majama.AllModals.PageInfoFirestore;
import com.memoworld.majama.R;
import com.memoworld.majama.Util.CustomLinearLayoutManager;
import com.memoworld.majama.pages.CreatePage;
import com.memoworld.majama.pages.VisitingMyPage;

import de.hdodenhof.circleimageview.CircleImageView;

public class myPages extends AppCompatActivity {

    private static final String TAG = "myPages";
    private RecyclerView recyclerView;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    String userid;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter<PageInfoFirestore, UserPagesViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pages);
        recyclerView = findViewById(R.id.recycler_view_my_pages);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        userid = auth.getCurrentUser().getUid();
        new Thread(runnable).start();
    }

    public void CreateNewPage(View view) {
        startActivity(new Intent(myPages.this, CreatePage.class));
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Query query = ff.collection("Users").document(userid).collection("Pages").orderBy("timestamp", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<PageInfoFirestore> options = new FirestoreRecyclerOptions.Builder<PageInfoFirestore>().setQuery(query, PageInfoFirestore.class).build();
            adapter = new FirestoreRecyclerAdapter<PageInfoFirestore, UserPagesViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull UserPagesViewHolder holder, int position, @NonNull PageInfoFirestore model) {
//                    Log.d(TAG, "onBindViewHolder: "+position);
                    String pageId = getSnapshots().getSnapshot(position).getId();
                    if (model.getImageUrl() != null)
                        Glide.with(myPages.this).load(model.getImageUrl()).placeholder(R.drawable.user).into(holder.profileImage);
                    holder.userName.setText(model.getName());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(myPages.this, VisitingMyPage.class);
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
    };

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
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}