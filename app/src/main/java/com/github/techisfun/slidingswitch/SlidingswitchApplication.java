package com.github.techisfun.slidingswitch;

import android.app.Application;

/**
 * Base application class
 */
public abstract class SlidingswitchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

   /**
    *
    */
    abstract void initialize();
}