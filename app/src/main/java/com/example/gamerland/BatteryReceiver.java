package com.example.gamerland;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

public class BatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level >= 0 && scale > 0) {
            int batteryPct = (int) ((level / (float) scale) * 100);

            // ✅ הצג טוסט רק אם אחוז הסוללה קטן או שווה ל־10
            if (batteryPct <= 10) {
                Toast.makeText(context, "⚠️ Battery low: " + batteryPct + "%", Toast.LENGTH_LONG).show();
            }
        }
    }
}
