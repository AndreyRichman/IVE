package com.mta.ive.pages.home.addtask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class EditExistingTaskActivity extends AppCompatActivity {

    EditText taskName, taskDescription, taskDuration;

    EditText  dateTextField;

    String taskId;

    DatabaseReference databaseReference;
    Spinner prioritySpinner;

    DatePickerDialog datePickerDialog;
    MultiSelectionSpinner locationMultiSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        taskName = findViewById(R.id.task_name);
        taskDescription = findViewById(R.id.description);
        taskDuration = findViewById(R.id.duration);

        prioritySpinner = findViewById(R.id.urgency);
        locationMultiSpinner = (MultiSelectionSpinner) findViewById(R.id.spinner_locations);

        dateTextField = findViewById(R.id.editTextDate);
        dateTextField.setInputType(InputType.TYPE_NULL);


        Bundle bundle = getIntent().getExtras();
        this.taskId = bundle.getString("taskId");

//        updateLocations();

        Button saveBtn = findViewById(R.id.save_button);
        Button deleteBtn = findViewById(R.id.delete_button);
        readTaskFromDB(taskId);


        setNavigationButtons();



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


        dateTextField.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            datePickerDialog = new DatePickerDialog(EditExistingTaskActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            dateTextField.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, year, month, day);
            datePickerDialog.show();
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    Task task = snapshot.getValue(Task.class);

                    taskName.setText(task.getName());
                    taskDuration.setText(String.valueOf(task.getDuration()));
                    taskDescription.setText(task.getDescription());
                    dateTextField.setText(task.getDeadLineDate());
                    List<UserLocation> taskLocations = task.getLocations();
                    ArrayList<Item> locationItems = taskLocations.stream()
                            .map(userLocation ->
                            new Item(userLocation.getName(), userLocation.getId(), userLocation))
                            .collect(Collectors.toCollection(ArrayList::new));
                    updateLocations(locationItems);
//                    locationMultiSpinner.setSelection(locationItems);
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
                task.setDuration(Integer.parseInt(taskDuration.getText().toString()));

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

    private void updateLocations(ArrayList<Item> selectedItems) {
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
                locationMultiSpinner = (MultiSelectionSpinner) findViewById(R.id.spinner_locations);
                locationMultiSpinner.setItems(items);
                locationMultiSpinner.setSelection(selectedItems);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}