package com.example.proximitysensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements
        SensorEventListener {
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private TextView tvStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.tvStatus);
        // Get the SensorManager service
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Get the proximity sensor (default)
        if (sensorManager != null) {
            proximitySensor =
                    sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        // Check if the device has a proximity sensor
        if (proximitySensor == null) {
            tvStatus.setText("This device has NO proximity sensor.");
            Toast.makeText(this, "No proximity sensor found",
                    Toast.LENGTH_LONG).show();
        } else {
            tvStatus.setText("Proximity sensor ready.\nMove your hand near the top of the phone.");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Register the listener when app is in foreground
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister to save battery when app is in background
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        // When proximity sensor value changes
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            // Maximum range value means "far" (no object near)
            float maxRange = proximitySensor.getMaximumRange();
            if (distance < maxRange) {
                // Near
                tvStatus.setText("Object is NEAR\nDistance: " + distance + " cm");

                getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.
                        holo_red_dark));
            } else {
                // Far
                tvStatus.setText("Object is FAR\nDistance: " + distance + " cm");

                getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.
                        holo_green_dark));
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this app, but required by the interface
    }
}