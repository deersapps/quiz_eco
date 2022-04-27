package com.jkdeers.activitygame.ui.dashboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.jkdeers.activitygame.MainActivity;
import com.jkdeers.activitygame.R;
import com.jkdeers.activitygame.databinding.FragmentDashboardBinding;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    int SELECT_PICTURE = 300;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "AssetMapper";
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String FilePathImage="";

    Button camButton ;
    Button submitButton ;
    CheckBox consent ;
    AutoCompleteTextView autoCompleteTextView ;
    TextView tvTitle;
    ImageView imgIcon;
    TextInputLayout ddLayout;
    Button galleryButton;
    private Uri fileUri; // file url to store image/video

    private ImageView imgPreviewassets;
    private VideoView videoPreview;
    String imageUrl = "No image selected";
    // FOR REAL IMAGE FROM ASSET MAPPER CODE//
    private ImageView imageView;
    TextView imagepath;

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    // key for storing email.
    public static final String EMAIL_KEY = "email_key";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        imgPreviewassets = binding.imageCapture;
        imagepath = binding.textDashboard;
        tvTitle = binding.addActivityTitle;
        imgIcon = binding.imageIcon;
        ddLayout = binding.activityDropDown;
        galleryButton = binding.btnAddPhotoGallery;
        String[] countries = getResources().getStringArray(R.array.activities);
        ArrayAdapter arrayAdapterActivity = new ArrayAdapter(getContext(), R.layout.dropdown_item, R.id.textView, countries);
        // get reference to the autocomplete text view
        autoCompleteTextView = (AutoCompleteTextView)
                binding.activtyAutoCompleteView;
        //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        autoCompleteTextView.setAdapter(arrayAdapterActivity);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        camButton = binding.btnAddPhoto;
        submitButton = binding.btnAddActivity;
        consent = binding.addActivityCheckBox;

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
                String FilePathImage="";
                FilePathImage=fileUri.toString();

                imagepath.setText(FilePathImage);
            }
        });
        
        camButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                // capture picture
                captureImage();
                FilePathImage="";
                FilePathImage=fileUri.toString();

                imagepath.setText(FilePathImage);

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgPreviewassets.getDrawable() == null) {
                    showPopup("Please take pic");
                } else if (!consent.isChecked()) {
                    showPopup("Please check the consent checkbox");
                } else {
                    try {
                        upload();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        return root;
    }

    private void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent.
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

        StrictMode.setVmPolicy(builder.build());

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    private void captureImage() {


        if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            // .startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // start the image capture Intent.
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

            StrictMode.setVmPolicy(builder.build());
            //startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }






    }

    public Uri getOutputMediaFileUri(int type) {
        return  Uri.fromFile(getOutputMediaFile(type));
        // return  FileProvider.getUriForFile(Context, Context.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));

    }
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        final File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }



        // Create a media file name
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
            else
            {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == 1000) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                // User checks permission.

            } else {
                Toast.makeText(getContext(), "Permission is denied.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }

    }
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();


            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // video successfully recorded
                // preview the recorded video
                //  previewVideo();
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == SELECT_PICTURE) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (resultCode ==getActivity().RESULT_OK) {
                // Get the url of the image from data
                fileUri = data.getData();
                if (null != fileUri) {
                    // update the preview image in the layout
                    imgPreviewassets.setImageURI(fileUri);

                    imgPreviewassets.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void createTestFile() {
        // String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        // If Target API level is 29(Android 10),
        // you should access local path in scoped storage mode.
        File localStorage = getActivity().getExternalFilesDir(null);
        if (localStorage == null) { return; }
        String storagePath = localStorage.getAbsolutePath();
        String rootPath = storagePath + "/test";
        String fileName = "/test.zip";

        File root = new File(rootPath);
        if(!root.mkdirs()) {
            Log.i("Test", "This path is already exist: " + root.getAbsolutePath());
        }

        File file = new File(rootPath + fileName);
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(
                    getContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (!file.createNewFile()) {
                    Log.i("Test", "This file is already exist: " + file.getAbsolutePath());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void upload() throws IOException {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.setCanceledOnTouchOutside(false);
        pd.show();


        if (fileUri != null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtension(fileUri));
            //FirebaseAuth.instance.signInAnonymously();

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            //uploading the image

            StorageTask uploadtask = filePath.putBytes(data);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = ref.push().getKey();
                    pd.dismiss();
                    addPointForm();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image was selected!", Toast.LENGTH_SHORT).show();
        }
    }
    private void previewCapturedImage() {
        try {
            // hide video preview

            imagepath=binding.textDashboard;
            imgPreviewassets.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize =16;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreviewassets.setImageBitmap(bitmap);



        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(uri));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    // adding the point info into goole docs
    private void   addPointForm() {
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Saving the earth bit by bit ","Please wait");
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        String lat = "34";
        String lon = "77";
        String photourl = imageUrl;
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // getting data from shared prefs and
        // storing it in our string variable.
        String addedby = sharedpreferences.getString(EMAIL_KEY, null);
        String remarksText = "remarks";


        // StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxL51N6TS4GAOMO71IJX8Hp2mJ8Pcvw6EkAF1QStRblNfo1B-kc6NHNebhVPblPSL7p/exec",
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxYC8JSfzZqxlq9PgbeJdv48FeFoY2cXWF-whQZzorKjWINGoEQNKzvhF8epLkq1SAK/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //after email sent and data added to sheets we dismiss the progress screen
                        loading.dismiss();
                        LottieAnimationView addedIt = binding.addedLottie;
                        addedIt.setVisibility(View.VISIBLE);
                        imgPreviewassets.setVisibility(View.GONE);
                        camButton.setVisibility(View.GONE);
                        autoCompleteTextView.setVisibility(View.GONE);
                        submitButton.setVisibility(View.GONE);
                        consent.setVisibility(View.GONE);
                        tvTitle.setVisibility(View.GONE);
                        imgIcon.setVisibility(View.GONE);
                        ddLayout.setVisibility(View.GONE);
                        galleryButton.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                        // going back to register screen after registration
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        }, 4000);


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

                //here we pass params
                parmas.put("action","addItem");
                parmas.put("lat",lat);
                parmas.put("lon",lon);
                parmas.put("city","city");
                parmas.put("name","name");
                parmas.put("alt","alt");
                parmas.put("dist","dist");
                parmas.put("vil","vil");
                parmas.put("landmark","landmark");
                parmas.put("topo","topo");
                parmas.put("ownership","ownership");
                parmas.put("area","area");
                parmas.put("wetlandtype","wetlandtype");
                // parmas.put("typ",typ);
                parmas.put("depth","depth");
                parmas.put("inlet","inlet");
                parmas.put("outlet","outlet");
                parmas.put("qual","qual");
                parmas.put("bio","bio");
                parmas.put("biodiversityfill","biodiversityEditTextBoxString");
                parmas.put("ecoservice","ecoservice");
                parmas.put("importance","importance");
                parmas.put("rightsandpriveleges","rightsandpriveleges");
                parmas.put("threats","threats");
                parmas.put("wetlandproducts","wetlandproducts");
                parmas.put("culturalservice","culturalservice");
                parmas.put("remarksanyfill",remarksText);
                parmas.put("addedby","asim");
                if (photourl!=null) {
                    parmas.put("photourl",photourl);
                } else {
                    parmas.put("photourl","no image taken for this point");
                }
                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        queue.add(stringRequest);

    }
    private void showPopup( String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(message)
                .setNegativeButton("Ok", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
        // Getting the view elements
        TextView textView = alert1.getWindow().findViewById(android.R.id.message);
        //TextView alertTitle = (TextView) alert1.getWindow().findViewById(R.id.alertTitle);
        Button button1 = alert1.getWindow().findViewById(android.R.id.button1);
        Button button2 = alert1.getWindow().findViewById(android.R.id.button2);
        // Setting font
        //Typeface face=Typeface.createFromAsset(getAssets(),"fonts/lgb.ttf");
        Typeface face = ResourcesCompat.getFont(getContext(), R.font.lgb);
        textView.setTypeface(face);
        textView.setTextColor(getResources().getColor(R.color.colorTheme));
        button1.setTextColor(getResources().getColor(R.color.colorThemeFaded));


    }
}