package com.example.gamerland;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoInternetActivity extends AppCompatActivity {

    private TextView tvStatus;
    private Button btnRetry;
    private NetworkChangeReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        tvStatus = findViewById(R.id.tvInternetStatus);
        btnRetry = findViewById(R.id.btnRetry);

        if (isConnectedToInternet()) {
            goToOpenScreen();
            return;
        }

        tvStatus.setText("No Internet Connection");
        btnRetry.setVisibility(View.VISIBLE);
        btnRetry.setOnClickListener(v -> {
            if (isConnectedToInternet()) {
                goToOpenScreen();
            } else {
                tvStatus.setText("Still no Internet Connection. Try Again");
            }
        });

        networkReceiver = new NetworkChangeReceiver();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkReceiver != null) {
            try {
                unregisterReceiver(networkReceiver);
            } catch (IllegalArgumentException e) {
                // כדי למנוע קריסה במקרה שה־Receiver כבר שוחרר
                e.printStackTrace();
            }
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return nc != null && (
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            );
        }
        return false;
    }

    private void goToOpenScreen() {
        startActivity(new Intent(this, OpenScreenActivity.class));
        finish();
    }
}
