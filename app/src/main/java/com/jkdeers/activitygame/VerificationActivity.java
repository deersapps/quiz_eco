package com.jkdeers.activitygame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class VerificationActivity extends AppCompatActivity {

    EditText code1;
    EditText code2;
    EditText code3;
    EditText code4;

    Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
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
        code1 = findViewById(R.id.etCode1);
        code2 = findViewById(R.id.etCode2);
        code3 = findViewById(R.id.etCode3);
        code4 = findViewById(R.id.etCode4);

        btnSubmit = findViewById(R.id.btnSubmit);

        code1.addTextChangedListener(new GenericTextWatcher(code2, code1));

        code2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    code2.addTextChangedListener(new GenericTextWatcher(code2, code1));
                } else {
                    code2.addTextChangedListener(new GenericTextWatcher(code3, code2));
                }
                return false;
            }
        });
        code3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    code3.addTextChangedListener(new GenericTextWatcher(code3, code2));
                } else {
                    code3.addTextChangedListener(new GenericTextWatcher(code4, code3));
                }
                return false;
            }
        });
        code4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    code4.addTextChangedListener(new GenericTextWatcher(code4, code3));
                }
                return false;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!code1.getText().toString().matches("") && !code2.getText().toString().matches("") &&
                        !code3.getText().toString().matches("") && !code4.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), code1.getText().toString() +
                            code2.getText().toString() +
                            code3.getText().toString() +
                            code4.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
