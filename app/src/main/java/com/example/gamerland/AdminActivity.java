package com.example.gamerland;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        AdminUsersControlFragment adminUsersControl = new AdminUsersControlFragment();
        AdminChatsControlFragment adminChatsControl = new AdminChatsControlFragment();
        setCurrentFragment(adminUsersControl);

        BottomNavigationView adminBottomNavigationView = findViewById(R.id.adminBottomNavView);
        adminBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.ItUsersControl) {
                setCurrentFragment(adminUsersControl);
            } else if (item.getItemId() == R.id.ItChatsControl) {
                setCurrentFragment(adminChatsControl);
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment2, fragment)
                .commit();
    }
}