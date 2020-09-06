package com.mta.ive.pages.home.addtask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.R;
import com.mta.ive.logic.task.Task;

public class EditExistingTaskActivity extends AppCompatActivity {

    EditText taskName, taskDescription, taskDuration;

    int taskId;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_existing_task);

        taskName = findViewById(R.id.task_name);
        taskDescription = findViewById(R.id.description);
        taskDuration = findViewById(R.id.duration);


        Bundle bundle = getIntent().getExtras();
        this.taskId = bundle.getInt("taskId");


        Button saveBtn = findViewById(R.id.save_button);
        Button cancelBtn = findViewById(R.id.cancel_button);
        Button deleteBtn = findViewById(R.id.delete_button);
        readTaskFromDB(taskId);

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View btn){

                updateTaskByFields(taskId);
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View btn){
                deleteTaskById(taskId);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View btn){
                finish();
            }
        });
    }

    private void readTaskFromDB(int taskId){
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("task").child(String.valueOf(taskId));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Task task = snapshot.getValue(Task.class);

                taskName.setText(task.getName());
                taskDuration.setText(task.getDuration());
                taskDescription.setText(task.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateTaskByFields(int taskId){
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("task").child(String.valueOf(taskId));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Task task = snapshot.getValue(Task.class);

                task.setName(taskName.getText().toString());
                task.setDescription(taskDescription.getText().toString());
                task.setDuration(taskDuration.getText().toString());

                databaseReference.setValue(task);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteTaskById(int idToDelete){
        FirebaseDatabase.getInstance().getReference()
                .child("task").child(String.valueOf(taskId)).removeValue();
    }
}