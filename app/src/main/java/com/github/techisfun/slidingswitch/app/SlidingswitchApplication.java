package com.github.techisfun.slidingswitch.app;

import android.app.Application;
import android.os.StrictMode;

import timber.log.Timber;

public class SlidingswitchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        // Detect all kind of problems and log it
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
    }

}