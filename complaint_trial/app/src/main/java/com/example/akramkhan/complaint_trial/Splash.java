package com.example.akramkhan.complaint_trial;

/**
 * Created by Vineeth Patibandla on 3/30/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends Activity {
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String logged = sharedPreferences.getString("logged","false");
        String curruser=sharedPreferences.getString("curruser",null);
        String currpass=sharedPreferences.getString("currpass",null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if(logged!=null) {
            if (logged.matches("true")) {
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("curruser",curruser);
                editor.putString("currpass", currpass);
                editor.commit();
                String url = Constants.IP+"/my_api/home/login?user_id="+ curruser + "&password=" + currpass;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject j = new JSONObject(s);
                            int i = j.getInt("success");
                            if (i == 1) {
                                JSONObject k = j.getJSONObject("message");
                                String usertype = k.getString("user_type");
                                final SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("logged","true");
                                editor.commit();
                                switch (usertype){
                                    case "0":
                                        run(MainActivity.class);
                                        break;
                                    case "1":
                                        run(WardenMain.class);
                                        break;
                                    case "2":
                                        run(WardenMain.class);
                                        break;
                                    case "3":
                                        run(WardenMain.class);
                                        break;
                                    default:
                                        run(Login.class);
                                        break;
                                }
                            } else {
                                String message = j.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Volley error11", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
            else{
                run(Login.class);
            }
        }

    }

    public void run(final Class x){

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this,x);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}


