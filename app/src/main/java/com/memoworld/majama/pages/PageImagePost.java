package com.memoworld.majama.pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.memoworld.majama.AllModals.PagePostTags;
import com.memoworld.majama.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PageImagePost extends AppCompatActivity {


    String pageId, userId;
    ImageView imageView;
    Uri imageUri;
    private FirebaseAuth auth;
    private ChipGroup chipGroup;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();
    PagePostTags pagePostTags;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private Button uploadButton;
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("PostImages");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);
        imageView = findViewById(R.id.upload_image_view);
        autoCompleteTextView = findViewById(R.id.edit_text_search_tag_post_image);
        uploadButton = findViewById(R.id.upload_image_btn);

        pageId = getIntent().getExtras().getString("pageId");

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPagePost();
            }
        });
//        autoCompleteTextView.setAdapter(adapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(v);
            }
        });


        chipGroup = findViewById(R.id.interest_chip_group);

        assert auth.getCurrentUser() != null;
        userId = auth.getCurrentUser().getUid();

        new Thread(runnable).start();


    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ff.collection("Tag").document("postTags").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null) {
                        pagePostTags = value.toObject(PagePostTags.class);
                        if (pagePostTags != null) {
                            adapter = new ArrayAdapter<String>(PageImagePost.this, R.layout.support_simple_spinner_dropdown_item, pagePostTags.getTags());
                            autoCompleteTextView.setAdapter(adapter);
                        }
                    }

                }
            });
        }
    };

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
                imageView.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadPagePost() {
        if (imageUri == null) {
            Toast.makeText(this, "Please Select Some Meme Post", Toast.LENGTH_SHORT).show();
            return;
        }

    }


}