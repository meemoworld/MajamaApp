package com.memoworld.majama.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.memoworld.majama.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class visitingProfile extends AppCompatActivity {


    private CircleImageView userProfileImage;
    private TextView username,followersCount,followingCount,about;
    private RecyclerView recyclerViewPost;
    private Button followButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_profile);
        initialize();

    }

    private void initialize() {
        username=findViewById(R.id.name_txt_box_visiting);
        userProfileImage=findViewById(R.id.image_view_visiting_profile);
        about=findViewById(R.id.about_visiting_profile);
        followersCount=findViewById(R.id.count_follower_visiting_profile);
        followingCount=findViewById(R.id.count_following_visiting_profile);
        recyclerViewPost=findViewById(R.id.recycler_view_visiting_profile_posts);
        followButton=findViewById(R.id.follow_btn_visiting_profile);

    }
}