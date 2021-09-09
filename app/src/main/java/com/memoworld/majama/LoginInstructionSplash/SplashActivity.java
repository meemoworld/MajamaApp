package com.memoworld.majama.LoginInstructionSplash;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.memoworld.majama.MainActivity;
import com.memoworld.majama.R;
import com.memoworld.majama.User.Account_Details;
import com.memoworld.majama.User.Interest.Interest;
import com.memoworld.majama.User.UserNameInput;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Sidd";
    Animation topAnim, bottomAnim;
    ImageView logo;
    TextView name;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser mUser;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name_txt_box_visiting);

        logo.setAnimation(topAnim);
        name.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mUser == null) {
                    Intent intent = new Intent(SplashActivity.this, NewLogin.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(logo, "logo_image");
                    pairs[1] = new Pair<View, String>(name, "app_name");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                } else {
                    Log.d(TAG, "onSuccess: User ID not Null");
                    firebaseFirestore.collection("AllNames").document("users").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: Document Snapshot");
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "onSuccess: username got");
                                String username = (String) documentSnapshot.get(mUser.getUid());
                                if (username == null) {
                                    Log.d(TAG, "onSuccess: username was Null");
                                    startActivity(new Intent(SplashActivity.this, UserNameInput.class));
                                    finish();
                                } else {
                                    Log.d(TAG, "onSuccess: username was Not Null");
                                    firebaseFirestore.collection("Users").document(mUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (value.exists()) {
                                                Log.d(TAG, "onSuccess: Snapshot was Not Null");
                                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                            } else if (value.get("interest") == null) {
                                                startActivity(new Intent(SplashActivity.this, Interest.class));
                                            } else {
                                                Log.d(TAG, "onSuccess: Snapshot was Null");
                                                startActivity(new Intent(SplashActivity.this, Account_Details.class));
                                            }
                                            finish();
                                        }
                                    });
                                }
                            } else {
                                Log.d(TAG, "Nothing Exists: ");
                                startActivity(new Intent(SplashActivity.this, UserNameInput.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure:  Username nhi mila");
                            startActivity(new Intent(SplashActivity.this, UserNameInput.class));
                            finish();
                        }
                    });
                }
            }
        }, 1000);

    }
}