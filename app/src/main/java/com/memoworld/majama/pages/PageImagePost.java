package com.memoworld.majama.pages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.memoworld.majama.AllModals.PageImagePostFirestore;
import com.memoworld.majama.AllModals.PagePostImageModal;
import com.memoworld.majama.AllModals.PagePostTags;
import com.memoworld.majama.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

public class PageImagePost extends AppCompatActivity {

    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("PostImages");
    private final FirebaseFirestore ff = FirebaseFirestore.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ChipGroup chipGroup;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private List<String> tagsList;
    PagePostTags pagePostTags;
    String pageId, userId;
    StringBuilder tags;
    ImageView imageView;
    Uri imageUri;
    Long followers;
    private ProgressDialog progressDialog;
    private static final String TAG = "PageImagePost";
    Long priority = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_image_post);
        imageView = findViewById(R.id.upload_image_view);
        autoCompleteTextView = findViewById(R.id.edit_text_search_tag_post_image);
        Button uploadButton = findViewById(R.id.upload_image_btn);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        pageId = getIntent().getExtras().getString("pageId");
        assert auth.getCurrentUser() != null;
        userId = auth.getCurrentUser().getUid();
        tags = new StringBuilder();

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

        tagsList = new ArrayList<>();
        chipGroup = findViewById(R.id.interest_chip_group);

        new Thread(runnable).start();

        database.getReference("Pages").child(pageId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                priority = (Long) dataSnapshot.child("priority").getValue();
            }
        });


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
                    .setMaxCropResultSize(6000, 4000)
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
        if (tagsList.size() < 3) {
            Toast.makeText(this, "Select Atleast 3 tags", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        database.getReference("Pages").child(pageId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                followers = (Long) dataSnapshot.child("followers").getValue();
            }
        });

        String time = String.valueOf(System.currentTimeMillis());

        tags.append(tagsList.get(0));
        for (int i = 1; i < tagsList.size(); i++) {
            tags.append(",");
            tags.append(tagsList.get(i));
        }


        storageReference.child(pageId).child(time).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child(pageId).child(time).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        PagePostImageModal pagePostImageModal = new PagePostImageModal(pageId, uri.toString(), tags.toString(), followers.toString(), Timestamp.now(), priority);
                        PageImagePostFirestore pageImagePostFirestore = new PageImagePostFirestore(uri.toString(), Timestamp.now());

                        ff.collection("Users").document(userId).collection("Pages").document(pageId)
                                .collection("Posts").document(time).set(pageImagePostFirestore).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: Firestore updated...");
                            }
                        });

                        database.getReference("Posts").child(time).setValue(pagePostImageModal).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(PageImagePost.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PageImagePost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void AddToChipGroup(String tag) {
        Chip chip = new Chip(this);
        chip.setText(tag);
        chip.setCloseIconVisible(true);
        chip.setCheckable(true);
//        chip.setBackgroundColor(getColor(R.color.lightBlue));
        chip.setClickable(false);
        chipGroup.addView(chip);
        chip.setOnCloseIconClickListener(this::OnCancelClick);
        chipGroup.setVisibility(View.VISIBLE);
    }

    public void OnCancelClick(View view) {
        Chip chip = (com.google.android.material.chip.Chip) view;
        chipGroup.removeView(chip);
        tagsList.remove(chip.getText().toString());
    }


    public void AddTag(View view) {

        String tag = autoCompleteTextView.getText().toString();
        if (!pagePostTags.getTags().contains(tag)) {
            Toast.makeText(this, "Sorry this tag is not present in the list...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tagsList.contains(tag)) {
            Toast.makeText(this, "Already Selected...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tagsList.size() > 20) {
            Toast.makeText(this, "You have already selected maximum allowed tags", Toast.LENGTH_SHORT).show();
            return;
        }
        autoCompleteTextView.setText("");
        tagsList.add(tag);
        AddToChipGroup(tag);
    }
}