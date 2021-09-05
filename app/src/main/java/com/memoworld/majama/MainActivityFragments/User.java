package com.memoworld.majama.MainActivityFragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
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

    Toolbar toolbar;

    FirestoreRecyclerAdapter<Post, PostViewHolder> adapter;

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
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar.inflateMenu(R.menu.user_fragment_menu);
        toolbar.setTitle("User");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        thread = new Thread(getData);
        thread.start();

//        Query query = ff.collection("Users").document(userId).collection("Posts").orderBy("uploadTime", Query.Direction.DESCENDING);

//        recyclerView.setLayoutManager(new GridAutoFitLayoutManager(getContext(), view.getWidth()));
        return view;


//            ff.collection("Users").document(userId).collection("Posts").orderBy("uploadTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
//                @Override
//                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                    if (error != null) {
//                        Log.d(TAG, "onEvent: Failed");
//                        return;
//                    }
//                    for (QueryDocumentSnapshot documentSnapshot : value) {
//                        arrayList.add(documentSnapshot.toObject(Post.class));
//                    }
//                }
//            });

    }

    Runnable getData = new Runnable() {
        @Override
        public void run() {
            ff.collection("Users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.exists()) {
                        userDetailsFirestore = value.toObject(UserDetailsFirestore.class);
                        Glide.with(getContext()).load(userDetailsFirestore.getProfileImageUrl()).into(userImage);
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
                    }
                }
            });
            Query query = ff.collection("Users").document(userId).collection("Posts");

            FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();

            adapter = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
//                Glide.with(view).load(model.getImageUrl()).into(holder.imageView);
                    Log.d(TAG, "onBindViewHolder: " + position);
                    holder.imageView.setImageResource(R.drawable.coder);
                }

                @NonNull
                @Override
                public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_post_view, parent, false);
                    return new PostViewHolder(view1);
                }
            };
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        toolbar = view.findViewById(R.id.main_appBar_user);
    }

    private static class PostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final ConstraintLayout layout1;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_post_image_single_view);
            layout1 = itemView.findViewById(R.id.layout_single_post_user);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.settings_menu_users)
        {
            Toast.makeText(getContext(), "Clicked...", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}