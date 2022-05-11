package com.jkdeers.activitygame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.android.volley.toolbox.Volley;

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

public class RegisterSchool extends AppCompatActivity {
    private RequestQueue mQueue;
    Map<String, String> districtMap = new HashMap<>();
    Map<String, String> zonesMap = new HashMap<>();
    String[] districtsListStringArray,zonesListStringArray;
    ArrayAdapter arrayAdapterDistrict,arrayAdapterZones;
    AutoCompleteTextView autoCompleteTextViewDistricts,autoCompleteTextViewZone;
    int selectedDistrictId,selectedZoneId;
    EditText SchoolName,SchoolAddress,SchoolEmail,SchoolPhone,SchoolStudents,schoolUdise;
    Button RegisterButton;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);
        fillDistrict();
        SchoolName = findViewById(R.id.schoolName);
        SchoolStudents = findViewById(R.id.student_school_number);
        SchoolAddress = findViewById(R.id.school_address);
        SchoolEmail = findViewById(R.id.emaiL_school);
        SchoolPhone = findViewById(R.id.phone_school);
        schoolUdise = findViewById(R.id.school_UDISE);
        RegisterButton = findViewById(R.id.register_school_button);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              if(checkFields()) {
                  Toast.makeText(getApplicationContext(),"Passed",Toast.LENGTH_SHORT).show();
                  addPointForm();

              } else {
                  Toast.makeText(getApplicationContext(),"FAILED",Toast.LENGTH_SHORT).show();
              }
            }
        });
    }


    private void fillDistrict() {
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(HTTPReq.getRequest("https://orbisliferesearch.com/api/PrerequisiteAPIs/GetDistricts", new VolleyCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (!response.equals("[]")) {
                    //setting session key Name
                    Log.v("response:", response);
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> listDistricts = new ArrayList<String>();
                    List<String> listDistrictIds = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        listDistricts.add(jsonArray.getJSONObject(i).getString("name"));
                        listDistrictIds.add(jsonArray.getJSONObject(i).getString("id"));
                        districtMap.put(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
                    }
                    districtsListStringArray = listDistricts.toArray(new String[listDistricts.size()]);
                }
                arrayAdapterDistrict = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, R.id.textView, districtsListStringArray);
                // get reference to the autocomplete text view
                autoCompleteTextViewDistricts = (AutoCompleteTextView)
                        findViewById(R.id.school_list_district);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                autoCompleteTextViewDistricts.setAdapter(arrayAdapterDistrict);
                // resetViewsOnSelectDistrict();


                autoCompleteTextViewZone = (AutoCompleteTextView)
                        findViewById(R.id.school_list_zones);
                autoCompleteTextViewDistricts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                        String selection = (String) parent.getItemAtPosition(position);
                        int pos = -1;
                        for (String key : getKeyFromHashMapUsingValue(districtMap, autoCompleteTextViewDistricts.getText().toString().trim())) {
                            // Log.i("selected district id is :",key);
                            selectedDistrictId = Integer.parseInt(key);
                            Toast.makeText(getApplicationContext(), "DISTRICT ID " + key, Toast.LENGTH_LONG).show();
                        }

                        for (int i = 0; i < districtsListStringArray.length; i++) {
                            if (districtsListStringArray[i].equals(selection)) {

                                pos = i + 1;
                                int dId = selectedDistrictId;
                                String baseUrl = "https://orbisliferesearch.com/api/PrerequisiteAPIs/GetZones?districtid=";
                                baseUrl = baseUrl + dId;
                                mQueue.add(HTTPReq.getRequest(baseUrl, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String response) throws JSONException {

                                        //JSONArray jsonresultsarray = new JSONArray(response);
                                        if (!response.equals("[]")) {
                                            //setting session key Name
                                            Log.v("response:", response);
                                            JSONArray jsonArray = new JSONArray(response);
                                            List<String> listZones = new ArrayList<String>();
                                            List<String> listZoneIds = new ArrayList<String>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                listZones.add(jsonArray.getJSONObject(i).getString("name"));
                                                listZoneIds.add(jsonArray.getJSONObject(i).getString("id"));
                                                zonesMap.put(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
                                            }
                                            zonesListStringArray = listZones.toArray(new String[listZones.size()]);
                                        }

                                        //binding list of zones in the autocomplete view
                                        arrayAdapterZones = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, R.id.textView, zonesListStringArray);
                                        // get reference to the autocomplete text view
                                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        autoCompleteTextViewZone.setAdapter(arrayAdapterZones);


                                        autoCompleteTextViewZone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            String selection = (String) parent.getItemAtPosition(position);
                                            int pos = -1;

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                                                String selection = (String) parent.getItemAtPosition(position);
                                                int pos = -1;
                                                for (String key : getKeyFromHashMapUsingValue(zonesMap, autoCompleteTextViewZone.getText().toString().trim())) {
                                                    // Log.i("selected district id is :",key);
                                                    selectedZoneId = Integer.parseInt(key);
                                                    Toast.makeText(getApplicationContext(), "ZONE ID " + key, Toast.LENGTH_LONG).show();
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
                        Log.i("Position ", String.valueOf(pos)); //check it now in Logcat
                    }
                });

            }

            @Override
            public void onError(String result) {
                System.out.println(result);
            }
        }));
    }

    private void   addPointForm() {
        final ProgressDialog loading = ProgressDialog.show(this,"Creating your account ","Please wait");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("Title", "Title");
            object.put("Description", "sdfe2r3sa asfda ");
            object.put("Userid", 13);
            object.put("DistrictId", 1);
            object.put("ZoneId", 1);
            object.put("SchoolId", 1);
            object.put("ClassId", 4);
            object.put("TypeActivityId", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxL51N6TS4GAOMO71IJX8Hp2mJ8Pcvw6EkAF1QStRblNfo1B-kc6NHNebhVPblPSL7p/exec",
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://orbisliferesearch.com/T_School/Create", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //after email sent and data added to sheets we dismiss the progress screen
                        loading.dismiss();

                        //  getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                        // going back to register screen after registration
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        }, 1000);


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
    private static Set<String> getKeyFromHashMapUsingValue(
            Map<String, String> map, String value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }

    private boolean checkFields() {
        if (SchoolName.getText().toString().trim().equals("")) {
            SchoolName.setError("Enter School Name");
            return false;
        }
        else if (autoCompleteTextViewDistricts.getText().toString().trim().equals("")) {
            autoCompleteTextViewDistricts.setError("Select District");
            return false;
        }
        else if (autoCompleteTextViewZone.getText().toString().trim().equals("")) {
            autoCompleteTextViewZone.setError("Select Zone");
            return false;
        }
        else if (schoolUdise.getText().toString().trim().equals("")) {
            schoolUdise.setError("Enter UDISE Code");
            return false;
        }
        else if (SchoolStudents.getText().toString().trim().equals("")) {
            SchoolStudents.setError("Enter no. of students");
            return false;
        }
        else if (SchoolAddress.getText().toString().trim().equals("")) {
            SchoolAddress.setError("Enter Address");
            return false;
        }
        else if (SchoolEmail.getText().toString().trim().equals("") || !SchoolEmail.getText().toString().trim().matches(emailPattern)) {
            SchoolEmail.setError("Enter Email");
            return false;
        }
        else if (SchoolPhone.getText().toString().trim().equals("") || SchoolPhone.getText().toString().trim().length()>10 ||SchoolPhone.getText().toString().trim().length()<10) {
            SchoolPhone.setError("Enter Phone");
            return false;
        } else  {
            return  true;
        }
    }
}