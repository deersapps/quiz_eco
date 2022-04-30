package com.jkdeers.activitygame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignUp;
    Button btnLogIn;
    private String android_id;
    EditText fn, ln, dis, zn, school, cl, em, ph, un, pas;
    String fns, lns, diss, zns, schools, cls, ems, phs, uns, pass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String SHARED_PREFS = "shared_prefs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("*************************************************************************", android_id);
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

        fn = findViewById(R.id.fn_tb);
        ln = findViewById(R.id.ln_tb);
        dis = findViewById(R.id.autoCompleteTextView);
        zn = findViewById(R.id.zn_tb);
        school = findViewById(R.id.autoCompleteTextViewSchool);
        cl = findViewById(R.id.autoCompleteTextViewStandard);
        em = findViewById(R.id.email_tb);
        ph = findViewById(R.id.phone_tb);
        un = findViewById(R.id.username_tb);
        pas = findViewById(R.id.password_tb);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin: {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            }
            case R.id.btnSignUp: {
                addItemToSheet();
//                startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
//                finish();
                break;
            }
        }
    }


    private void addItemToSheet() {
        // final ProgressDialog loading = ProgressDialog.show(this,"Registering you now","Please wait");

        fns = fn.getText().toString().trim();
        lns = ln.getText().toString().trim();
        diss = dis.getText().toString().trim();
        zns = zn.getText().toString().trim();
        schools = school.getText().toString().trim();
        cls = cl.getText().toString().trim();
        ems = em.getText().toString().trim();
        phs = ph.getText().toString().trim();
        uns = un.getText().toString().trim();
        pass = pas.getText().toString().trim();

        int min = 0;
        int max = 9999;

        JSONObject object = new JSONObject();
        try {
                    object.put("FirstName","Sheikh Shuaib");
                    object.put("LastName", "Ashraf");
                    object.put( "DistrictId",1);
                    object.put( "ZoneId",1);
                    object.put( "SchoolId", 1);
                    object.put("ClassId", 1);
                    object.put("PhoneNumber", "9898989887");
                    object.put("Email", "tessssst@gmail.com");
                    object.put("UserName", "TestUsewwwr123");
                    object.put("Password", "Very@3434");
                    object.put("latitude", "34.0352758");
                    object.put("longitude", "74.5866882");
                    object.put("Section", "1");
                    object.put("RollNumber", "541");
                    object.put("Age", 12);
                    object.put("Address", "test");
                    object.put("Pincode", "test");
                    object.put("IsActive", false);
                    object.put("DOB","2001-05-03T17:21:27.717");
                    object.put("AndroidId","test234324sf");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://orbisliferesearch.com/api/AuthenticationApi/register", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                getActivity().startActivity(intent);
//                            }
//                        }, 1000);


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

                //here we pass params
//                parmas.put("Title", "Swerwer sasdfb");
//                parmas.put("Description", "sdfe2r3sa asfda ");
//                parmas.put("Userid", String.valueOf(13));
//                parmas.put("DistrictId", String.valueOf(1));
//                parmas.put("ZoneId", String.valueOf(1));
//                parmas.put("SchoolId", String.valueOf(1));
//                parmas.put("ClassId", String.valueOf(1));
//                parmas.put("TypeActivityId", String.valueOf(1));
//                if (photourl!=null) {
//                    parmas.put("Attachment",photourl);
//                } else {
//                    parmas.put("photourl","no image taken for this point");
//                }
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
