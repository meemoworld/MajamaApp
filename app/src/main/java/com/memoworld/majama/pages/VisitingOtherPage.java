package com.memoworld.majama.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.memoworld.majama.AllModals.PageImagePostFirestore;
import com.memoworld.majama.AllModals.UserPageFollowing;
import com.memoworld.majama.R;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitingOtherPage extends AppCompatActivity {


    private CircleImageView pageProfileImage;
    private TextView pageName, pageFollowers, aboutPage;
    private Button followPage;
    private RecyclerView recyclerViewPagePost;
    private FirestoreRecyclerAdapter<PageImagePostFirestore, MyViewHolder> adapter;
    String pageId, ownerId, userId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private UserPageFollowing userPageFollowing;
    private Long originalFollowers;


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
                    originalFollowers = (Long) snapshot.child("followers").getValue();
                    String followersString = String.valueOf(snapshot.child("followers").getValue());
                    if (profileImageUrl.isEmpty()) {
                        Glide.with(VisitingOtherPage.this).load(profileImageUrl).into(pageProfileImage);
                    }
                    pageName.setText(name);
                    aboutPage.setText(about);
                    pageFollowers.setText(followersString);

                    database.getReference().child("Users").child(userId).child("PageFollowing").child(pageId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                followPage.setText("Following");
                                followPage.setClickable(true);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            followPage.setText("Follow");
                            followPage.setClickable(true);
                        }
                    });

                    new Thread(runnable).start();
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

            Query query = FirebaseFirestore.getInstance().collection("Users").document(ownerId).collection("Pages").document(pageId).collection("Posts");
            FirestoreRecyclerOptions<PageImagePostFirestore> recyclerOptions = new FirestoreRecyclerOptions.Builder<PageImagePostFirestore>()
                    .setQuery(query, PageImagePostFirestore.class).build();
            adapter = new FirestoreRecyclerAdapter<PageImagePostFirestore, MyViewHolder>(recyclerOptions) {
                @Override
                protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull PageImagePostFirestore model) {
                    Glide.with(VisitingOtherPage.this).load(model.getImageUrl()).placeholder(R.drawable.ruko_jara).into(holder.imageView);
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
        pageProfileImage = findViewById(R.id.image_view_visiting_page);
        pageName = findViewById(R.id.page_name_txt_box_visit_page);
        pageFollowers = findViewById(R.id.count_follower_visiting_page);
        followPage = findViewById(R.id.follow_btn_visiting_page);
        recyclerViewPagePost = findViewById(R.id.recycler_view_visiting_page);
//        recyclerViewPagePost.setLayoutManager(new CustomGridLayoutManager(this, 3));
        recyclerViewPagePost.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        aboutPage = findViewById(R.id.about_page_visiting_page);
    }

    public void FollowClick(View view) {

        followPage.setClickable(false);
        Map<String, Object> updates = new HashMap<>();
        // Setting state of button as false for follow and true for following
        boolean originalState = !followPage.getText().equals("Follow");


        if (originalState) {
            updates.put("followers", ServerValue.increment(-1));
            database.getReference().child("Users").child(userId).child("PageFollowing").child(pageId).removeValue();
            followPage.setText("Follow");
            pageFollowers.setText(String.valueOf(originalFollowers - 1));
        } else {
            updates.put("followers", ServerValue.increment(1));
            Map<String, Object> nameUpdate = new HashMap<>();
            nameUpdate.put(pageId, true);
            database.getReference().child("Users").child(userId).child("PageFollowing").updateChildren(nameUpdate);
            pageFollowers.setText(String.valueOf(originalFollowers + 1));
            followPage.setText("Following");
        }

        database.getReference("Pages").child(pageId).updateChildren(updates);
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
        adapter.stopListening();
    }
}