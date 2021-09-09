package com.memoworld.majama.pages;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.memoworld.majama.AllModals.UserPageFollowing;
import com.memoworld.majama.R;
import com.memoworld.majama.Util.CustomGridLayoutManager;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitingOtherPage extends AppCompatActivity {


    private CircleImageView pageProfileImage;
    private TextView pageName, pageFollowers, aboutPage;
    private Button followPage;
    private RecyclerView recyclerViewPagePost;
    String pageId, ownerId, userId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private UserPageFollowing userPageFollowing;
    boolean originalState, newState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_page);
        initialize();
        followPage.setClickable(false);
        pageId = getIntent().getStringExtra("pageId");
        assert auth.getCurrentUser() != null;
        userId = auth.getCurrentUser().getUid();

        database.getReference("Pages").child(pageId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrl = String.valueOf(snapshot.child("imageUrl").getValue());
                    String about = String.valueOf(snapshot.child("about").getValue());
                    String name = String.valueOf(snapshot.child("name").getValue());
                    ownerId = String.valueOf(snapshot.child("ownerUid").getValue());
                    String followersString = String.valueOf(snapshot.child("followers").getValue());
                    if (profileImageUrl.isEmpty()) {
                        Glide.with(VisitingOtherPage.this).load(profileImageUrl).into(pageProfileImage);
                    }
                    pageName.setText(name);
                    aboutPage.setText(about);
                    pageFollowers.setText(followersString);

                    firebaseFirestore.collection("Users").document(userId).collection("Followings").document("pages").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value != null) {
                                userPageFollowing = value.toObject(UserPageFollowing.class);
                            } else
                                userPageFollowing = new UserPageFollowing();
                            if (userPageFollowing != null && userPageFollowing.getFollowings().contains(pageId)) {
                                followPage.setText("Following");
                            }
                        }
                    });
                    new Thread(runnable).start();
                    originalState = !followPage.getText().equals("Follow");
                    followPage.setClickable(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO:  FETCH IMAGES for RECYCLER IMAGES
            CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users").document(ownerId).collection("Pages").document(pageId).collection("PagePosts");
        }
    };

    private void initialize() {
        pageProfileImage = findViewById(R.id.image_view_visiting_page);
        pageName = findViewById(R.id.page_name_txt_box_visit_page);
        pageFollowers = findViewById(R.id.count_follower_visiting_page);
        followPage = findViewById(R.id.follow_btn_visiting_page);
        recyclerViewPagePost = findViewById(R.id.recycler_view_visiting_page);
        recyclerViewPagePost.setLayoutManager(new CustomGridLayoutManager(this, 3));
//        recyclerViewPagePost.setLayoutManager(new GridLayoutManager(this, 3));
        aboutPage = findViewById(R.id.about_page_visiting_page);
    }

    public void FollowClick(View view) {
        Map<String, Object> updates = new HashMap<>();
        // Setting state of button as false for follow and true for following

        if (originalState) {
            updates.put("followers", ServerValue.increment(-1));
            database.getReference("Pages").child(pageId).updateChildren(updates);
            originalState = true;
            if (userPageFollowing != null)
                userPageFollowing.getFollowings().remove(pageId);
            followPage.setText("Follow");
        } else {
            updates.put("followers", ServerValue.increment(1));
            database.getReference("Pages").child(pageId).updateChildren(updates);
            originalState = false;
            if (userPageFollowing != null)
                userPageFollowing.getFollowings().add(pageId);
            followPage.setText("Following");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseFirestore.collection("Users").document(userId).collection("Followings").document("pages").update("followings", userPageFollowing.getFollowings());

    }
}