package com.memoworld.majama.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.memoworld.majama.AllModals.PageImagePostFirestore;
import com.memoworld.majama.AllModals.PageInfoFirestore;
import com.memoworld.majama.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitingMyPage extends AppCompatActivity {

    private CircleImageView pageProfileImage;
    private TextView pageName, balance, aboutPage, followers;
    private RecyclerView recyclerViewPagePost;
    String pageId, userId;
    private Button upload;
    private Toolbar toolbar;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private PageInfoFirestore pageInfoFirestore;
    private FirestoreRecyclerAdapter<PageImagePostFirestore, MyViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_my_pages);
        initialize();
        pageId = getIntent().getExtras().getString("pageId");
        new Thread(runnable).start();
        assert auth.getCurrentUser() != null;
        userId = auth.getCurrentUser().getUid();

        toolbar.setTitle("Page");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        firebaseFirestore.collection("Users").document(userId).collection("Pages").document(pageId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null)
                    pageInfoFirestore = value.toObject(PageInfoFirestore.class);
                else
                    pageInfoFirestore = new PageInfoFirestore();
                Glide.with(VisitingMyPage.this).load(pageInfoFirestore.getImageUrl()).into(pageProfileImage);
                pageName.setText(pageInfoFirestore.getName());
                balance.setText(String.valueOf(pageInfoFirestore.getBalancing()));
                aboutPage.setText(pageInfoFirestore.getAbout());

                toolbar.setTitle(pageInfoFirestore.getName());
                toolbar.setTitleTextColor(getResources().getColor(R.color.white));

                new Thread(getRecyclerViewImages).start();


            }
        });

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            databaseReference.child("Pages").child(pageId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    String followersString = String.valueOf(dataSnapshot.child("followers").getValue());
                    followers.setText(followersString);
                }
            });
        }
    };

    Runnable getRecyclerViewImages = new Runnable() {
        @Override
        public void run() {
            Query query = firebaseFirestore.collection("Users")
                    .document(userId)
                    .collection("Pages")
                    .document(pageId)
                    .collection("Posts").orderBy("timestamp", Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<PageImagePostFirestore> recyclerOptions = new FirestoreRecyclerOptions.Builder<PageImagePostFirestore>()
                    .setQuery(query, PageImagePostFirestore.class).build();

            adapter = new FirestoreRecyclerAdapter<PageImagePostFirestore, MyViewHolder>(recyclerOptions) {
                @Override
                protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull PageImagePostFirestore model) {
                    Glide.with(VisitingMyPage.this).load(model.getImageUrl()).placeholder(R.drawable.ruko_jara).into(holder.imageView);
                }

                @NonNull
                @Override
                public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_post_view, parent, false);
                    return new MyViewHolder(view);
                }
            };
            adapter.startListening();
            recyclerViewPagePost.setAdapter(adapter);
        }
    };

    private void initialize() {
        pageProfileImage = findViewById(R.id.image_view_my_page_visiting);
        pageName = findViewById(R.id.name_txt_box_my_page);
        balance = findViewById(R.id.balance_my_page);
        aboutPage = findViewById(R.id.about_visiting_my_page);
        followers = findViewById(R.id.count_follower_visiting_my_page);
        recyclerViewPagePost = findViewById(R.id.recycler_view_visiting_my_page);

        upload = findViewById(R.id.upload_btn_my_page);
        toolbar = findViewById(R.id.app_bar_visiting_pages);

        recyclerViewPagePost.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        Button upload = findViewById(R.id.upload_btn_my_page);

    }

    public void UploadPost(View view) {
        Intent intent = new Intent(VisitingMyPage.this, PageImagePost.class);
        intent.putExtra("pageId", pageId);
        intent.putExtra("pageImageUrl", pageInfoFirestore.getImageUrl());
        intent.putExtra("pageName", pageInfoFirestore.getName());
        startActivity(intent);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_post_image_single_view);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}