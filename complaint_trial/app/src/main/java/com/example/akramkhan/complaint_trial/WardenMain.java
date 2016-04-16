package com.example.akramkhan.complaint_trial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WardenMain extends AppCompatActivity {
    private ListView listView;
    private List<complaint> listofcomp;
    String user_type;
    int sortType;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden_main);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Intent intent=getIntent();
        user_type = intent.getStringExtra("user_type");
        sortType = intent.getIntExtra("sortType", 1);
        listView = (ListView) findViewById(R.id.wcomplaintview);

        listofcomp = new ArrayList<>();

        switch (user_type){
            case "1":
                String url = Constants.IP+"/my_api/complaints/technician";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int success = jsonObject.getInt("success");
                            if(success==1){
                                JSONArray jsonArray = jsonObject.getJSONArray("complaints");
                                for(int i=0;i<jsonArray.length();i++){
                                    String compid=jsonArray.getJSONObject(i).getString("complaint_id");
                                    String comptitle = jsonArray.getJSONObject(i).getString("title");
                                    int upvotes = jsonArray.getJSONObject(i).getInt("upvote");
                                    int downvotes = jsonArray.getJSONObject(i).getInt("upvote");
                                    String time = jsonArray.getJSONObject(i).getString("time");
                                    listofcomp.add(new complaint(i,compid,comptitle,time,upvotes,downvotes,jsonArray.getJSONObject(i).getString("complaint_status")));
                                }
                                if(sortType==1){
                                    Collections.sort(listofcomp,new TimeComparator());
                                }
                                else {
                                    Collections.sort(listofcomp, new VotesComparator());
                                }
                                complaintslistadapter adapter;
                                adapter=new complaintslistadapter(getApplicationContext(),listofcomp);
                                listView.setAdapter(adapter);
                            }
                            else {
                                Toast.makeText(WardenMain.this,jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(WardenMain.this, "Volley error", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
                break;
            case "2":
                String url1 = Constants.IP+"/my_api/complaints/hostel";
                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int success = jsonObject.getInt("success");
                            if(success==1){
                                JSONArray jsonArray = jsonObject.getJSONArray("complaints");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject j = jsonArray.getJSONObject(i);
                                    listofcomp.add(new complaint(i,j.getString("complaint_id"),j.getString("title"),j.getString("time"),Integer.parseInt(j.getString("upvote").toString()),Integer.parseInt(j.getString("downvote").toString()),j.getString("complaint_status")));
                                }
                                if(sortType==1){
                                    Collections.sort(listofcomp,new TimeComparator());
                                }
                                else {
                                    Collections.sort(listofcomp, new VotesComparator());
                                }
                                complaintslistadapter adapter1;
                                adapter1=new complaintslistadapter(getApplicationContext(),listofcomp);
                                listView.setAdapter(adapter1);
                            }
                            else{
                                Toast.makeText(WardenMain.this,jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "volley error", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(this);
                requestQueue1.add(stringRequest1);
                break;
            case "3":
                String url2 = Constants.IP+"/my_api/complaints/institute";
                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int success = jsonObject.getInt("success");
                            if(success==1){
                                JSONArray jsonArray = jsonObject.getJSONArray("complaints");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject j = jsonArray.getJSONObject(i);
                                    listofcomp.add(new complaint(i,j.getString("complaint_id"),j.getString("title"),j.getString("time"), Integer.parseInt(j.getString("upvote").toString()),Integer.parseInt(j.getString("downvote").toString()),j.getString("complaint_status")));
                                }
                                if(sortType==1){
                                    Collections.sort(listofcomp,new TimeComparator());
                                }
                                else {
                                    Collections.sort(listofcomp, new VotesComparator());
                                }
                            }
                            else {
                                Toast.makeText(WardenMain.this,jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "volley error", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue2= Volley.newRequestQueue(getApplicationContext());
                requestQueue2.add(stringRequest2);
                complaintslistadapter adapter2;
                adapter2=new complaintslistadapter(getApplicationContext(),listofcomp);
                listView.setAdapter(adapter2);
                break;
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), complaintdetails.class);
                intent.putExtra("complaintid", "" + view.getTag());
                intent.putExtra("userid", user_type);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.change_password:
                Intent intent = new Intent(getApplicationContext(),Changepassword.class);
                intent.putExtra("user_type",user_type);
                startActivity(intent);
                return true;
            case R.id.timesort:
                Intent intent1 = new Intent(getApplicationContext(),WardenMain.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("sortType",1);
                intent1.putExtra("user_type",user_type);
                startActivity(intent1);
                return true;
            case R.id.trending:
                Intent intent2 = new Intent(getApplicationContext(),WardenMain.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.putExtra("sortType",2);
                intent2.putExtra("user_type",user_type);
                startActivity(intent2);
                return true;
            case R.id.refresh:
                Intent intent3 = new Intent(getApplicationContext(),WardenMain.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent3.putExtra("user_type",user_type);
                startActivity(intent3);
                return true;
            case R.id.logout:
                String url = Constants.IP+"/my_api/home/logout";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int success = jsonObject.getInt("success");
                            if(success==1){
                                final SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("logged","false");
                                editor.commit();
                                Intent intent1 = new Intent(getApplicationContext(),Login.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                startActivity(intent1);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message").toString(),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(),"volley error", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to Logout?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        String url = Constants.IP+"/my_api/home/logout";
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    int success = jsonObject.getInt("success");
                                    if(success==1){
                                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged","false");
                                        editor.commit();
                                        Intent intent1 = new Intent(getApplicationContext(),Login.class);
                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                        startActivity(intent1);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message").toString(),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(getApplicationContext(),"volley error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);
                    }
                }).create().show();
    }
}
