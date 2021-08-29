package com.memoworld.majama.User;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.memoworld.majama.LoginInstructionSplash.NewLogin;
import com.memoworld.majama.LoginInstructionSplash.login;
import com.memoworld.majama.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account_Details extends AppCompatActivity {


    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("UserImages").child("ProfileImages");
    private static final String TAG = "Account_Details";
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    TextInputLayout inputFirstName, inputLastName, inputAge, inputGender, inputAbout, inputCity;
    String userFirstName, userLastName, userAge, userCity, userGender, userAbout, userImageUrl, userId;
    CircleImageView userImage;


    ProgressDialog progressDialog;
    Dialog dialog;
    ArrayList<String> listAll = new ArrayList<String>();
    MaterialButton button;
    Uri imageUri;

    //    Gender Dropdown function
    @Override
    protected void onPostResume() {
        super.onPostResume();

        String[] gender = new String[]{"Male", "Female", "Others"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.gender_dropdown, gender);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteText);
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        obj_list();
        progressDialog = new ProgressDialog(this);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();


        initialize();

        inputCity.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Account_Details.this);
                dialog.setContentView(R.layout.dialog_searchable_city);
                dialog.getWindow().setLayout(850, 1000);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText editText = dialog.findViewById(R.id.editText_city_search);
                ListView listView = dialog.findViewById(R.id.list_view_search_city);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Account_Details.this, android.R.layout.simple_list_item_1, listAll);
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        inputCity.getEditText().setText(adapter.getItem(position));
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    // Get the content of cities.json from assets directory and store it as string
    public String getJson() {
        String json = null;
        try {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    void obj_list() {
        // Exceptions are returned by JSONObject when the object cannot be created
        try {
            // Convert the string returned to a JSON object
            JSONObject jsonObject = new JSONObject(getJson());
            // Get Json array
            JSONArray array = jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for (int i = 0; i < array.length(); i++) {
                // select the particular JSON data
                JSONObject object = array.getJSONObject(i);
                String city = object.getString("name");
                String state = object.getString("state");
                // add to the lists in the specified format
                listAll.add(city + " , " + state);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        inputFirstName = findViewById(R.id.input_first_name_details);
        inputLastName = findViewById(R.id.input_last_name_details);
        inputAbout = findViewById(R.id.input_about_details);
//        inputAge = findViewById(R.id.input_age_details);
        inputCity = findViewById(R.id.input_city_details);
        inputGender = findViewById(R.id.input_gender_details);
        button = findViewById(R.id.btn_continue_details);
        userImage = findViewById(R.id.profile_image_details);
    }

    public void SelectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .setMaxCropResultSize(4000, 4000)
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                userImage.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void Continue(View view) {

        userFirstName = inputFirstName.getEditText().getText().toString().toLowerCase();
        userLastName = inputLastName.getEditText().getText().toString().toLowerCase();
//        userAge = inputAge.getEditText().getText().toString();
        userCity = inputCity.getEditText().getText().toString();
        userGender = inputGender.getEditText().getText().toString();
        userAbout = inputAbout.getEditText().getText().toString();

        if (userFirstName.isEmpty()) {
            login.ShowError(inputFirstName, "Please Fill this field");
            return;
        }
        if (userLastName.isEmpty()) {
            login.ShowError(inputLastName, "Please Fill this field");
            return;
        }
        if (userGender.isEmpty()) {
            login.ShowError(inputGender, "Please Fill this field");
            return;
        }
        if (userCity.isEmpty()) {
            login.ShowError(inputCity, "Please Fill this field");
            return;
        }
        if (imageUri == null) {
            Toast.makeText(this, "Image is necessary", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (userAge.isEmpty())
//            userAge = null;

        if (userAbout.isEmpty())
            userAbout = null;

//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setTitle("Updating");
//        progressDialog.setMessage("Please Wait while we set up your account");
//        progressDialog.show();

        Map<String, Object> personalInfo = new HashMap<>();
        personalInfo.put("About", userAbout);
        personalInfo.put("firstName", userFirstName);
        personalInfo.put("lastName", userLastName);
        personalInfo.put("profileImageUrl", null);
        personalInfo.put("userCity", userCity);
        personalInfo.put("username", null);
        personalInfo.put("birthDate", null);

        Thread detailUploadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("Users").document(userId).set(personalInfo);
                Log.d(TAG, "run: Details Uploaded");
            }
        });
        detailUploadThread.setPriority(1);
        detailUploadThread.start();
        Thread imageUploadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: Image Upload started");
                storageReference.child(userId)
                        .putFile(imageUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.child(userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            userImageUrl = uri.toString();
                                            Log.d(TAG, "run: Image Upload ended");
                                            firebaseFirestore.collection("Users").document(userId).update("profileImageUrl", userImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "onSuccess: Image Url Updated");
                                                }
                                            });

                                        }
                                    });
                                }
                            }
                        });
            }
        });
        imageUploadThread.setPriority(2);
        imageUploadThread.start();
        startActivity(new Intent(Account_Details.this, UserNameInput.class));
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            startActivity(new Intent(Account_Details.this, NewLogin.class));
            finish();
        }
    }
}