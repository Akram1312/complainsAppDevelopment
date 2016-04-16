package com.example.akramkhan.complaint_trial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText userid;
    private EditText password;
    private Button submit;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String curruser=sharedPreferences.getString("curruser",null);
        String currpass=sharedPreferences.getString("currpass",null);
        String logged = sharedPreferences.getString("logged",null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userid = (EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);

        userid.setText(curruser);
        password.setText(currpass);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("curruser",userid.getText().toString());
                editor.putString("currpass", password.getText().toString());
                editor.commit();
                String url = Constants.IP+"/my_api/home/login?user_id="+ userid.getText().toString() + "&password=" + password.getText().toString();
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
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("user_type",usertype);
                                        startActivity(intent);
                                        break;
                                    case "1":
                                        Intent intent1 = new Intent(getApplicationContext(), WardenMain.class);
                                        intent1.putExtra("user_type", usertype);
                                        startActivity(intent1);
                                        break;
                                    case "2":
                                        Intent intent2 = new Intent(getApplicationContext(), WardenMain.class);
                                        intent2.putExtra("user_type",usertype);
                                        startActivity(intent2);
                                        break;
                                    case "3":
                                        Intent intent3 = new Intent(getApplicationContext(), WardenMain.class);
                                        intent3.putExtra("user_type",usertype);
                                        startActivity(intent3);
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
        });
    }
}
