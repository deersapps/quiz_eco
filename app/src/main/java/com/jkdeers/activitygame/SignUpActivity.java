package com.jkdeers.activitygame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignUp;
    Button btnLogIn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // get reference to the string array that we just created

        String[] countries = getResources().getStringArray(R.array.countries);
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, R.id.textView, countries);
        // get reference to the autocomplete text view
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        autoCompleteTextView.setAdapter(arrayAdapter);

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
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
