package com.example.akramkhan.complaint_trial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Akram Khan on 27-03-2016.
 */
public class complaintfragment extends Fragment{
    private ListView listView;
    private List<complaint> listofcomp;
    private complaintslistadapter adapter;
    private int position;
    private int sortType;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_view,null);
        listView = (ListView) v.findViewById(R.id.complaintview);

        position= (int) getArguments().get("section_number");
        sortType = (int) getArguments().get("sortType");
        switch (position){
            case 1:
                listofcomp = new ArrayList<>();
                String url = Constants.IP+"/my_api/complaints/hostel";
                final ProgressDialog p = new ProgressDialog(getContext());
                p.setMessage("Loading data......");
                p.setCancelable(false);
                p.show();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                                adapter = new complaintslistadapter(getActivity(),listofcomp);
                                listView.setAdapter(adapter);
                                p.dismiss();
                            }
                            else {
                                p.dismiss();
                                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            p.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        p.dismiss();
                        Toast.makeText(getContext(), "volley error hostel", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
                break;
            case 2:
                listofcomp = new ArrayList<>();
                final ProgressDialog q = new ProgressDialog(getContext());
                q.setMessage("Loading data......");
                q.setCancelable(false);
                q.show();
                String url1 = Constants.IP+"/my_api/complaints/institute";
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
                                    listofcomp.add(new complaint(i, j.getString("complaint_id"), j.getString("title"), j.getString("time"), Integer.parseInt(j.getString("upvote").toString()), Integer.parseInt(j.getString("downvote").toString()), j.getString("complaint_status")));
                                }
                                if(sortType==1){
                                    Collections.sort(listofcomp,new TimeComparator());
                                }
                                else {
                                    Collections.sort(listofcomp, new VotesComparator());
                                }
                                adapter = new complaintslistadapter(getActivity(),listofcomp);
                                listView.setAdapter(adapter);
                                q.dismiss();
                            }
                            else {
                                q.dismiss();
                                Toast.makeText(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "volley error institute", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue1= Volley.newRequestQueue(getContext());
                requestQueue1.add(stringRequest1);
                break;
            case 3:
                listofcomp = new ArrayList<>();
                final ProgressDialog r = new ProgressDialog(getContext());
                r.setMessage("Loading data......");
                r.setCancelable(false);
                r.show();
                String url2 = Constants.IP+"/my_api/complaints/personal";
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
                                    listofcomp.add(new complaint(i,j.getString("complaint_id"),j.getString("title"),j.getString("time"),Integer.parseInt(j.getString("upvote").toString()),Integer.parseInt(j.getString("downvote").toString()),j.getString("complaint_status")));
                                }
                                if(sortType==1){
                                    Collections.sort(listofcomp,new TimeComparator());
                                }
                                else {
                                    Collections.sort(listofcomp, new VotesComparator());
                                }
                                adapter = new complaintslistadapter(getActivity(),listofcomp);
                                listView.setAdapter(adapter);
                                r.dismiss();
                            }
                            else {
                                r.dismiss();
                                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            r.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        r.dismiss();
                        Toast.makeText(getContext(), "volley error personal", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue2= Volley.newRequestQueue(getContext());
                requestQueue2.add(stringRequest2);
                break;
            case 4:
                listofcomp = new ArrayList<>();
                final ProgressDialog t = new ProgressDialog(getContext());
                t.setMessage("Loading data......");
                t.setCancelable(false);
                t.show();
                String url3 = Constants.IP+"/my_api/complaints/notifications";
                StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int success = jsonObject.getInt("success");
                            if(success==1){
                                JSONArray jsonArray = jsonObject.getJSONArray("notifications");
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
                                adapter = new complaintslistadapter(getActivity(),listofcomp);
                                listView.setAdapter(adapter);
                                t.dismiss();
                            }
                            else {
                                t.dismiss();
                                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            t.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        t.dismiss();
                        Toast.makeText(getContext(), "volley error notifications", Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue3= Volley.newRequestQueue(getContext());
                requestQueue3.add(stringRequest3);
                break;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),complaintdetails.class);
                intent.putExtra("complaintid",""+view.getTag());
                intent.putExtra("userid","0");
                startActivity(intent);
            }
        });

        return v;
    }
}
