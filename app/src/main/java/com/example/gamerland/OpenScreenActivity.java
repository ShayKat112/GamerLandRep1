package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class OpenScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);

        Thread t=new Thread() {
            public void run() {

                try {
//sleep thread for 10 seconds, time in milliseconds
                    sleep(69);

//start new activity
                    Intent i=new Intent(OpenScreenActivity.this, WelcomeActivity.class);
                    startActivity(i);

//destroying Splash activity
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

//start thread
        t.start();
    }
    }