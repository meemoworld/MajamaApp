package com.memoworld.majama.MainActivityFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.memoworld.majama.AllModals.Post;
import com.memoworld.majama.AllModals.UserDetailsFirestore;
import com.memoworld.majama.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class User extends Fragment {

    private static final String TAG = "Majama";
    FirebaseFirestore ff = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    Thread thread;
    ArrayList<Post> arrayList = new ArrayList<>();

    public User() {
        // Required empty public constructor
    }

    UserDetailsFirestore userDetailsFirestore;
    CircleImageView userImage;
    TextView userName, userAbout, followers, followings, balance;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initialize(view);
        thread = new Thread(getData);
        thread.start();
        if (userDetailsFirestore == null) {
            thread.start();
        } else {
            Glide.with(view).load(userDetailsFirestore.getProfileImageUrl()).into(userImage);
            String userFinalName = userDetailsFirestore.getFirstName() + " " + userDetailsFirestore.getLastName();
            userName.setText(userFinalName);
            if (userAbout != null)
                userAbout.setText(userDetailsFirestore.getAbout());
            else
                userAbout.setVisibility(View.INVISIBLE);
            if (userDetailsFirestore.getFollowing() != null)
                followings.setText(userDetailsFirestore.getFollowing());
            if (userDetailsFirestore.getFollowers() != null)
                followers.setText(userDetailsFirestore.getFollowers());
            if (userDetailsFirestore.getBalance() != null)
                balance.setText(userDetailsFirestore.getBalance());

            ff.collection("Users").document(userId).collection("Posts").orderBy("uploadTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.d(TAG, "onEvent: Failed");
                        return;
                    }
                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        arrayList.add(documentSnapshot.toObject(Post.class));
                    }
                }
            });
            recyclerView.setLayoutManager(new GridLayoutManager());
        }
        return view;
    }

    Runnable getData = new Runnable() {
        @Override
        public void run() {
            ff.collection("Users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.exists()) {
                        userDetailsFirestore = value.toObject(UserDetailsFirestore.class);
                    }
                }
            });
        }
    };

    private void initialize(View view) {

        userImage = view.findViewById(R.id.user_image_user_fragment);
        userName = view.findViewById(R.id.name_txt_box_user_fragment);
        userAbout = view.findViewById(R.id.about_user_fragment);
        followers = view.findViewById(R.id.count_follower_user_fragment);
        followings = view.findViewById(R.id.countFollowing_user_fragment);
        balance = view.findViewById(R.id.balance_user_fragment);
        recyclerView = view.findViewById(R.id.recycler_view_user_fragment);
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        private final TextView interestTagText;
        private final TextView interestTagText1;
        private final TextView interestTagText2;
        private final ImageView imageView;
        private final ImageView imageView1;
        private final ImageView imageView2;
        private final ConstraintLayout layout1;
        private final ConstraintLayout layout2;
        private final ConstraintLayout layout3;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            interestTagText = itemView.findViewById(R.id.name_text_box_singleItem);
            interestTagText1 = itemView.findViewById(R.id.name_text_box_singleItem1);
            interestTagText2 = itemView.findViewById(R.id.name_text_box_singleItem2);
            imageView = itemView.findViewById(R.id.Image);
            imageView1 = itemView.findViewById(R.id.Image1);
            imageView2 = itemView.findViewById(R.id.Image2);

            layout1 = itemView.findViewById(R.id.constraint1);
            layout2 = itemView.findViewById(R.id.constraint2);
            layout3 = itemView.findViewById(R.id.constraint3);
        }
    }
    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.profile_activity_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.profile_activity_menu) {
//            startActivity(new Intent(ProfileActivity.this, userSetting.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}