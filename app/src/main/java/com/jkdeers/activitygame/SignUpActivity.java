package com.jkdeers.activitygame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignUp;
    Button btnLogIn;
    private String android_id;
    EditText fn, ln, zn, em, ph, un, pas;
    AutoCompleteTextView dis,school, cl;
    ArrayAdapter arrayAdapterDistrict;
    AutoCompleteTextView autoCompleteTextViewDistricts;
    String fns, lns, diss, zns, schools, cls, ems, phs, uns, pass;
    Integer districtId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String SHARED_PREFS = "shared_prefs";
    String[]
    districtsListStringArray;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("*************************************************************************", android_id);
        // get reference to the string array that we just created

        getDistrictsFromAPI();


//        // create an array adapter and pass the required parameter
//        // in our case pass the context, drop down layout , and array.
//        arrayAdapterDistrict = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, R.id.textView, districtsListStringArray);
//        // get reference to the autocomplete text view
//        autoCompleteTextViewDistricts = (AutoCompleteTextView)
//                findViewById(R.id.autoCompleteTextView);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        autoCompleteTextViewDistricts.setAdapter(arrayAdapterDistrict);
//
//
//        ArrayAdapter arrayAdapterSchool = new ArrayAdapter(this, R.layout.dropdown_item, R.id.textView, districtsListStringArray);
//        // get reference to the autocomplete text view
//        AutoCompleteTextView autoCompleteTextViewSchool = (AutoCompleteTextView)
//                findViewById(R.id.autoCompleteTextViewSchool);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        autoCompleteTextViewSchool.setAdapter(arrayAdapterSchool);
//
//
//        ArrayAdapter arrayAdapterStandard = new ArrayAdapter(this, R.layout.dropdown_item, R.id.textView, districtsListStringArray);
//        // get reference to the autocomplete text view
//        AutoCompleteTextView autoCompleteTextViewStandard = (AutoCompleteTextView)
//                findViewById(R.id.autoCompleteTextViewStandard);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        autoCompleteTextViewStandard.setAdapter(arrayAdapterStandard);


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

//        autoCompleteTextViewDistricts.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
//                getDistrictsFromAPI();
//                String selection = (String) parent.getItemAtPosition(position);
//                int pos = -1;
//
//                for (int i = 0; i < districtsListStringArray.length; i++) {
//                    if (districtsListStringArray[i].equals(selection)) {
//                        pos = i+1;
//                        break;
//                    }
//                }
//                Log.i("Position " , String.valueOf(pos)); //check it now in Logcat
//            }
//        });
//
//        autoCompleteTextViewDistricts.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

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
                registerUser();
//                startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
//                finish();
                break;
            }
        }
    }


    private void registerUser() {
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
                    object.put("Email", "tessssset@gmail.com");
                    object.put("UserName", "TestUesewwwr123");
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
                        Log.i("res", String.valueOf(response));
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
    void getDistrictsFromAPI() {
        final ProgressDialog loading = ProgressDialog.show(this,"Signing in","Please wait");

        String baseUrl = "https://orbisliferesearch.com/api/PrerequisiteAPIs/GetDistricts";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] districtsList = new String[22];
                        try {
                            loading.dismiss();
                            //JSONArray jsonresultsarray = new JSONArray(response);
                            if (!response.equals("[]")) {
                                //setting session key Name
                                Log.v("response:", response);
                                JSONArray jsonArray = new JSONArray(response);
                                List<String> listDistricts = new ArrayList<String>();
                                List<String> listDistrictIds = new ArrayList<String>();
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    listDistricts.add(jsonArray.getJSONObject(i).getString("name"));
                                    listDistrictIds.add(jsonArray.getJSONObject(i).getString("id"));
                                }
                                districtsListStringArray = listDistricts.toArray(new String[listDistricts.size()]);
                            }
                            arrayAdapterDistrict = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, R.id.textView, districtsListStringArray);
                            // get reference to the autocomplete text view
                            autoCompleteTextViewDistricts = (AutoCompleteTextView)
                                    findViewById(R.id.autoCompleteTextView);
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            autoCompleteTextViewDistricts.setAdapter(arrayAdapterDistrict);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    void getSchoolsFromAPI() {
        String baseUrl = "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonresultsarray = new JSONArray(response);
                            if (!response.equals("[]")) {
                                //setting session key Name
                                Log.v("response:", response);
                                JSONArray jsonArray = new JSONArray(response);

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    Double Lat = Double.parseDouble(jsonObject1.optString("Latitude"));
                                    Double Long = Double.parseDouble(jsonObject1.optString("Longitude"));
                                    String City = (String) jsonObject1.opt("Name");
                                    Log.v("here is ur name",jsonObject1.toString());
                                }
                            } else
                            {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
