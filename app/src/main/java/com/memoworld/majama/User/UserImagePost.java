package com.memoworld.majama.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.memoworld.majama.AllModals.PostImage;
import com.memoworld.majama.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UserImagePost extends AppCompatActivity {

    private static final String TAG = "UserImagePost";
    private ImageView imageUpload;
    private TextInputLayout textInputLayout;
    Uri imageUri;
    String userId, caption;
    private ProgressDialog progressDialog;
    private final StorageReference storage = FirebaseStorage.getInstance().getReference().child("UserImagePost");
    private final FirebaseFirestore ff = FirebaseFirestore.getInstance();
    private String downloadUrl;
    private PostImage postImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post2);
        imageUpload = findViewById(R.id.image_upload_image_post);
        textInputLayout = findViewById(R.id.caption_input_image_post);
        MaterialButton uploadButton = findViewById(R.id.upload_image_post);
        progressDialog = new ProgressDialog(this);
        assert FirebaseAuth.getInstance().getCurrentUser() != null;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(v);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });
    }

    private void UploadImage() {
        if (imageUri == null) {
            Toast.makeText(this, "Please Select Some Meme Post", Toast.LENGTH_SHORT).show();
            return;
        }
        if (textInputLayout.getEditText() != null)
            caption = textInputLayout.getEditText().getText().toString();
        progressDialog.setMessage("Uploading....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String time = System.currentTimeMillis() + "";
        storage.child(userId).child(time).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storage.child(userId).child(time).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrl = uri.toString();
                        postImage = new PostImage(downloadUrl, caption, Timestamp.now());

                        ff.collection("Users").document(userId).collection("Posts")
                                .document(time).set(postImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(UserImagePost.this, "Successfully uploaded...", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserImagePost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + e.getMessage());
            }
        });


    }

    public void SelectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setMaxCropResultSize(4000, 4000)
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && result != null) {
                imageUri = result.getUri();
                imageUpload.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}