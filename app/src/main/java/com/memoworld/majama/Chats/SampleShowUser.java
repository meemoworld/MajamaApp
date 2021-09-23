package com.memoworld.majama.Chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.memoworld.majama.R;
import com.memoworld.majama.User.pagesFollowed;
import com.memoworld.majama.pages.VisitingOtherPage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SampleShowUser extends AppCompatActivity {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String userID , profileId;

    List<pagesFollowed.Page> list = new ArrayList<>();
    private ArrayAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_show_user);

        recyclerView = findViewById(R.id.sampleUserRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ArrayAdapter(this, list);
        recyclerView.setAdapter(adapter);


        assert FirebaseAuth.getInstance().getCurrentUser() != null;
        profileId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDatabase.getReference().child("UserExtraDetails").child(profileId).child("UserToUserConnection").child("Friends").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                String userID = snapshot.getKey();
                if (userID != null){
                    firebaseDatabase.getReference().child("Users").child(userID).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            String userName = String.valueOf(dataSnapshot.child("username").getValue());
                            String userImage = String.valueOf(dataSnapshot.child("personalInfo").child("profileImageUrl").getValue());
                            list.add(new pagesFollowed.Page(userName, userImage, userID));
                            adapter.notifyItemInserted(list.size());
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.MyViewHolder> {

        Context context;
        List<pagesFollowed.Page> pageList = new ArrayList<>();

        public ArrayAdapter(Context context, List<pagesFollowed.Page> pageList) {
            this.context = context;
            this.pageList = pageList;
        }


        @NonNull
        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.pages_single_item, parent, false);
            return new ArrayAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
            Glide.with(context).load(pageList.get(position).getImageUrl()).placeholder(R.drawable.user).
                    into(holder.circleImageView);
            holder.textView.setText(pageList.get(position).getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SampleShowUser.this, ChatMessaging.class);
                    intent.putExtra("profileId", pageList.get(position).getPageId());
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
}