package com.jkdeers.activitygame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Objects;

public class
ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnForgetPassword;
    Button btnLoginNow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
    }

    private void init() {
        btnForgetPassword =findViewById(R.id.btnForgetPassword);
        btnForgetPassword.setOnClickListener(this);
        btnLoginNow =findViewById(R.id.btnLoginForgetScreen);
        btnLoginNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnForgetPassword:{
                startActivity(new Intent(getApplicationContext(),VerificationActivity.class));
                finish();
                break;
            }
            case R.id.btnLoginForgetScreen:{
                startActivity(new Intent(getApplicationContext(),Signin.class));
                finish();
                break;
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
