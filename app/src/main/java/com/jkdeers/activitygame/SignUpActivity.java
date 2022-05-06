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
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSignUp;
    Button btnLogIn;
    private String android_id;
    EditText fn, ln, zn, em, ph, un, pas,sec;
    AutoCompleteTextView dis,school, cl;
    ArrayAdapter arrayAdapterDistrict,arrayAdapterSchool,arrayAdapterClass,arrayAdapterZones;
    AutoCompleteTextView autoCompleteTextViewDistricts,autoCompleteTextViewClass,autoCompleteTextViewZone,autoCompleteTextViewSchool ;
    String fns, lns, diss, zns, schools, cls, sect, ems, phs, uns, pass, OtherSchoolName;
    Integer districtId;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//    Explanations for passwod pattern:
//
//            (?=.*[0-9]) a digit must occur at least once
//            (?=.*[a-z]) a lower case letter must occur at least once
//            (?=.*[A-Z]) an upper case letter must occur at least once
//            (?=.*[@#$%^&+=]) a special character must occur at least once
//            (?=\\S+$) no whitespace allowed in the entire string
//            .{8,} at least 8 characters
    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{7,}$";
    public static final String SHARED_PREFS = "shared_prefs";
    String[]
    districtsListStringArray;
    String[] schoolListStringArray;
    String[] classListStringArray;
    String[] zonesListStringArray;
    private RequestQueue mQueue;
    TextInputLayout otherSchoolNameTextInputLayout;
    EditText otherEditTextBoxSchoolView;
    boolean isAllFieldsChecked = false;
    Map<String, String> classMap = new HashMap<>();
    Map<String, String> districtMap = new HashMap<>();
    Map<String, String> schoolMap = new HashMap<>();
    Map<String, String> zonesMap = new HashMap<>();
    int selectedDistrictId, selectedSchoolId, selectedClassId, selectedUserId,selectedZoneId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("*************************************************************************", android_id);
        // get reference to the string array that we just created
        InitializeDropDowns();
        InitializeDropDownClass();

        Log.i("districtsListStringArray", String.valueOf(districtsListStringArray));



        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogin);

        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);

        fn = findViewById(R.id.fn_tb);
        ln = findViewById(R.id.ln_tb);
        dis = findViewById(R.id.autoCompleteTextView);
        zn = findViewById(R.id.autoCompleteTextViewZones);
        school = findViewById(R.id.autoCompleteTextViewSchool);
        cl = findViewById(R.id.autoCompleteTextViewStandard);
        sec = findViewById(R.id.section_tb);
        em = findViewById(R.id.email_tb);
        ph = findViewById(R.id.phone_tb);
        un = findViewById(R.id.username_tb);
        pas = findViewById(R.id.password_tb);
    }

    private void InitializeDropDownClass() {
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(HTTPReq.getRequest( "https://orbisliferesearch.com/api/PrerequisiteAPIs/GetClasses", new VolleyCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                //JSONArray jsonresultsarray = new JSONArray(response);
                if (!response.equals("[]")) {
                    //setting session key Name
                    Log.v("response:", response);
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> listClass = new ArrayList<String>();
                    List<String> lisClassId = new ArrayList<String>();


                    for(int i=0;i<jsonArray.length();i++)
                    {
                        listClass.add(jsonArray.getJSONObject(i).getString("name"));

                        lisClassId.add(jsonArray.getJSONObject(i).getString("id"));
                        classMap.put(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
                    }
                    classListStringArray = listClass.toArray(new String[listClass.size()]);
                    Log.i("response map", String.valueOf(classMap));
                }
                arrayAdapterClass = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, R.id.textView, classListStringArray);
                // get reference to the autocomplete text view
                autoCompleteTextViewClass = (AutoCompleteTextView)
                        findViewById(R.id.autoCompleteTextViewStandard);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                autoCompleteTextViewClass.setAdapter(arrayAdapterClass);

                autoCompleteTextViewClass.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                        for (String key : getKeyFromHashMapUsingValue(classMap, autoCompleteTextViewClass.getText().toString().trim())) {
                           // Log.i("selected class id is :",key);
                            selectedClassId = Integer.parseInt(key);
                            Toast.makeText(getApplicationContext(), "CLASS ID " + key, Toast.LENGTH_LONG).show();

                        }
                    }
                });




            }

            @Override
            public void onError(String result) {
                System.out.println(result);
            }
        }));
    }

    private static Set<String> getKeyFromHashMapUsingValue(
            Map<String, String> map, String value) {
            return map
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }
    private void InitializeDropDowns() {
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(HTTPReq.getRequest( "https://orbisliferesearch.com/api/PrerequisiteAPIs/GetDistricts", new VolleyCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

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
                        districtMap.put(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
                    }
                    districtsListStringArray = listDistricts.toArray(new String[listDistricts.size()]);
                }
                arrayAdapterDistrict = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, R.id.textView, districtsListStringArray);
                // get reference to the autocomplete text view
                autoCompleteTextViewDistricts = (AutoCompleteTextView)
                        findViewById(R.id.autoCompleteTextView);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                autoCompleteTextViewDistricts.setAdapter(arrayAdapterDistrict);
               // resetViewsOnSelectDistrict();

                autoCompleteTextViewDistricts.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                        getDistrictsFromAPI();
                        String selection = (String) parent.getItemAtPosition(position);
                        int pos = -1;
                        for (String key : getKeyFromHashMapUsingValue(districtMap, autoCompleteTextViewDistricts.getText().toString().trim())) {
                           // Log.i("selected district id is :",key);
                            selectedDistrictId = Integer.parseInt(key);
                            Toast.makeText(getApplicationContext(), "DISTRICT ID " + key, Toast.LENGTH_LONG).show();
                        }

                        for (int i = 0; i < districtsListStringArray.length; i++) {
                            if (districtsListStringArray[i].equals(selection)) {

                                pos = i+1;
                                int dId = selectedDistrictId;
                                String baseUrl = "https://orbisliferesearch.com/api/PrerequisiteAPIs/GetZones?districtid=";
                                baseUrl = baseUrl+dId;
                                mQueue.add(HTTPReq.getRequest( baseUrl, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String response) throws JSONException {

                                        //JSONArray jsonresultsarray = new JSONArray(response);
                                        if (!response.equals("[]")) {
                                            //setting session key Name
                                            Log.v("response:", response);
                                            JSONArray jsonArray = new JSONArray(response);
                                            List<String> listZones = new ArrayList<String>();
                                            List<String> listZoneIds = new ArrayList<String>();
                                            for(int i=0;i<jsonArray.length();i++)
                                            {
                                                listZones.add(jsonArray.getJSONObject(i).getString("name"));
                                                listZoneIds.add(jsonArray.getJSONObject(i).getString("id"));
                                                zonesMap.put(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
                                            }
                                            zonesListStringArray = listZones.toArray(new String[listZones.size()]);
                                        }

                                        //binding list of zones in the autocomplete view
                                        arrayAdapterZones = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, R.id.textView, zonesListStringArray);
                                        // get reference to the autocomplete text view
                                        autoCompleteTextViewZone = (AutoCompleteTextView)
                                                findViewById(R.id.autoCompleteTextViewZones);
                                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        autoCompleteTextViewZone.setAdapter(arrayAdapterZones);


                                        autoCompleteTextViewZone.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                            String selection = (String) parent.getItemAtPosition(position);
                                            int pos = -1;
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                                                for (String key : getKeyFromHashMapUsingValue(zonesMap, autoCompleteTextViewZone.getText().toString().trim())) {
                                                    // Log.i("selected school id is :",key);
                                                    selectedSchoolId = Integer.parseInt(key);
                                                    Toast.makeText(getApplicationContext(), "ZONE ID " + key, Toast.LENGTH_LONG).show();

                                                }

                                                String selection = (String) parent.getItemAtPosition(position);
                                                int pos = -1;
                                                for (String key : getKeyFromHashMapUsingValue(zonesMap, autoCompleteTextViewZone.getText().toString().trim())) {
                                                    // Log.i("selected district id is :",key);
                                                    selectedZoneId = Integer.parseInt(key);
                                                    Toast.makeText(getApplicationContext(), "ZONE ID " + key, Toast.LENGTH_LONG).show();
                                                }

                                                for (int i = 0; i < districtsListStringArray.length; i++) {
                                                    if (zonesListStringArray[i].equals(selection)) {

                                                        pos = i+1;
                                                        int dId = selectedDistrictId;
                                                        int zId = selectedZoneId;
                                                        String baseUrl = "https://orbisliferesearch.com/api/PrerequisiteAPIs/Getschools?districtid=";
                                                        baseUrl = baseUrl+dId;
                                                        baseUrl = baseUrl +"&zoneid="+zId;
                                                        mQueue.add(HTTPReq.getRequest( baseUrl, new VolleyCallback() {
                                                            @Override
                                                            public void onSuccess(String response) throws JSONException {

                                                                //JSONArray jsonresultsarray = new JSONArray(response);
                                                                if (!response.equals("[]")) {
                                                                    //setting session key Name
                                                                    Log.v("response:", response);
                                                                    JSONArray jsonArray = new JSONArray(response);
                                                                    List<String> listSchools = new ArrayList<String>();
                                                                    List<String> listSchoolsIds = new ArrayList<String>();
                                                                    for(int i=0;i<jsonArray.length();i++)
                                                                    {
                                                                        listSchools.add(jsonArray.getJSONObject(i).getString("name"));
                                                                        listSchoolsIds.add(jsonArray.getJSONObject(i).getString("id"));
                                                                        schoolMap.put(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
                                                                    }
                                                                    listSchools.add("Other");
                                                                    schoolListStringArray = listSchools.toArray(new String[listSchools.size()]);
                                                                }

                                                                //binding the list of schools to the autocompleteview
                                                                arrayAdapterSchool = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, R.id.textView, schoolListStringArray);
                                                                // get reference to the autocomplete text view
                                                                autoCompleteTextViewSchool = (AutoCompleteTextView)
                                                                        findViewById(R.id.autoCompleteTextViewSchool);
                                                                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                                                autoCompleteTextViewSchool.setAdapter(arrayAdapterSchool);


                                                                autoCompleteTextViewSchool.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                                                    @Override
                                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                                                                        for (String key : getKeyFromHashMapUsingValue(schoolMap, autoCompleteTextViewSchool.getText().toString().trim())) {
                                                                            // Log.i("selected school id is :",key);
                                                                            selectedSchoolId = Integer.parseInt(key);
                                                                            Toast.makeText(getApplicationContext(), "SCHOOL ID " + key, Toast.LENGTH_LONG).show();
                                                                        }

                                                                        if (autoCompleteTextViewSchool.getText().toString().trim().equals("Other")) {

                                                                            otherSchoolNameTextInputLayout = findViewById(R.id.otherEditTextSchool);
                                                                            otherSchoolNameTextInputLayout.setVisibility(View.VISIBLE);
                                                                            otherEditTextBoxSchoolView = findViewById(R.id.otherEditTextBoxSchool);

                                                                            OtherSchoolName = otherEditTextBoxSchoolView.getText().toString().trim();


                                                                        }
                                                                        else {
                                                                            otherSchoolNameTextInputLayout = findViewById(R.id.otherEditTextSchool);
                                                                            otherSchoolNameTextInputLayout.setVisibility(View.GONE);
                                                                            otherEditTextBoxSchoolView = findViewById(R.id.otherEditTextBoxSchool);

                                                                          //  OtherSchoolName = otherEditTextBoxSchoolView.getText().toString().trim();
                                                                        }
                                                                    }
                                                                });




                                                            }

                                                            @Override
                                                            public void onError(String result) {
                                                                System.out.println(result);
                                                            }
                                                        }));
                                                        break;
                                                    }
                                                }
                                                Log.i("Position " , String.valueOf(pos)); //check it now in Logcat





                                            }
                                        });




                                    }

                                    @Override
                                    public void onError(String result) {
                                        System.out.println(result);
                                    }
                                }));
                                break;
                            }
                        }
                        Log.i("Position " , String.valueOf(pos)); //check it now in Logcat
                    }
                });

            }

            @Override
            public void onError(String result) {
                System.out.println(result);
            }
        }));
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
        if (school.getText().equals("Other")) {
            OtherSchoolName = otherEditTextBoxSchoolView.getText().toString().trim();
            schools = OtherSchoolName;
        } else  {
            schools = school.getText().toString().trim();
        }
        cls = cl.getText().toString().trim();
        sect = sec.getText().toString().trim();
        ems = em.getText().toString().trim();
        phs = ph.getText().toString().trim();
        uns = un.getText().toString().trim();
        pass = pas.getText().toString().trim();
        int min = 0;
        int max = 9999;

        isAllFieldsChecked = CheckAllFields();
        if (isAllFieldsChecked) {
            Toast.makeText(getApplicationContext(), "All validations passed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Please check errors", Toast.LENGTH_SHORT).show();
        }
        JSONObject object = new JSONObject();


        try {
                    object.put("FirstName","Sheikh Shuaib");
                    object.put("LastName", "Ashraf");
                    object.put( "DistrictId",1);
                    object.put( "ZoneId",1);
                    object.put( "SchoolId", 1);
                    object.put("ClassId", 4);
                    object.put("PhoneNumber", "9898989887");
                    object.put("Email", "asimjan123@gmail.com");
                    object.put("UserName", "TestUAsim123");
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
        final ProgressDialog loading = ProgressDialog.show(this,"Fetching List of school in this district","Please wait");

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
                                    districtMap.put(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
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
        final ProgressDialog loading = ProgressDialog.show(this,"Fetching List of school in this district","Please wait");

        String baseUrl = "https://orbisliferesearch.com/api/PrerequisiteAPIs/Getschools?districtid=";
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
                                    districtMap.put(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
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


    private boolean CheckAllFields() {
        if (fn.length() == 0) {
            fn.setError("Name is required");
            return false;
        }

        if (ln.length() == 0) {
            ln.setError("Last Name is required");
            return false;
        }

        if (dis.getText().toString().trim().equals("District") || diss.length() == 0) {
            dis.setError("Please select district");
            return false;
        }

        if (school.getText().toString().trim().equals("Other")) {
            if (otherEditTextBoxSchoolView.getVisibility() == View.VISIBLE)
            if (OtherSchoolName.length() == 0) {
                otherEditTextBoxSchoolView.setError("Please fill in the school name");
            }
        }
        if (school.getText().toString().trim().equals("School") || schools.length() == 0) {
            school.setError("Please select School");
            return false;
        }
        if (cl.getText().toString().trim().equals("Class") || cls.length() == 0) {
            cl.setError("Please select School");
            return false;
        }
        if (sec.getText().toString().trim().equals("Section") || sect.length() == 0 ) {
            sec.setError("Please select section");
            return false;
        } else if (pas.length() < 7 || !pas.getText().toString().trim().matches(passwordPattern)) {
            pas.setError("pass has to be atleast one digit, one lower, one upper , one special and >7 chars and no white space is allowed");
            return false;
        } else if (ph.length() < 10 || ph.length() > 10) {
            ph.setError("Invalid Phone number");
            return false;
        }
        else if (!em.getText().toString().trim().matches(emailPattern)) {
            em.setError("Please enter valid email");
            return false;
        } else if (un.length()<5) {
            un.setError("Username too short");
            return false;
        } else if (zn.length() == 0 || zn.getText().equals("Zones")) {
            zn.setError("Select Zones");
        }
        zn.setError(null);
        school.setError(null);
        fn.setError(null);
        ln.setError(null);
        dis.setError(null);
        school.setError(null);
        if (otherEditTextBoxSchoolView.getVisibility() == View.VISIBLE) {
            otherEditTextBoxSchoolView.setError(null);
        }
        cl.setError(null);
        sec.setError(null);
        pas.setError(null);
        ph.setError(null);
        em.setError(null);
        // after all validation return true.
        return true;
    }

}
