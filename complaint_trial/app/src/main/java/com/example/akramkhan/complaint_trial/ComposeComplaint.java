package com.example.akramkhan.complaint_trial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class ComposeComplaint extends AppCompatActivity {
    private EditText cmpCtitle;
    private EditText cmpCdesc;
    private Button cmpsubmit;
    private Button takephoto;
    private RadioGroup type;
    private ImageView mImageView;
    private Spinner Hostel;
    private Spinner auth;

    private Bitmap bitmap;

    String techi;
    String complaintType;
    String image;

    private String[] hostels;
    private String[] technicians;
    private Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;

    String complaintTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_complaint);

        cmpCdesc = (EditText) findViewById(R.id.cmpCdesc);
        cmpCtitle = (EditText) findViewById(R.id.cmpCtitle);
        cmpsubmit = (Button) findViewById(R.id.cmpsubmit);
        takephoto = (Button) findViewById(R.id.takephoto);
        type = (RadioGroup) findViewById(R.id.cmpCtype);
        Hostel = (Spinner) findViewById(R.id.cmpChostel);
        mImageView = (ImageView) findViewById(R.id.cmpCImage);
        auth = (Spinner) findViewById(R.id.technician);

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        cmpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth != null && auth.getSelectedItem() != null) {

                    if (type.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "select complaint type", Toast.LENGTH_SHORT).show();
                        complaintTypes = "Personal";
                    } else {
                        complaintTypes = ((RadioButton) findViewById(type.getCheckedRadioButtonId())).getText().toString();
                    }

                    switch (auth.getSelectedItem().toString()) {
                        case "Electrician":
                            techi = "tech_1";
                            break;
                        case "Plumber":
                            techi = "tech_2";
                            break;
                        case "Sanitation":
                            techi = "tech_3";
                            break;
                        case "LAN":
                            techi = "tech_4";
                            break;
                        case "Carpenter":
                            techi = "tech_5";
                            break;
                    }

                    switch (complaintTypes) {
                        case "Personal":
                            complaintType = "0";
                            break;
                        case "Hostel":
                            complaintType = "1";
                            break;
                        case "Institute":
                            complaintType = "2";
                            break;
                    }
//                    final ProgressDialog loading = ProgressDialog.show(getApplication(), "Uploading...", "Please wait...", false, false);
                    String url = Constants.IP+"/my_api/complaints/ccomplaint";
                    /*String url = "http://192.168.201.1:80/my_api/complaints/ccomplaint?description=" + cmpCdesc.getText().toString()
                            + "&title=" + cmpCtitle.getText().toString() + "&complaint_type=" + complaintType + "&tech_id=" +
                            techi + "&image="+image;*/
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getInt("success") == 1) {
                                    //loading.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "complaint posted succesfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    //loading.dismiss();
                                    Toast.makeText(ComposeComplaint.this, "complaint not posted try again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //loading.dismiss();
                            Toast.makeText(ComposeComplaint.this, "volley error compose", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //Converting Bitmap to String

                            if(mImageView.getDrawable()==null){
                                image="";
                            }
                            else {
                                image = getStringImage(bitmap);
                            }

                            //Creating parameters
                            Map<String,String> params = new Hashtable<String, String>();
                            /*String url = "http://192.168.201.1:80/my_api/complaints/ccomplaint?description=" + cmpCdesc.getText().toString()
                            + "&title=" + cmpCtitle.getText().toString() + "&complaint_type=" + complaintType + "&tech_id=" +
                            techi + "&image="+image;*/
                            params.put("description",cmpCdesc.getText().toString());
                            params.put("title",cmpCtitle.getText().toString());
                            params.put("complaint_type",complaintType);
                            params.put("tech_id",techi);
                            params.put("image",image);

                            //returning parameters
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "select the concerned authority", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hostels = new String[]{"Karakoram", "Nilgiri", "Aravali", "Jwalamukhi", "Kumaon", "Vindyachal", "Shivalik", "Zanskar", "Satpura", "Udaigiri", "Girnar", "Himadri", "Kailash"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.spinner_item, hostels);
        Hostel.setAdapter(arrayAdapter);

        technicians = new String[]{"Electrician", "Plumber", "Sanitation", "LAN", "Carpenter"};
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, technicians);
        auth.setAdapter(arrayAdapter1);
    }

/*    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, 100);
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            super.onActivityResult(requestCode,resultCode,data);
            Uri filepath= data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);

                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }/*

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                setPic();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }*/
    }

/*    private void setPic() {
        try {
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            mImageView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }*/

/*    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }*/

/*    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }*/

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }
}
