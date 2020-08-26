package com.example.ive.pages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ive.MainActivity;
import com.example.ive.R;
import com.example.ive.logic.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AllTasksActivity extends AppCompatActivity {

    ListView tasksList;
    public Map<String, Task> tasks = new HashMap();

    private void populateDummyTasksMap() {
        tasks.put("1", new Task("contact asos about delivery", 3, 5, new Location("")));
        tasks.put("2", new Task("buy groceries from the supermarket", 2, 45, new Location("")));
        tasks.put("3", new Task("do homework in calculus", 1, 150, new Location("")));
        tasks.put("4", new Task("send CV to companies", 3, 60, new Location("")));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        populateDummyTasksMap();
        tasksList =  findViewById(R.id.allTasksListView);
        List<String> tasksTitles = tasks.values().stream().map(Task::getTitle).collect(Collectors.toCollection(ArrayList::new));

    }
}