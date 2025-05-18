package com.example.gamerland;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BatteryReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Fragment HomeFragment = new HomeFragment();
        Fragment SearchFragment = new SearchFragment();
        Fragment SettingsFragment = new SettingsFragment();

        String openChatId = getIntent().getStringExtra("openChatId");
        if (openChatId != null) {
            ChatFragment chatFragment = new ChatFragment();
            Bundle bundle = new Bundle();
            bundle.putString("chatId", openChatId);
            chatFragment.setArguments(bundle);

            setCurrentFragment(chatFragment);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
            bottomNavigationView.setSelectedItemId(R.id.ItSearch); // או R.id.ItChat אם יש לך כזה
            return;
        }


        setCurrentFragment(HomeFragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.ItHome) {
                setCurrentFragment(HomeFragment);
            } else if (item.getItemId() == R.id.ItSearch) {
                setCurrentFragment(SearchFragment);
            } else if (item.getItemId() == R.id.ItSettings) {
                setCurrentFragment(SettingsFragment);
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }

    protected void onStart() {
        super.onStart();
        batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver, new android.content.IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    protected void onStop() {
        super.onStop();
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }

}
