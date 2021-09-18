package com.memoworld.majama.pages;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.memoworld.majama.AllModals.PostImage;
import com.memoworld.majama.AllModals.UserDetailsFirestore;
import com.memoworld.majama.R;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitingProfile extends AppCompatActivity {


    private CircleImageView userProfileImage;
    private TextView username, followersCount, followingCount, about;
    private RecyclerView recyclerViewPost;
    private Button followButton;
    boolean isFollowing;
    FirestoreRecyclerAdapter<PostImage, MyViewHolder> adapter;

    String profileId, userId;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    UserDetailsFirestore userDetailsFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_profile);
        profileId = getIntent().getExtras().getString("profileId");
        assert FirebaseAuth.getInstance().getCurrentUser() != null;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        new Thread(fetchImages).start();
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
            database.getReference().child("Users").child(userId).child("UserFollowings").child(profileId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // TODO : USER IS FOLLOWING
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // TODO: USER HAS TO FOLLOW
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerViewPost.setAdapter(adapter);
                }
            });

        }
    };

    public void FollowUser(View view) {
        followButton.setClickable(false);
        Map<String, Object> updates = new HashMap<>();
        // Setting state of button as false for follow and true for following
        boolean originalState = !followButton.getText().equals("Follow");
/*

        if (originalState) {
            firebaseFirestore.collection("Users").document(userId).update("following", ServerValue.increment(-1));
            firebaseFirestore.collection("Users").document(profileId).update("followers", ServerValue.increment(-1));
            database.getReference().child("UserExtraDetails").child(userId).child("UserFollowings").child(profileId).removeValue();
            database.getReference().child("UserExtraDetails").child(userId).child("UserFollowings").child(profileId).removeValue();
            followButton.setText("Follow");
        } else {
//            updates.put("followers", ServerValue.increment(1));
//            Map<String, Object> nameUpdate = new HashMap<>();
//            nameUpdate.put(pageId, true);
//            database.getReference().child("Users").child(userId).child("PageFollowing").updateChildren(nameUpdate);
//            pageFollowers.setText(String.valueOf(originalFollowers + 1));
//            followPage.setText("Following");
        }


 */
//        database.getReference("Pages").child(pageId).updateChildren(updates);
    }

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