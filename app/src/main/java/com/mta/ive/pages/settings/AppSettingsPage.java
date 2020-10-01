package com.mta.ive.pages.settings;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.users.UserSettings;

import java.time.LocalTime;
import java.util.Calendar;

public class AppSettingsPage extends AppCompatActivity {

    TextView startTime, endTime;
    Button saveSettingsButton;
    Spinner schedulePolicy;

    LocalTime userStartTime, userEndTime;
    int selectedPolicyIndex = 0;

    boolean startTimeChanged = false;
    boolean endTimeChanged = false;
    boolean policyChanged = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_app_settings);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        saveSettingsButton = findViewById(R.id.save_app_settings_button);
        schedulePolicy = (Spinner) findViewById(R.id.SchedulingPolicy);

        updateWindowWithUserSettings();

        schedulePolicy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                policyChanged = true;
                selectedPolicyIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        startTime.setOnClickListener(click -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker = new TimePickerDialog(AppSettingsPage.this, new TimePickerDialog.OnTimeSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    userStartTime = LocalTime.MIN.plusHours(selectedHour).plusMinutes(selectedMinute);
                    startTime.setText(userStartTime.toString());
                    startTimeChanged = true;
                }
            }, hour, minute, true);
            mTimePicker.show();
        });

        endTime.setOnClickListener(click -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker = new TimePickerDialog(AppSettingsPage.this, new TimePickerDialog.OnTimeSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    userEndTime = LocalTime.MIN.plusHours(selectedHour).plusMinutes(selectedMinute);
                    endTime.setText(userEndTime.toString());
                    endTimeChanged = true;
                }
            }, hour, minute, true);
            mTimePicker.show();
        });

        saveSettingsButton.setOnClickListener(click -> {
            if ( false) {
                String msg = "Beginning of day must be before end of day";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
            else{

                if (startTimeChanged || endTimeChanged || policyChanged){
                    updateUserSetting();
                }
                String msg = "Settings Saved";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        setNavigationButtons();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateWindowWithUserSettings() {
        UserSettings settings = LogicHandler.getCurrentUser().getSettings();

        LocalTime settingsStartTime = LocalTime.MIN.plusHours(settings.getDayStartHour())
                .plusMinutes(settings.getDayStartMinutes());
        LocalTime settingsEndTime = LocalTime.MIN.plusHours(settings.getDayEndHour())
                .plusMinutes(settings.getDayEndMinutes());
        startTime.setText(settingsStartTime.toString());
        endTime.setText(settingsEndTime.toString());

        int policyIndexToSelect = settings.getPriorityType() == UserSettings.PRIORITY_TYPE.EFFICIENCY?
                0: 1;

        schedulePolicy.setSelection(policyIndexToSelect);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateUserSetting() {
        UserSettings settings = LogicHandler.getCurrentUser().getSettings();
        if(startTimeChanged){
            int newStartHour = userStartTime.getHour();
            int newStartMinutes = userStartTime.getMinute();
            settings.setDayStartHour(newStartHour);
            settings.setDayStartMinutes(newStartMinutes);
        }

        if(endTimeChanged){
            int newEndHour = userEndTime.getHour();
            int newEndMinutes = userEndTime.getMinute();
            settings.setDayEndHour(newEndHour);
            settings.setDayEndMinutes(newEndMinutes);
        }

        if (policyChanged){
             UserSettings.PRIORITY_TYPE priorityType = selectedPolicyIndex == 0?
                     UserSettings.PRIORITY_TYPE.EFFICIENCY : UserSettings.PRIORITY_TYPE.URGENCY;
             settings.setPriorityType(priorityType);
        }

        LogicHandler.updateExistingUserSettings(settings);
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