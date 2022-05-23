package com.jkdeers.activitygame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.provider.Settings.Secure;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.jkdeers.activitygame.databinding.ActivityMainBinding;
import com.jkdeers.activitygame.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Signin extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view;
    TextView tvForgetPassword;
    Button btnLogin;
    Button btnSignUp;
    EditText btnUn,btnPwd;
    JSONObject credentialsObject = new JSONObject();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        requestPermission();
        init();
    }

    private void init() {
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnUn = findViewById(R.id.un_edit_text);
        btnPwd = findViewById(R.id.pass_edit_text);

        tvForgetPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvForgetPassword:{
                startActivity(new Intent(getApplicationContext(),ForgetPasswordActivity.class));
               // finish();
                break;
            }
            case R.id.btnLogin:{
             //   loginProcess();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            }
            case R.id.btnSignUp:{
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
                break;
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted && readAccepted && writeAccepted)
                        //Snackbar.make( "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access.", Toast.LENGTH_SHORT).show();
                    else {
                        //Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access.", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if ((shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(CAMERA)
                                    ||shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)||shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))
                            || ((grantResults[0] != PackageManager.PERMISSION_GRANTED) || (grantResults[1] != PackageManager.PERMISSION_GRANTED) || (grantResults[2] != PackageManager.PERMISSION_GRANTED) || (grantResults[3] != PackageManager.PERMISSION_GRANTED)))
                            {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                } else {
                                                   // requestPermission();
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                        //Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();



                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Signin.this);
        alert.setMessage(message)
                .setPositiveButton("OK", okListener);
        AlertDialog alert1 = alert.create();

                alert1.show();
        TextView textView = alert1.getWindow().findViewById(android.R.id.message);
        //TextView alertTitle = (TextView) alert1.getWindow().findViewById(R.id.alertTitle);
        Button button1 = alert1.getWindow().findViewById(android.R.id.button1);
        // Setting font
        //Typeface face=Typeface.createFromAsset(getAssets(),"fonts/lgb.ttf");
        Typeface face = ResourcesCompat.getFont(this, R.font.lgb);
        textView.setTypeface(face);
        textView.setTextColor(getResources().getColor(R.color.purple_500));
        button1.setTextColor(Color.RED);
        alert1.setCanceledOnTouchOutside(false);
    }
    private void   loginProcess() {
        final ProgressDialog loading = ProgressDialog.show(this,"Signing you in ","Please wait");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        try {
            credentialsObject.put("UserName",btnUn.getText().toString().trim());
            credentialsObject.put("Password",btnPwd.getText().toString().trim());
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        // StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxL51N6TS4GAOMO71IJX8Hp2mJ8Pcvw6EkAF1QStRblNfo1B-kc6NHNebhVPblPSL7p/exec",
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://orbisliferesearch.com/api/AuthenticationApi/login", credentialsObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //after email sent and data added to sheets we dismiss the progress screen
                        loading.dismiss();

                        //  getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                        // going back to register screen after registration
                        try {
                            if (response.get("status") == "Success") {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else  {
                                Toast.makeText(getApplicationContext(), "status is " + response.get("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("************************* \n*****\n****", String.valueOf(error));

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();
                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        queue.add(jsonObjectRequest);

    }

}