package com.example.gamerland;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Fragment HomeFragment = new HomeFragment();
        Fragment ChatFragment = new ChatFragment();
        Fragment ContactFragment = new ContactFragment();

        setCurrentFragment(HomeFragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.ItHome) {
                setCurrentFragment(HomeFragment);
            } else if (item.getItemId() == R.id.ItChat) {
                setCurrentFragment(ChatFragment);
            } else if (item.getItemId() == R.id.ItContact) {
                setCurrentFragment(ContactFragment);
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}