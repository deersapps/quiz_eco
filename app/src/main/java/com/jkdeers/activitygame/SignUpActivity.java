package com.jkdeers.activitygame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
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
import android.widget.ImageView;
import android.widget.TextView;
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
    EditText fn, ln, zn, em, ph, un, pas,sec,lngTb,latTb;
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

    double lat_a,lng_a,acc;
    TextView accuracyTv;


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
        latTb = findViewById(R.id.lat_tb);
        lngTb= findViewById(R.id.lng_tb);
        accuracyTv = findViewById(R.id.accuracyGPS);

        GPSTracker gps;
        LocationManager locationManager;
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        final boolean isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final ImageView gpsIconFirstRun=(ImageView) findViewById(R.id.image_icon_gps);
        if (isGPSEnabled){
            gpsIconFirstRun.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_not_fixed_24));
            gps = new GPSTracker(SignUpActivity.this);
            lat_a= gps.getLatitude();
            lng_a= gps.getLongitude();
            acc = gps.getAccuracy();
            latTb.setText(String.valueOf(lat_a));
            lngTb.setText(String.valueOf(lng_a));
            accuracyTv.setText("Accuracy(m):"+String.valueOf(acc));
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat_a + "\nLong: " + lng_a, Toast.LENGTH_SHORT).show();

        }
        else{
            gpsIconFirstRun.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_off_24));
        }




        gpsIconFirstRun.setOnClickListener(new View.OnClickListener() {
            GPSTracker gps;
            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(SignUpActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){
                    lat_a= gps.getLatitude();
                    lng_a= gps.getLongitude();
                    latTb.setText(String.valueOf(lat_a));
                    lngTb.setText(String.valueOf(lng_a));
                }else{
                    gps.showSettingsAlert();
                }

            }
        });
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
//                                                for (String key : getKeyFromHashMapUsingValue(zonesMap, autoCompleteTextViewZone.getText().toString().trim())) {
//                                                    // Log.i("selected school id is :",key);
//                                                    selectedZoneId = Integer.parseInt(key);
//                                                    Toast.makeText(getApplicationContext(), "ZONE ID " + key, Toast.LENGTH_LONG).show();
//
//                                                }

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
            GPSTracker gps;
            LocationManager locationManager;
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            final boolean isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            gps = new GPSTracker(SignUpActivity.this);
            if (!isGPSEnabled) {
                gps.showSettingsAlert();
            }
            // final ProgressDialog loading = ProgressDialog.show(this,"Registering you now","Please wait");



            JSONObject object = new JSONObject();


            try {
                        object.put("FirstName",fns);
                        object.put("LastName", lns);
                        object.put( "DistrictId",selectedDistrictId);
                        object.put( "ZoneId",selectedZoneId);
                        object.put( "SchoolId", selectedSchoolId);
                        object.put("ClassId", selectedClassId);
                        object.put("PhoneNumber", phs);
                        object.put("Email", ems);
                        object.put("UserName", uns);
                        object.put("Password", pass);
                        object.put("latitude", "34.0352758");
                        object.put("longitude", "74.5866882");
                        object.put("Section", "1");
                        object.put("RollNumber", "541");
                        object.put("Age", 12);
                        object.put("Address", "test");
                        object.put("Pincode", "test");
                        object.put("IsActive", true);
                        object.put("DOB","2001-05-03T17:21:27.717");
                        object.put("AndroidId",android_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://orbisliferesearch.com/api/AuthenticationApi/register", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String s="";
                            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            try {
                                s = (String) response.get("status");
                            } catch (Error | JSONException e) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                            if (s.equals("Success")) {

                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                Intent intentLoginStudent = new Intent(getApplicationContext(),Signin.class);
                                startActivity(intentLoginStudent);
                                finish();

                            } else if (s.equals("AL_EXISTS")) {

                                Toast.makeText(getApplicationContext(), "This username already taken", Toast.LENGTH_LONG).show();
                                un.setError("try a different user name");

                            } else {
                                Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_LONG).show();
                            }
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


        } else {
            Toast.makeText(getApplicationContext(), "Please check errors", Toast.LENGTH_SHORT).show();
        }
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
        } else  {
            fn.setError(null);
        }

        if (ln.length() == 0) {
            ln.setError("Last Name is required");
            return false;
        } else  {
            ln.setError(null);
        }

        if (diss.equals("District") || diss.length() == 0) {
            dis.setError("Please select district");
            return false;
        } else  {
            dis.setError(null);
        }

        if (schools.equals("Other")) {
            if (otherEditTextBoxSchoolView.getVisibility() == View.VISIBLE)
            if (OtherSchoolName.length() == 0) {
                otherEditTextBoxSchoolView.setError("Please fill in the school name");
                return false;
            }  else  {
                otherEditTextBoxSchoolView.setError(null);
            }
        }
        if (schools.equals("School") || schools.length() == 0) {
            school.setError("Please select School");
            return false;
        } else  {
            school.setError(null);
        }
        if (cls.equals("Class") || cls.length() == 0) {
            cl.setError("Please select Class");
            return false;
        } else  {
            cl.setError(null);
        }
        if (sect.equals("Section") || sect.length() == 0 ) {
            sec.setError("Please select section");
            return false;
        } else  {
            sec.setError(null);
        }
        if (pass.length() < 7 || !pass.trim().matches(passwordPattern)) {
            pas.setError("pass has to be atleast one digit, one lower, one upper , one special and >7 chars and no white space is allowed");
            return false;
        }else  {
            pas.setError(null);
        }
        if (phs.length() < 10 || phs.length() > 10) {
            ph.setError("Invalid Phone number");
            return false;
        }else  {
            ph.setError(null);
        }
        if (!ems.trim().matches(emailPattern)) {
            em.setError("Please enter valid email");
            return false;
        } else  {
            em.setError(null);
        }
        if (uns.length()<5) {
            un.setError("Username too short");
            return false;
        } else  {
            un.setError(null);
        }
        if (zns.length() == 0 || zns.equals("Zones")) {
            zn.setError("Select Zones");
        } else  {
            zn.setError(null);
        }

        if (otherEditTextBoxSchoolView.getVisibility() == View.VISIBLE) {
            otherEditTextBoxSchoolView.setError(null);
        }

        // after all validation return true.
        return true;
    }

    /* after redirecting to gps settings we call onreusume method to change state of toggle button to show gps is on
     * */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        LocationManager locationManager;
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        final boolean isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        super.onResume();
        final ImageView GPSbutton=(ImageView) findViewById(R.id.image_icon_gps);
        if (isGPSEnabled){
            Toast.makeText(getApplicationContext(), "GPS ENABLED" , Toast.LENGTH_LONG).show();
            GPSbutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_not_fixed_24));
        }
        else{
            Toast.makeText(getApplicationContext(), "GPS DISABLED CLICK THE LOCATION BUTTON  ON TOP TO ENABLE IT" , Toast.LENGTH_LONG).show();
            GPSbutton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_off_24));
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Log.v("blah", "blah blah");
            ImageView toggle_utilityscreen_gps = (ImageView) findViewById(R.id.image_icon_gps);
            toggle_utilityscreen_gps.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_gps_not_fixed_24));
        }


    }
}
