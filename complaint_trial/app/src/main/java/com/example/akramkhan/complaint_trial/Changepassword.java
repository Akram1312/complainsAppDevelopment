package com.example.akramkhan.complaint_trial;

import android.content.Intent;
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

public class Changepassword extends AppCompatActivity {
    private EditText oldpassword;
    private EditText newpassword;
    private EditText Confirmpassword;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        final Intent intent = getIntent();
        final String user_type =intent.getStringExtra("user_type");

        confirm = (Button) findViewById(R.id.confirm);
        oldpassword = (EditText) findViewById(R.id.opassword);
        newpassword = (EditText) findViewById(R.id.npassword);
        Confirmpassword = (EditText) findViewById(R.id.cpassword);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.IP+"/my_api/home/updatepass?old="+oldpassword.getText().toString()+"&new_1="+newpassword.getText().toString()+"&new_2="+Confirmpassword.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int success = jsonObject.getInt("success");
                            if(success==1){
                                Toast.makeText(Changepassword.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                if(user_type.matches("0")) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent1 = new Intent(getApplicationContext(),WardenMain.class);
                                    intent1.putExtra("user_type",user_type);
                                    startActivity(intent1);
                                }
                            }
                            else {
                                Toast.makeText(Changepassword.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Changepassword.this, "vollet error", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
    }
}
