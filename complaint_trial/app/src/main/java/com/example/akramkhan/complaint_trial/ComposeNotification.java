package com.example.akramkhan.complaint_trial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ComposeNotification extends AppCompatActivity {
    private TextView NotifiTitle;
    private TextView NotifiId;
    private Button postNotifi;
    private TextView NotifiDesc;
    private EditText NotifiDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_notification);

        final Intent intent= getIntent();
        String id=intent.getStringExtra("cmpId");
        String Title=intent.getStringExtra("complaintTitle");
        String desc = intent.getStringExtra("complaintDesc");


        NotifiDetails = (EditText) findViewById(R.id.Notifidetails);
        NotifiId = (TextView) findViewById(R.id.NotifiId);
        postNotifi = (Button) findViewById(R.id.NotifiPost);
        NotifiDesc = (TextView) findViewById(R.id.NotifiDesc);
        NotifiTitle = (TextView) findViewById(R.id.NotifiTitle);

        NotifiId.setText(id);
        NotifiTitle.setText(Title);
        NotifiDesc.setText(desc);


        postNotifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.IP + "/my_api/complaints/cnotification?complaint_id=" + NotifiId.getText().toString() +
                        "&message=" + NotifiDetails.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("success") == 1) {
                                Toast.makeText(ComposeNotification.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ComposeNotification.this, "notification not placed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ComposeNotification.this, "Volley error composen", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
    }
}
