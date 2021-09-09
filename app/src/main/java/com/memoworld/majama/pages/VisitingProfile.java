package com.memoworld.majama.pages;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.memoworld.majama.AllModals.UserDetailsFirestore;
import com.memoworld.majama.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitingProfile extends AppCompatActivity {


    private CircleImageView userProfileImage;
    private TextView username, followersCount, followingCount, about;
    private RecyclerView recyclerViewPost;
    private Button followButton;
    boolean isFollowing;

    String profileId, userId;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    UserDetailsFirestore userDetailsFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_profile);
        profileId = getIntent().getExtras().getString("profileId");
        assert FirebaseAuth.getInstance().getCurrentUser() != null;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore.collection("Users").document(profileId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    userDetailsFirestore = value.toObject(UserDetailsFirestore.class);
                } else
                    userDetailsFirestore = new UserDetailsFirestore();
                Glide.with(VisitingProfile.this).load(userDetailsFirestore.getProfileImageUrl()).into(userProfileImage);
                String name = userDetailsFirestore.getFirstName() + " " + userDetailsFirestore.getLastName();
                username.setText(name);
                followersCount.setText(String.valueOf(userDetailsFirestore.getFollowers()));
                followingCount.setText(String.valueOf(userDetailsFirestore.getFollowing()));
                about.setText(userDetailsFirestore.getAbout());
            }
        });
        initialize();
    }

    Runnable followButtonCheck = new Runnable() {
        @Override
        public void run() {
            firebaseFirestore.collection("Users").document(userId).collection("Followings").document("users").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                }
            });
        }
    };

    private void initialize() {
        username = findViewById(R.id.name_txt_box_visiting);
        userProfileImage = findViewById(R.id.image_view_visiting_profile);
        about = findViewById(R.id.about_visiting_profile);
        followersCount = findViewById(R.id.count_follower_visiting_profile);
        followingCount = findViewById(R.id.count_following_visiting_profile);
        recyclerViewPost = findViewById(R.id.recycler_view_visiting_profile_posts);
        followButton = findViewById(R.id.follow_btn_visiting_profile);

    }
}