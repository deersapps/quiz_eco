package com.jkdeers.activitygame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignUp;
    Button btnLogIn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogin);

        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:{
                startActivity(new Intent(getApplicationContext(),Signin.class));
                finish();
                break;
            }
            case R.id.btnSignUp:{
                startActivity(new Intent(getApplicationContext(), Signin.class));
                finish();
                break;
            }
        }
    }
}
