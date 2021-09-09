package com.memoworld.majama.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.memoworld.majama.AllModals.PageInfoFirestore;
import com.memoworld.majama.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitingMyPage extends AppCompatActivity {

    private CircleImageView pageProfileImage;
    private TextView pageName, balance, aboutPage, followers;
    private RecyclerView recyclerViewPagePost;
    private Button upload;
    String pageId, userId;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private PageInfoFirestore pageInfoFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_my_pages);
        initialize();
        pageId = getIntent().getExtras().getString("pageId");
        new Thread(runnable).start();
        assert auth.getCurrentUser() != null;
        userId = auth.getCurrentUser().getUid();
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
            // TODO: FETCH IMAGES FROM FIRESTORE
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
    }

    public void UploadPost(View view) {
        Intent intent = new Intent(VisitingMyPage.this, PageImagePost.class);
        intent.putExtra("pageId", pageId);
        startActivity(intent);
    }
}