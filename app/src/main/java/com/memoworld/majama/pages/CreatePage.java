package com.memoworld.majama.pages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.memoworld.majama.AllModals.PageInfoFirestore;
import com.memoworld.majama.AllModals.PageInfoRealTime;
import com.memoworld.majama.R;
import com.memoworld.majama.User.Mypages;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePage extends AppCompatActivity {

    private CircleImageView pageImage;
    private TextInputEditText pageName, pageAbout;
    private Uri imageUri;
    private String pageId, pageImageUrl, userId;
    private PageInfoFirestore pageInfoFirestore;
    private Map<String, Object> pageNameMap;
    private PageInfoRealTime pageInfoRealTime;

    private final FirebaseFirestore ff = FirebaseFirestore.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("PageImages").child("userPageImages");
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_page);
        initialize();
        userId = auth.getCurrentUser().getUid();
        ff.collection("AllNames").document("pages").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    pageNameMap = task.getResult().getData();
                    if (pageNameMap == null)
                        pageNameMap = new HashMap<>();
                }
            }
        });

        pageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(v);
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
                pageImage.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize() {
        Toolbar toolbar = findViewById(R.id.app_bar_create_page);
        pageAbout = findViewById(R.id.text_input_about_create_page_edit_text);
        pageImage = findViewById(R.id.circleImageView);
        pageName = findViewById(R.id.text_input_create_page_edit);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Create Page");
    }

    public void CreatePageFn(View view) {
        String pageNameString = pageName.getText().toString().trim();
        String pageAboutString = pageAbout.getText().toString().trim();

        if (pageAboutString.isEmpty()) {
            Toast.makeText(this, "Something about is must", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pageNameString.isEmpty()) {
            Toast.makeText(this, "Page Name is must and must be unique", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Map.Entry<String, Object> entry : pageNameMap.entrySet()) {
            if (entry.getValue().equals(pageNameString)) {
                Toast.makeText(this, "Sorry a page with this name Already exists..", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        pageId = autoIdGenerator();
        pageNameMap.put(pageId, pageNameString);


        new Thread(runnable).start();

        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(R.style.Widget_MaterialComponents_CircularProgressIndicator);
            progressDialog.setMessage("Uploading your information.....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            storageReference.child(pageId).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(CreatePage.this, "Success", Toast.LENGTH_LONG).show();
                    storageReference.child(pageId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            pageImageUrl = uri.toString();

                            pageInfoFirestore = new PageInfoFirestore(pageNameString, pageAboutString, pageImageUrl, Timestamp.now());
                            pageInfoRealTime = new PageInfoRealTime(pageNameString, pageAboutString, pageImageUrl, userId, 0);
                            database.getReference("Pages").child(pageId).setValue(pageInfoRealTime);

                            ff.collection("Users").document(userId).collection("Pages").document(pageId).set(pageInfoFirestore).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pageImage.setImageResource(R.drawable.coder);
                                    pageName.setText("");
                                    pageAbout.setText("");
                                    progressDialog.dismiss();
                                    startActivity(new Intent(CreatePage.this, Mypages.class));
                                    finish();
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreatePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setProgress((int) progress);
                }
            });
        } else {

            pageInfoFirestore = new PageInfoFirestore(pageNameString, pageAboutString, null, Timestamp.now());
            pageInfoRealTime = new PageInfoRealTime(pageNameString, pageAboutString, null, userId, 0);

            database.getReference("Pages").child(pageId).setValue(pageInfoRealTime);

            ff.collection("Users").document(userId).collection("Pages").document(pageId).set(pageInfoFirestore).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    pageImage.setImageResource(R.drawable.coder);
                    pageName.setText("");
                    pageAbout.setText("");
                    startActivity(new Intent(CreatePage.this, Mypages.class));
                    finish();
                }
            });
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ff.collection("AllNames").document("pages").set(pageNameMap);
        }
    };

    private static final int AUTO_ID_LENGTH = 20;
    private static final String AUTO_ID_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random rand = new SecureRandom();

    private String autoIdGenerator() {
        StringBuilder builder = new StringBuilder();
        int maxRandom = AUTO_ID_ALPHABET.length();
        for (int i = 0; i < AUTO_ID_LENGTH; i++) {
            builder.append(AUTO_ID_ALPHABET.charAt(rand.nextInt(maxRandom)));
        }
        return builder.toString();
    }
}