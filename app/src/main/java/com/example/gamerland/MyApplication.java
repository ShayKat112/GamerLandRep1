package com.example.gamerland;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class MyApplication extends Application {

    private static NetworkChangeReceiver networkReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        // רישום קבוע של ה־BroadcastReceiver
        networkReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }
}
