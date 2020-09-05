package com.mta.ive.pages.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.MainActivity;
import com.mta.ive.R;

import java.util.Random;

public class NewTaskActivity extends AppCompatActivity {

    Button btnCreateNew;
    Button btnCancel;
    DatabaseReference databaseReference;

    TextView nameTextField, descriptionTextField;
    Spinner prioritySelectField;
    Integer someNumber = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        nameTextField = findViewById(R.id.newTaskNameEditText);
        descriptionTextField = findViewById(R.id.newTaskDescriptionEditText);
        prioritySelectField = findViewById(R.id.newTaskPrioritySpinner);

        btnCreateNew = findViewById(R.id.createTaskButton);
        btnCancel = findViewById(R.id.cancelCreateButton);

        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("task").child(someNumber.toString());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("name").setValue(nameTextField.getText().toString());
                        snapshot.getRef().child("duration").setValue("1234");
                        snapshot.getRef().child("description").setValue(descriptionTextField.getText().toString());
                        snapshot.getRef().child("priority").setValue(prioritySelectField.getSelectedItem().toString());

                        Intent target = new Intent(NewTaskActivity.this, MainActivity.class);
                        startActivity(target);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}