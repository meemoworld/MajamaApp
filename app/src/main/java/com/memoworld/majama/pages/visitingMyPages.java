package com.memoworld.majama.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.memoworld.majama.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class visitingMyPages extends AppCompatActivity {

    private CircleImageView pageProfileImage;
    private TextView pageName,balance,aboutPage,followers;
    private RecyclerView recyclerViewPagePost;
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_my_pages);
        initialize();
    }

    private void initialize() {
        pageProfileImage=findViewById(R.id.image_view_my_page_visiting);
        pageName=findViewById(R.id.name_txt_box_my_page);
        balance=findViewById(R.id.balance_my_page);
        aboutPage=findViewById(R.id.about_visiting_my_page);
        followers=findViewById(R.id.count_follower_visiting_my_page);
        recyclerViewPagePost=findViewById(R.id.recycler_view_visiting_my_page);
        upload=findViewById(R.id.upload_btn_my_page);
    }
}