package com.memoworld.majama.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.memoworld.majama.AllModals.PostImage;
import com.memoworld.majama.AllModals.UserDetailsFirestore;
import com.memoworld.majama.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitingProfile extends AppCompatActivity {


    private CircleImageView userProfileImage;
    private TextView username, followersCount, followingCount, about;
    private RecyclerView recyclerViewPost;
    private Button followButton;
    boolean isFollowing;
    FirestoreRecyclerAdapter<PostImage, MyViewHolder> adapter;
    Toolbar toolbar;
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
        new Thread(fetchImages).start();
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

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
    Runnable fetchImages = new Runnable() {
        @Override
        public void run() {
            Query query = firebaseFirestore.collection("Users").document(profileId).collection("Posts").orderBy("timestamp", Query.Direction.ASCENDING);
            FirestoreRecyclerOptions<PostImage> options = new FirestoreRecyclerOptions.Builder<PostImage>().setQuery(query, PostImage.class).build();

            adapter = new FirestoreRecyclerAdapter<PostImage, MyViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull PostImage model) {
                    Glide.with(VisitingProfile.this).load(model.getImageUrl()).placeholder(R.drawable.coder).into(holder.imageView);
                }

                @NonNull
                @Override
                public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_post_view, parent, false);
                    return new MyViewHolder(view1);
                }
            };
            adapter.startListening();
            recyclerViewPost.setAdapter(adapter);
        }
    };

    private void initialize() {
        username = findViewById(R.id.name_txt_box_visiting);
        userProfileImage = findViewById(R.id.image_view_visiting_profile);
        about = findViewById(R.id.about_visiting_profile);
        followersCount = findViewById(R.id.count_follower_visiting_profile);
        followingCount = findViewById(R.id.count_following_visiting_profile);
        recyclerViewPost = findViewById(R.id.recycler_view_visiting_profile_posts);
        recyclerViewPost.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        followButton = findViewById(R.id.follow_btn_visiting_profile);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_post_image_single_view);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}