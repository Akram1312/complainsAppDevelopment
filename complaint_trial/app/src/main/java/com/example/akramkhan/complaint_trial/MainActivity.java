package com.example.akramkhan.complaint_trial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

public class MainActivity extends AppCompatActivity {
    public static int type;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private int[] imageResId = {
            R.drawable.ic_location_city_white_24dp,
            R.drawable.ic_school_white_24dp,
            R.drawable.ic_person_white_24dp,
            R.drawable.ic_record_voice_over_white_24dp
    };

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Intent intent=getIntent();
        type=intent.getIntExtra("sortType", 1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ComposeComplaint.class);
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
                final Intent intent = new Intent(getApplicationContext(),Changepassword.class);
                intent.putExtra("user_type","0");
                startActivity(intent);
                return true;
            case R.id.timesort:
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("sortType",1);
                startActivity(intent1);
                return true;
            case R.id.trending:
                Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.putExtra("sortType",2);
                startActivity(intent2);
                return true;
            case R.id.refresh:
                Intent intent3 = new Intent(getApplicationContext(),MainActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                                Toast.makeText(MainActivity.this,jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this,"volley error", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static complaintfragment newInstance(int sectionNumber,int t) {
            /*PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;*/
            complaintfragment fragment = new complaintfragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt("sortType",t);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1,type);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    Drawable image = ContextCompat.getDrawable(getApplicationContext(), imageResId[position]);
                    image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
                    SpannableString sb = new SpannableString(" "+"Hostel");
                    ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
                    sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb;
                case 1:
                    Drawable image1 = ContextCompat.getDrawable(getApplicationContext(), imageResId[position]);
                    image1.setBounds(0, 0, image1.getIntrinsicWidth(), image1.getIntrinsicHeight());
                    SpannableString sb1 = new SpannableString(" "+"Insti");
                    ImageSpan imageSpan1 = new ImageSpan(image1, ImageSpan.ALIGN_BOTTOM);
                    sb1.setSpan(imageSpan1, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb1;
                case 2:
                    Drawable image2 = ContextCompat.getDrawable(getApplicationContext(), imageResId[position]);
                    image2.setBounds(0, 0, image2.getIntrinsicWidth(), image2.getIntrinsicHeight());
                    SpannableString sb2 = new SpannableString(" "+"Personal");
                    ImageSpan imageSpan2 = new ImageSpan(image2, ImageSpan.ALIGN_BOTTOM);
                    sb2.setSpan(imageSpan2, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb2;
                case 3:
                    Drawable image3 = ContextCompat.getDrawable(getApplicationContext(), imageResId[position]);
                    image3.setBounds(0, 0, image3.getIntrinsicWidth(), image3.getIntrinsicHeight());
                    SpannableString sb3 = new SpannableString(" "+"Notices");
                    ImageSpan imageSpan3 = new ImageSpan(image3, ImageSpan.ALIGN_BOTTOM);
                    sb3.setSpan(imageSpan3, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb3;
            }
            return null;
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
                    }
                }).create().show();
    }
}