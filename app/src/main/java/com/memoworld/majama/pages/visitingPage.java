package com.memoworld.majama.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.memoworld.majama.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class visitingPage extends AppCompatActivity {


    private CircleImageView pageProfileImage;
    private TextView pageName,pageFollowers,aboutPage;
    private Button followPage;
    private RecyclerView recyclerViewPagePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_page);
        initialize();
    }

    private void initialize() {
        pageProfileImage =findViewById(R.id.image_view_visiting_page);
        pageName=findViewById(R.id.page_name_txt_box_visit_page);
        pageFollowers=findViewById(R.id.count_follower_visiting_page);
        followPage=findViewById(R.id.follow_btn_visiting_profile);
        recyclerViewPagePost=findViewById(R.id.recycler_view_visiting_page);
        aboutPage=findViewById(R.id.about_page_visiting_page);
    }
}