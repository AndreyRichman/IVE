package com.mta.ive.pages.home.tasksbylocation;

import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.LocationWithTasksWrapper;
import com.mta.ive.logic.task.Task;

public class StatisticsActivity extends AppCompatActivity {

    TextView locationNameTextView;
    int completed = 3, created = 7;

    String locationName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        locationNameTextView = findViewById(R.id.textViewLocation);

        loadValues();
        updateChart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadValues() {
        int indexOfSelectedLocation = LogicHandler.getLastSelectedIndex();
        LocationWithTasksWrapper location = LogicHandler.getSwichableLocationsWithAll()
                .get(indexOfSelectedLocation);

        locationName = location.getLocation().getName();
        locationNameTextView.setText("Productivity at " + locationName);

        String locationId = location.getLocation().getId();
        int leftToFinish = location.getTasks().size();

        created = (int) LogicHandler.getLocationIdToAllTasksCreated().get(locationId).stream()
                .filter(task -> task.getStatus() != Task.Status.ARCHIVED).count();
        completed = created - leftToFinish;

    }

    public void updateChart(){
        // Update the text in a center of the chart:

        TextView numberOfCals = findViewById(R.id.number_of_calories);
        String doneStr = String.valueOf(completed);
        String totalStr = String.valueOf(created);
        numberOfCals.setText(doneStr + " / " + totalStr);

        // Calculate the slice size and update the pie chart:
        ProgressBar pieChart = findViewById(R.id.stats_progressbar);
        double d = (double) completed / (double) created;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);
    }
}
