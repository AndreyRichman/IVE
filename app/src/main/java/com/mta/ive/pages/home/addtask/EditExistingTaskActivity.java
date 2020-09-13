package com.mta.ive.pages.home.addtask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.vm.adapter.multiselect.Item;
import com.mta.ive.vm.adapter.multiselect.MultiSelectionSpinner;

import java.util.ArrayList;

public class EditExistingTaskActivity extends AppCompatActivity {

    EditText taskName, taskDescription, taskDuration;

    String taskId;

    DatabaseReference databaseReference;

    MultiSelectionSpinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        taskName = findViewById(R.id.task_name);
        taskDescription = findViewById(R.id.description);
        taskDuration = findViewById(R.id.duration);


        Bundle bundle = getIntent().getExtras();
        this.taskId = bundle.getString("taskId");


        Button saveBtn = findViewById(R.id.save_button);
        Button deleteBtn = findViewById(R.id.delete_button);
        readTaskFromDB(taskId);


        setNavigationButtons();

        updateLocations();


        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View btn){

                updateTaskByFields(taskId);
                finish();
                Toast.makeText(btn.getRootView().getContext(),"Task saved", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View btn){
                deleteTaskById(taskId);
                finish();
                Toast.makeText(btn.getRootView().getContext(),"Task deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setNavigationButtons() {
        findViewById(R.id.navigation_location).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "1");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        findViewById(R.id.navigation_home).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "2");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        findViewById(R.id.navigation_add).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "3");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        findViewById(R.id.navigation_user).setOnClickListener( t -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("selection", "4");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }

    private void readTaskFromDB(String taskId){
        databaseReference = LogicHandler.getTaskDBReferenceById(taskId);
//        databaseReference = FirebaseDatabase.getInstance().getReference()
//                .child("task").child(String.valueOf(taskId));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    Task task = snapshot.getValue(Task.class);

                    taskName.setText(task.getName());
                    taskDuration.setText(task.getDuration());
                    taskDescription.setText(task.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateTaskByFields(String taskId){


        databaseReference = LogicHandler.getTaskDBReferenceById(taskId);
//        databaseReference = FirebaseDatabase.getInstance().getReference()
//                .child("task").child(String.valueOf(taskId));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Task task = snapshot.getValue(Task.class);

                task.setName(taskName.getText().toString());
                task.setDescription(taskDescription.getText().toString());
                task.setDuration(taskDuration.getText().toString());

                LogicHandler.updateExistingTask(task);// saveTask(task);
//                databaseReference.setValue(task);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteTaskById(String idToDelete){
        LogicHandler.deleteTaskById(idToDelete);
//        FirebaseDatabase.getInstance().getReference()
//                .child("task").child(String.valueOf(taskId)).removeValue();
    }

    private void updateLocations() {
        DatabaseReference reference = LogicHandler.getAllLocationsDBReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Item> items = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()){
                    UserLocation location = data.getValue(UserLocation.class);

                    Item spinnerItem = new Item(location.getName(), location.getId(), location);
                    items.add(spinnerItem);
                }
                mySpinner = (MultiSelectionSpinner) findViewById(R.id.spinner_locations);
                mySpinner.setItems(items);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}