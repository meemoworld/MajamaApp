package com.memoworld.majama.pages;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
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
import com.memoworld.majama.AllModals.PostImage;
import com.memoworld.majama.AllModals.UserDetailsFirestore;
import com.memoworld.majama.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitingProfile extends AppCompatActivity {


    private static final String TAG = "VisitingProfile";
    private CircleImageView userProfileImage;
    private TextView username, followersCount, followingCount, about;
    private RecyclerView recyclerViewPost;
    private Button followButton;
    FirestoreRecyclerAdapter<PostImage, MyViewHolder> adapter;
    Toolbar toolbar;
    String profileId, userId;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    UserDetailsFirestore userDetailsFirestore;
    private Integer state = 1;
    String[] texts;
    DatabaseReference reference;
    DatabaseReference reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_profile);
        profileId = getIntent().getExtras().getString("profileId");

        initialize();

        assert FirebaseAuth.getInstance().getCurrentUser() != null;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference2 = database.getReference().child("UserExtraDetails").child(profileId).child("UserToUserConnection");
        reference = database.getReference().child("UserExtraDetails").child(userId).child("UserToUserConnection");

        new Thread(fetchImages).start();
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        new Thread(followButtonCheck).start();
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

    }

    Runnable followButtonCheck = new Runnable() {
        @Override
        public void run() {
            reference.child("AllConnections").child(profileId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d(TAG, "onSuccess: SnapShotExist");
                        String data = dataSnapshot.getValue().toString();
                        Log.d(TAG, "onSuccess: " + data);
                        if (data == null) {
                            state = 1;
                            return;
                        }
                        switch (data) {
                            case "Friend":
                                state = 2;
                                followButton.setText(texts[1]);
                                break;
                            case "Request Sent":
                                state = 3;
                                followButton.setText(texts[2]);
                                break;
                            case "Request Pending":
                                state = 4;
                                followButton.setText(texts[3]);
                                break;
                            default:
                                state = 1;
                                break;
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: SnapShot Does not exists");
                    state = 1;
                }
            });
            followButton.setClickable(true);
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

    private void initialize() {
        username = findViewById(R.id.name_txt_box_visiting);
        userProfileImage = findViewById(R.id.image_view_visiting_profile);
        about = findViewById(R.id.about_visiting_profile);
        followersCount = findViewById(R.id.count_follower_visiting_profile);
        followingCount = findViewById(R.id.count_following_visiting_profile);
        recyclerViewPost = findViewById(R.id.recycler_view_visiting_profile_posts);
        toolbar = findViewById(R.id.app_bar_visiting_profile);
        recyclerViewPost.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        followButton = findViewById(R.id.follow_btn_visiting_profile);

        texts = new String[]{"SEND FRIEND REQUEST", "UN FRIEND", "CANCEL REQUEST", "ACCEPT", "Friend", "Request Sent", "Request Pending"};

    }

    public void sendRequest(View view) {
        /* state     Button TEXT
            1        SEND FRIEND REQUEST
            2        UN FRIEND
            3        CANCEL FRIEND REQUEST
            4        ACCEPT FRIEND REQUEST
         */
        followButton.setClickable(false);
        switch (state) {
            case 1:
                followButton.setText(texts[2]);
                sendFriendRequest();
                break;
            case 2:
                followButton.setText(texts[0]);
                unFriendAUser();
                break;
            case 3:
                followButton.setText(texts[0]);
                cancelSentRequest();
                break;
            case 4:
                followButton.setText(texts[1]);
                acceptFriendRequest();
        }
    }

    private void acceptFriendRequest() {
        reference.child("Pending_Request").child(profileId).removeValue();
        reference.child("AllConnections").child(profileId).setValue(texts[4]);
        reference.child("Friends").child(profileId).setValue(true);


        reference2.child("AllConnections").child(userId).setValue(texts[4]);
        reference2.child("Friends").child(userId).setValue(true);
    }

    private void cancelSentRequest() {
        reference2.child("Pending_Request").child(userId).removeValue();
        reference2.child("AllConnections").child(userId).removeValue();

        reference.child("AllConnections").child(profileId).removeValue();

    }

    private void unFriendAUser() {
        reference.child("AllConnections").child(profileId).removeValue();
        reference.child("Friends").child(profileId).removeValue();

        reference2.child("AllConnections").child(userId).removeValue();
        reference2.child("Friends").child(userId).removeValue();
    }

    private void sendFriendRequest() {
        reference.child("AllConnections").child(profileId).setValue("Request Sent");

        reference2.child("AllConnections").child(userId).setValue("Request Pending");
        reference2.child("Pending_Request").child(userId).setValue(true);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}