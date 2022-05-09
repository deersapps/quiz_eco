package com.jkdeers.activitygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

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
    EditText SchoolName,SchoolAddress,SchoolEmail,SchoolPhone,SchoolStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);
        fillDistrict();
        fillZones();
        SchoolName = findViewById(R.id.schoolName);
        SchoolStudents = findViewById(R.id.student_school_number);
        SchoolAddress = findViewById(R.id.school_address);
        SchoolEmail = findViewById(R.id.emaiL_school);
        SchoolPhone = findViewById(R.id.phone_school);
    }

    private void fillZones() {
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
                                        autoCompleteTextViewZone = (AutoCompleteTextView)
                                                findViewById(R.id.school_list_zones);
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
            autoCompleteTextViewDistricts.setError("Enter School Name");
            return false;
        }
        else if (autoCompleteTextViewZone.getText().toString().trim().equals("")) {
            autoCompleteTextViewZone.setError("Enter School Name");
            return false;
        }
        else if (SchoolStudents.getText().toString().trim().equals("")) {
            SchoolStudents.setError("Enter School Name");
            return false;
        }
        else if (SchoolAddress.getText().toString().trim().equals("")) {
            SchoolAddress.setError("Enter School Name");
            return false;
        }
        else if (SchoolEmail.getText().toString().trim().equals("")) {
            SchoolEmail.setError("Enter School Name");
            return false;
        }
        else if (SchoolPhone.getText().toString().trim().equals("")) {
            SchoolPhone.setError("Enter School Name");
            return false;
        } else  {
            return  true;
        }
    }
}