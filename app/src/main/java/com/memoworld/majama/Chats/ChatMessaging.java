package com.memoworld.majama.Chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.memoworld.majama.AllModals.MessageModel;
import com.memoworld.majama.R;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessaging extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseRecyclerAdapter<MessageModel, ChatViewHolder> adapter;
    String userId, profileId;
    RecyclerView recyclerView;
    Timestamp timestamp;
    EditText message;
    private DatabaseReference reference, reference2;
    private CircleImageView userImage;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messaging);

        userImage = findViewById(R.id.profile_image);
        textView = findViewById(R.id.userNameChat);

        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profileId = getIntent().getExtras().getString("profileId");

        LoadUserData();


        assert FirebaseAuth.getInstance().getCurrentUser() != null;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference = database.getReference().child("UserExtraDetails").child(profileId).child("UserToUserConnection").child("Chats").child(userId);
        reference2 = database.getReference().child("UserExtraDetails").child(userId).child("UserToUserConnection").child("Chats").child(profileId);

        FirebaseRecyclerOptions<MessageModel> options = new FirebaseRecyclerOptions.Builder<MessageModel>().setQuery(reference2, MessageModel.class).build();
        adapter = new FirebaseRecyclerAdapter<MessageModel, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ChatViewHolder holder, int position, @NonNull @NotNull MessageModel model) {
                if (model.getSender().equals("me")) {
                    if (model.getType().equals("text")) {
                        holder.rightText.setText(model.getMessage());
                        holder.leftText.setVisibility(View.GONE);
                        holder.leftImage.setVisibility(View.GONE);
                        holder.rightImage.setVisibility(View.GONE);
                    } else {

                        Glide.with(ChatMessaging.this).load(model.getMessage()).into(holder.rightImage);
                        holder.leftText.setVisibility(View.GONE);
                        holder.leftImage.setVisibility(View.GONE);
                        holder.rightText.setVisibility(View.GONE);

                    }
                } else {

                    if (model.getType().equals("text")) {
                        holder.leftText.setText(model.getMessage());
                        holder.rightText.setVisibility(View.GONE);
                        holder.leftImage.setVisibility(View.GONE);
                        holder.rightImage.setVisibility(View.GONE);
                    } else {

                        Glide.with(ChatMessaging.this).load(model.getMessage()).into(holder.leftImage);
                        holder.leftText.setVisibility(View.GONE);
                        holder.rightImage.setVisibility(View.GONE);
                        holder.rightText.setVisibility(View.GONE);

                    }

                }
            }

            @NonNull
            @NotNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_image_messaging, parent, false);
                return new ChatViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

    }

    private void LoadUserData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                database.getReference().child("Users").child(profileId).child("personalInfo").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.child("username").getValue().toString();
                        String profileImage = dataSnapshot.child("profileImageUrl").getValue().toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(userName);
                                Glide.with(ChatMessaging.this).load(profileImage).placeholder(R.drawable.user).into(userImage);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public void sendMessage(View view) {

        message = findViewById(R.id.etMessage);
        String Messages = message.getText().toString();
        message.setText("");

        if (Messages.equals("")) {
            Toast.makeText(this, "Please Enter a Message", Toast.LENGTH_SHORT).show();
            return;
        }

        final MessageModel messageModel = new MessageModel("text", Messages, "you", new Date().getTime());
        final MessageModel messageModel2 = new MessageModel("text", Messages, "me", new Date().getTime());

//        reference.push().setValue(messageModel);
//        reference2.push().setValue(messageModel2);
        timestamp = Timestamp.now();

        reference.child(String.valueOf(timestamp.getSeconds())).setValue(messageModel);
        reference2.child(String.valueOf(timestamp.getSeconds())).setValue(messageModel2);

    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView leftText, rightText;
        private ImageView leftImage, rightImage;

        public ChatViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            leftImage = itemView.findViewById(R.id.receiverImage);
            rightImage = itemView.findViewById(R.id.senderImage);
            leftText = itemView.findViewById(R.id.receiverText);
            rightText = itemView.findViewById(R.id.senderText);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}