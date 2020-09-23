package com.mta.ive.pages.settings;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mta.ive.R;

import java.time.LocalTime;
import java.util.Calendar;

public class AppSettings extends AppCompatActivity {

    TextView startTime, endTime;
    LocalTime userStartTime, userEndTime;
    Button saveSettingsButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_app_settings);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        saveSettingsButton = findViewById(R.id.save_app_settings_button);

        startTime.setOnClickListener(click -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker = new TimePickerDialog(AppSettings.this, new TimePickerDialog.OnTimeSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    userStartTime = LocalTime.MIN.plusHours(selectedHour).plusMinutes(selectedMinute);
                    startTime.setText("Day starts at " +userStartTime.toString());
                }
            }, hour, minute, true);//Yes 24 hour time
//            mTimePicker.setTitle("Select beginning of day time");
            mTimePicker.show();
        });

        endTime.setOnClickListener(click -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker = new TimePickerDialog(AppSettings.this, new TimePickerDialog.OnTimeSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    userEndTime = LocalTime.MIN.plusHours(selectedHour).plusMinutes(selectedMinute);
                    endTime.setText("Day ends at " + userEndTime.toString());
                }
            }, hour, minute, true);//Yes 24 hour time
//            mTimePicker.setTitle("Select end of day time");
            mTimePicker.show();
        });

        saveSettingsButton.setOnClickListener(click -> {
            if (userStartTime.isAfter(userEndTime)){
                String msg = "Beginning of day must be before end of day";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
            else{
                String msg = "Settings Saved";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        setNavigationButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            String selection = data.getStringExtra("selection");

            Integer option = Integer.parseInt(selection);
            Intent returnIntent;
            switch (option) {
                case 1:
                    returnIntent = new Intent();
                    returnIntent.putExtra("selection", "1");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    break;
                case 2:
                    returnIntent = new Intent();
                    returnIntent.putExtra("selection", "2");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    break;
                case 3:
                    returnIntent = new Intent();
                    returnIntent.putExtra("selection", "3");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    break;
            }
        }
    }

    private void setNavigationButtons() {
        ((BottomNavigationView)findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_user);

        findViewById(R.id.navigation_location).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "1");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        findViewById(R.id.navigation_add).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "2");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        findViewById(R.id.navigation_user).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "3");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }
}