package com.example.akramkhan.complaint_trial;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class complaintdetails extends AppCompatActivity {
    private TextView cmpDid;
    private TextView cmpDtitle;
    private TextView cmpDupvote;
    private TextView cmpDdownvote;
    private TextView cmpDtime;
    private TextView cmpDhostel;
    private TextView cmpDstatus;
    private TextView cmpDtype;
    private TextView cmpDdesc;
    private TextView cmpDtechi;
    private TextView notidesc;
    private Button Resolved;
    private Button upvote;
    private Button downvote;
    private Button Notify;
    private Button redundant;
    private ImageView display;

    String userid;
    String cmptype="aa";

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintdetails);
        final Intent intent=getIntent();


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        cmpDid= (TextView) findViewById(R.id.cmpDid);
        notidesc = (TextView) findViewById(R.id.notidesc);
        cmpDtitle= (TextView) findViewById(R.id.cmpDtitle);
        cmpDtime= (TextView) findViewById(R.id.cmpDtime);
        cmpDtype= (TextView) findViewById(R.id.cmpDtype);
        cmpDupvote= (TextView) findViewById(R.id.cmpDupvote);
        cmpDdownvote= (TextView) findViewById(R.id.cmpDdownvote);
        cmpDhostel= (TextView) findViewById(R.id.cmpDhostel);
        cmpDstatus= (TextView) findViewById(R.id.cmpDstatus);
        cmpDtechi = (TextView) findViewById(R.id.cmpDtechi);
        cmpDtype = (TextView) findViewById(R.id.cmpDtype);
        cmpDdesc= (TextView) findViewById(R.id.cmpDdesc);

        Resolved = (Button) findViewById(R.id.resolved);
        upvote = (Button) findViewById(R.id.upvote);
        downvote = (Button) findViewById(R.id.downvote);
        Notify = (Button) findViewById(R.id.notify);
        redundant = (Button) findViewById(R.id.redundant);

        display = (ImageView) findViewById(R.id.cmpDimaged);


        final ProgressDialog q = new ProgressDialog(complaintdetails.this);
        q.setMessage("Loading data......");
        q.setCancelable(false);
        q.show();
        String url=Constants.IP+"/my_api/complaints/complaint?complaint_id="+intent.getStringExtra("complaintid");
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int success = jsonObject.getInt("success");
                    if(success==1){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("complaints");
                        userid = jsonObject1.getString("user_id");
                        String imaged=jsonObject1.getString("image");
                        byte[] decodedString = Base64.decode(imaged, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        String status="UnResolved";
                        String auth = "Electrician";

                        switch (jsonObject1.getString("complaint_status")){
                            case "0":
                                status="UnResolved";
                                break;
                            case "1":
                                status="Redundant";
                                break;
                            case "2":
                                status="Resolved";
                                break;
                        }

                        switch (jsonObject1.getString("tech_id")){
                            case "tech_1":
                                auth="Electrician";
                                break;
                            case "tech_2":
                                auth="Plumber";
                                break;
                            case "tech_3":
                                auth="Sanitation";
                                break;
                            case "tech_4":
                                auth="LAN";
                                break;
                            case "tech_5":
                                auth="Carpenter";
                                break;
                        }

                        switch (jsonObject1.getString("complaint_type")){
                            case "0":
                                cmptype="Personal";
                                break;
                            case "1":
                                cmptype="Hostel";
                                break;
                            case "2":
                                cmptype="Institute";
                                break;
                        }

                        if(jsonObject1.getString("notifications").matches("")){
                            notidesc.setText("No notifications yet");
                        }
                        else {
                            notidesc.setText(jsonObject1.getString("notifications"));
                        }

                        cmpDid.setText(jsonObject1.getString("complaint_id"));
                        cmpDtitle.setText(jsonObject1.getString("title"));
                        cmpDtime.setText(jsonObject1.getString("time"));
                        cmpDupvote.setText(jsonObject1.getString("upvote"));
                        cmpDdownvote.setText(jsonObject1.getString("downvote"));
                        cmpDhostel.setText(jsonObject1.getString("hostel"));
                        cmpDstatus.setText(status);
                        cmpDdesc.setText(jsonObject1.getString("description"));
                        cmpDtype.setText(cmptype);
                        cmpDtechi.setText(auth);
                        display.setImageBitmap(decodedByte);

                        final String userid = intent.getStringExtra("userid");
                        switch (userid){
                            case "0":
                                if(!cmptype.matches("Personal")){
                                    Resolved.setVisibility(View.GONE);
                                }
                                else{
                                    upvote.setVisibility(View.GONE);
                                    downvote.setVisibility(View.GONE);
                                }
                                redundant.setVisibility(View.GONE);
                                Notify.setVisibility(View.GONE);
                                break;
                            case "2":
                                upvote.setVisibility(View.GONE);
                                downvote.setVisibility(View.GONE);
                                redundant.setVisibility(View.GONE);
                                break;
                            case "1":
                                Notify.setVisibility(View.GONE);
                                upvote.setVisibility(View.GONE);
                                downvote.setVisibility(View.GONE);
                                Resolved.setVisibility(View.GONE);
                                break;
                        }
                        q.dismiss();
                    }
                    else {
                        q.dismiss();
                        Toast.makeText(complaintdetails.this, "unable to get complaint details", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    q.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                q.dismiss();
                Toast.makeText(getApplicationContext(),"volley error",Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        Resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cmpDstatus.getText().toString().matches("2")) {
                    Toast.makeText(complaintdetails.this,"This is complaint has already been resolved", Toast.LENGTH_SHORT).show();
                }
                else {
                    String url = Constants.IP+"/my_api/status/resolve?complaint_id=" + cmpDid.getText().toString();
                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int success = jsonObject.getInt("success");
                                if (success == 1) {
                                    Toast.makeText(complaintdetails.this, "complaint resolved", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(complaintdetails.this, "cannot resolve the complaint", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(complaintdetails.this, "volley error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue1.add(stringRequest1);
                }
            }
        });

        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sharedPreferences.contains(""+userid+"-"+cmpDid.getText().toString())){
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(""+userid+"-"+cmpDid.getText().toString(),"10");
                    editor.commit();
                    String url = Constants.IP+"/my_api/complaints/upvote?complaint_id="+cmpDid.getText().toString()+"&code=00";
                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int success = jsonObject.getInt("success");
                                if (success == 1) {
                                    upvote.setTextColor(Color.parseColor("#33aaff"));
                                    cmpDupvote.setText(jsonObject.getString("upvotes"));
                                    cmpDdownvote.setText(jsonObject.getString("downvotes"));
                                    Toast.makeText(complaintdetails.this, "upvoted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(complaintdetails.this, "cannot be upvoted", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(complaintdetails.this, "volley error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue1.add(stringRequest1);
                }
                else {
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    String url = "http://192.168.201.1:80/my_api/complaints/upvote?complaint_id="+cmpDid.getText().toString()+"&code="+sharedPreferences.getString(userid+"-"+cmpDid.getText().toString(),null);
                    editor.putString(""+userid+"-"+cmpDid.getText().toString(),"10");
                    editor.commit();
                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int success = jsonObject.getInt("success");
                                if (success == 1) {
                                    downvote.setTextColor(Color.parseColor("#000000"));
                                    upvote.setTextColor(Color.parseColor("#33aaff"));
                                    cmpDupvote.setText(jsonObject.getString("upvotes"));
                                    cmpDdownvote.setText(jsonObject.getString("downvotes"));
                                    Toast.makeText(complaintdetails.this, "upvoted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(complaintdetails.this, "cannot be upvoted", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(complaintdetails.this, "volley error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue1.add(stringRequest1);
                }
            }
        });

        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sharedPreferences.contains(""+userid+"-"+cmpDid.getText().toString())){
                    String url = Constants.IP+"/my_api/complaints/downvote?complaint_id="+cmpDid.getText().toString()+"&code=00";
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(""+userid+"-"+cmpDid.getText().toString(),"01");
                    editor.commit();
                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int success = jsonObject.getInt("success");
                                if (success == 1) {
                                    downvote.setTextColor(Color.parseColor("#33aaff"));
                                    cmpDupvote.setText(jsonObject.getString("upvotes"));
                                    cmpDdownvote.setText(jsonObject.getString("downvotes"));
                                    Toast.makeText(complaintdetails.this, "downvoted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(complaintdetails.this, "cannot be downvoted", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(complaintdetails.this, "volley error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue1.add(stringRequest1);
                }
                else {
                    String url = Constants.IP+"/my_api/complaints/downvote?complaint_id="+cmpDid.getText().toString()+"&code="+sharedPreferences.getString(userid+"-"+cmpDid.getText().toString(),null);
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(""+userid+"-"+cmpDid.getText().toString(),"01");
                    editor.commit();
                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int success = jsonObject.getInt("success");
                                if (success == 1) {
                                    downvote.setTextColor(Color.parseColor("#33aaff"));
                                    upvote.setTextColor(Color.parseColor("#000000"));
                                    cmpDupvote.setText(jsonObject.getString("upvotes"));
                                    cmpDdownvote.setText(jsonObject.getString("downvotes"));
                                    Toast.makeText(complaintdetails.this, "downvoted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(complaintdetails.this, "cannot be downvoted", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(complaintdetails.this, "volley error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue1.add(stringRequest1);
                }
            }
        });

        Notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),ComposeNotification.class);
                intent1.putExtra("cmpId",cmpDid.getText().toString());
                intent1.putExtra("complaintTitle",cmpDtitle.getText().toString());
                intent1.putExtra("complaintDesc",cmpDdesc.getText().toString());
                startActivity(intent1);
            }
        });

        redundant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cmpDstatus.getText().toString().matches("1")) {
                    Toast.makeText(complaintdetails.this,"This is already marked as redundant", Toast.LENGTH_SHORT).show();
                } else {
                    if (cmpDstatus.getText().toString().matches("2")) {
                        Toast.makeText(complaintdetails.this, "This has been resolved", Toast.LENGTH_SHORT).show();
                    } else {
                        String url = Constants.IP+"/my_api/status/redundant?complaint_id=" + cmpDid.getText().toString();
                        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int success = jsonObject.getInt("success");
                                    if (success == 1) {
                                        Toast.makeText(complaintdetails.this, "marked as redundant", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(complaintdetails.this, "cannot mark redundant", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(complaintdetails.this, "volley error", Toast.LENGTH_SHORT).show();
                            }
                        });
                        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                        requestQueue1.add(stringRequest1);
                    }
                }
            }
        });
    }
}
