package com.example.akramkhan.complaint_trial;

import android.app.Application;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;

/**
 * Created by Akram Khan on 29-03-2016.
 */
public class MyApp2 extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
    }
}
