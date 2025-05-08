package com.example.gamerland;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OpenScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);

        Thread t = new Thread() {
            public void run() {
                try {
                    // Wait for splash (optional, short time recommended)
                    sleep(1000); // 1 second (adjust as needed)

                    // Check if user is logged in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Intent i;

                    if (user != null) {
                        // User is logged in, go to home page
                        i = new Intent(OpenScreenActivity.this, HomeActivity.class);
                    } else {
                        // No user logged in, go to login/welcome page
                        i = new Intent(OpenScreenActivity.this, WelcomeActivity.class);
                    }

                    startActivity(i);
                    finish(); // Close splash screen

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        t.start(); // Start the splash thread
    }
}
