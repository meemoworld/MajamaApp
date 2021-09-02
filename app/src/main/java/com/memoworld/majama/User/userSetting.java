package com.memoworld.majama.User;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.memoworld.majama.R;

public class userSetting extends AppCompatActivity {


    private TextView myPages,account,privacyPolicy,help,withDraw,pages;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settting);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" Settings");
            getSupportActionBar().setLogo(R.drawable.ic_baseline_settings_24);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }
        initialize();
    }

    private void initialize() {
        myPages=findViewById(R.id.myPages);
        account=findViewById(R.id.account);
        privacyPolicy=findViewById(R.id.privacyPolicy);
        help=findViewById(R.id.help);
        withDraw=findViewById(R.id.withdraw);
        pages=findViewById(R.id.pages);
    }

    public void MyPages(View view) {

    }

    public void Account(View view) {
    }

    public void PrivacyPolicy(View view) {
    }

    public void Help(View view) {
    }

    public void Withdraw(View view) {
    }

    public void Pages(View view) {
    }

    public void displayDialog(){
        bottomSheetDialog = new BottomSheetDialog(userSetting.this , R.style.BottomSheetTheme);

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_user_settting , findViewById(R.id.bottom_sheet));

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
}