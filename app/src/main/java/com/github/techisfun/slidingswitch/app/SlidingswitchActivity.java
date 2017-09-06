package com.github.techisfun.slidingswitch.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.techisfun.slidingswitch.SlideListener;
import com.github.techisfun.slidingswitch.SlidingSwitch;

/**
 *
 */
public class SlidingswitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlidingSwitch slidingSwitch = findViewById(R.id.sliding_switch);
        slidingSwitch.setSlideListener(new SlideListener() {
            @Override
            public void onSecondOptionSelected() {
                Toast.makeText(SlidingswitchActivity.this, "Second option selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFirstOptionSelected() {
                Toast.makeText(SlidingswitchActivity.this, "First option selected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
