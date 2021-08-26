package com.memoworld.majama.User;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.memoworld.majama.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Account_Details extends AppCompatActivity {

    Dialog dialog;
    TextInputLayout inputFirstName, inputLastName, inputAge, inputGender, inputAbout, inputCity;
    ArrayList<String> listAll = new ArrayList<String>();


//    Gender Dropdown function
    @Override
    protected void onPostResume() {
        super.onPostResume();

        String[] gender = new String[]{"Male", "Female" , "Others"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.gender_dropdown , gender);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteText);
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        obj_list();

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
        inputAge = findViewById(R.id.input_age_details);
        inputCity = findViewById(R.id.input_city_details);
        inputGender = findViewById(R.id.input_gender_details);
    }
}