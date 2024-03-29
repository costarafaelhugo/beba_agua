package com.hugorafaelcosta.drinkwater;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnNotify;
    private EditText editMinutes;
    private TimePicker timePicker;

    private int hour;
    private int minute;
    private int interval;

    private boolean activated = false;

    private SharedPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotify = findViewById(R.id.btn_notify);
        editMinutes = findViewById(R.id.edit_txt_number_interval);
        timePicker = findViewById(R.id.time_picker);

        timePicker.setIs24HourView(true);

        preferences = getSharedPreferences("db", Context.MODE_PRIVATE);

        activated = preferences.getBoolean("activated", false);

        if(activated){
            btnNotify.setText(R.string.pause);
            int color = ContextCompat.getColor(this, android.R.color.black);
            btnNotify.setBackgroundTintList(ColorStateList.valueOf(color));
            activated = true;

            preferences.getInt("interval", 0);
            preferences.getInt("hour", timePicker.getCurrentHour());
            preferences.getInt("minute", timePicker.getCurrentMinute());

            editMinutes.setText(String.valueOf(interval));
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void notifyClick(View view) {
        String sInterval = editMinutes.getText().toString();

        if (sInterval.isEmpty()) {
            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        hour = timePicker.getCurrentHour();
        minute = timePicker.getChildCount();
        interval = Integer.parseInt(sInterval);

        if (!activated) {

            btnNotify.setText(R.string.pause);
            int color = ContextCompat.getColor(this, android.R.color.black);
            btnNotify.setBackgroundTintList(ColorStateList.valueOf(color));
            activated = true;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", true);
            editor.putInt("interval", interval);
            editor.putInt("hour", hour);
            editor.putInt("minute", minute);
            editor.apply();
        } else {
            btnNotify.setText(R.string.notfy);
            int color = ContextCompat.getColor(this, R.color.accent);
            btnNotify.setBackgroundTintList(ColorStateList.valueOf(color));
            activated = false;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", false);
            editor.remove("interval");
            editor.remove("hour");
            editor.remove("minute");
            editor.apply();
        }
        Log.d("Teste", "hora:" + hour + "minuto: " + minute + " intervalo: " + interval);
    }
}