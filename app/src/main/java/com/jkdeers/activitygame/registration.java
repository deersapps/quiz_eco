package com.jkdeers.activitygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Button studentButton = findViewById(R.id.btnLogin);
        Button schoolButton = findViewById(R.id.btnSignUp);

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStudent = new Intent(getApplicationContext(),Signin.class);
                startActivity(intentStudent);
                finish();
            }
        });

        schoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSchool = new Intent(getApplicationContext(), RegisterSchool.class);
                startActivity(intentSchool);
                finish();
            }
        });
    }
}