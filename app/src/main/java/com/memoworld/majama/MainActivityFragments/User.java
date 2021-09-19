package com.memoworld.majama.MainActivityFragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.memoworld.majama.AllModals.Post;
import com.memoworld.majama.AllModals.PostImage;
import com.memoworld.majama.AllModals.UserDetailsFirestore;
import com.memoworld.majama.LoginInstructionSplash.NewLogin;
import com.memoworld.majama.R;
import com.memoworld.majama.User.Mypages;
import com.memoworld.majama.User.UserImagePost;
import com.memoworld.majama.User.pagesFollowed;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class User extends Fragment {

    private static final String TAG = "Majama";
    FirebaseFirestore ff = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId;
    Thread thread;
    ArrayList<Post> arrayList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    Toolbar toolbar;

    FirestoreRecyclerAdapter<PostImage, MyViewHolder> adapter;

    public User() {
        // Required empty public constructor
    }

    UserDetailsFirestore userDetailsFirestore;
    CircleImageView userImage;
    TextView userName, userAbout, followers, followings, balance;
    RecyclerView recyclerView;
    Button uploadButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initialize(view);
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar.inflateMenu(R.menu.user_fragment_menu);
        toolbar.setTitle("User");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        userId = user.getUid();
        thread = new Thread(getData);
        thread.start();
//        Query query = ff.collection("Users").document(userId).collection("Posts").orderBy("uploadTime", Query.Direction.DESCENDING);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UserImagePost.class));
            }
        });
        LoadImages();
        return view;

    }

    public void LoadImages() {
        Query query = ff.collection("Users").document(userId).collection("Posts");

        FirestoreRecyclerOptions<PostImage> options = new FirestoreRecyclerOptions.Builder<PostImage>().setQuery(query, PostImage.class).build();

        adapter = new FirestoreRecyclerAdapter<PostImage, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull PostImage model) {
                Glide.with(getContext()).load(model.getImageUrl()).placeholder(R.drawable.coder).into(holder.imageView);
//                Log.d(TAG, "onBindViewHolder: " + position);
                holder.imageView.setImageResource(R.drawable.coder);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_post_view, parent, false);
                return new MyViewHolder(view1);
            }
        };
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.startListening();
        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    Runnable getData = new Runnable() {
        @Override
        public void run() {
            ff.collection("Users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null && value.exists()) {
                        userDetailsFirestore = value.toObject(UserDetailsFirestore.class);
                        Glide.with(Objects.requireNonNull(getContext())).load(userDetailsFirestore.getProfileImageUrl()).placeholder(R.drawable.account_circle).into(userImage);
                        String userFinalName = userDetailsFirestore.getFirstName() + " " + userDetailsFirestore.getLastName();
                        userName.setText(userFinalName);
                        if (userDetailsFirestore.getAbout() != null)
                            userAbout.setText(userDetailsFirestore.getAbout());
                        else
                            userAbout.setVisibility(View.INVISIBLE);
                        if (userDetailsFirestore.getFollowing() != null)
                            followings.setText(String.valueOf(userDetailsFirestore.getFollowing()));
                        if (userDetailsFirestore.getFollowers() != null)
                            followers.setText(String.valueOf(userDetailsFirestore.getFollowers()));
                        if (userDetailsFirestore.getBalance() != null)
                            balance.setText(String.valueOf(userDetailsFirestore.getBalance()));
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
        toolbar = view.findViewById(R.id.main_appBar_user);
        uploadButton = view.findViewById(R.id.upload_btn_my_profile);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_menu_users) {
            displayDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayDialog() {
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_user_settting, getView().findViewById(R.id.bottom_sheet));

        // MY PAGES
        bottomSheetDialog.setContentView(view);
        view.findViewById(R.id.myPages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(getContext(), Mypages.class));
            }
        });

        // LOGOUT button
        view.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), NewLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                if (getActivity() != null)
                    getActivity().finish();
                bottomSheetDialog.dismiss();
            }
        });

        // PAGES FOLLOWED
        view.findViewById(R.id.pages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(getContext(), pagesFollowed.class));
            }
        });

        bottomSheetDialog.show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_post_image_single_view);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}