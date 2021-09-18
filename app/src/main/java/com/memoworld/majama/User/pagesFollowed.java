package com.memoworld.majama.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.memoworld.majama.R;
import com.memoworld.majama.pages.VisitingOtherPage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class pagesFollowed extends AppCompatActivity {

    RecyclerView recyclerView;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String userID;
    List<Page> pageList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages_followed);

        assert FirebaseAuth.getInstance().getCurrentUser() != null;
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.recyclerView_pages_followed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayAdapter = new ArrayAdapter(this, pageList);
        recyclerView.setAdapter(arrayAdapter);

        firebaseDatabase.getReference("Users").child(userID).child("PageFollowing").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String pageId = snapshot.getKey();
                if (pageId != null)
                    firebaseDatabase.getReference("Pages").child(pageId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            String pageName = String.valueOf(dataSnapshot.child("name").getValue());
                            String imageUrl = String.valueOf(dataSnapshot.child("imageUrl").getValue());
                            pageList.add(new Page(pageName, imageUrl, pageId));
                            arrayAdapter.notifyItemInserted(pageList.size());
                        }
                    });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.MyViewHolder> {

        Context context;
        List<Page> pageList = new ArrayList<>();

        public ArrayAdapter(Context context, List<Page> pageList) {
            this.context = context;
            this.pageList = pageList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.pages_single_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Glide.with(context).load(pageList.get(position).getImageUrl()).placeholder(R.drawable.user).
                    into(holder.circleImageView);
            holder.textView.setText(pageList.get(position).getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(pagesFollowed.this, VisitingOtherPage.class);
                    intent.putExtra("pageId", pageList.get(position).getPageId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pageList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            final CircleImageView circleImageView;
            final TextView textView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                circleImageView = itemView.findViewById(R.id.page_image_single_view);
                textView = itemView.findViewById(R.id.page_name_single_view);
            }
        }
    }

    public static class Page {

        private String name, imageUrl, pageId;

        public Page(String name, String imageUrl, String pageId) {
            this.name = name;
            this.imageUrl = imageUrl;
            this.pageId = pageId;
        }

        public String getPageId() {
            return pageId;
        }

        public void setPageId(String pageId) {
            this.pageId = pageId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}