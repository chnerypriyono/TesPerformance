package com.example.tesperformance;

import android.app.Application;
import android.util.Log;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //sleep();
    }

    public void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
