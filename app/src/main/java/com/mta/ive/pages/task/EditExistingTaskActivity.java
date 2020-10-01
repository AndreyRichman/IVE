package com.mta.ive.pages.task;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.User;
import com.mta.ive.vm.adapter.multiselect.Item;
import com.mta.ive.vm.adapter.multiselect.MultiSelectionSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class EditExistingTaskActivity extends AppCompatActivity {

    EditText nameTextField, taskDescription, durationTextField;
    EditText dateTextField;
    CheckBox doneCheckBox;

    String taskId;

    Spinner prioritySpinner;

    DatePickerDialog datePickerDialog;
    MultiSelectionSpinner locationMultiSpinner;

    String dateString;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        nameTextField = findViewById(R.id.task_name);
        taskDescription = findViewById(R.id.description);
        durationTextField = findViewById(R.id.duration);

        prioritySpinner = findViewById(R.id.urgency);
        locationMultiSpinner = (MultiSelectionSpinner) findViewById(R.id.spinner_locations);

        dateTextField = findViewById(R.id.editTextDate);
        dateTextField.setInputType(InputType.TYPE_NULL);

        doneCheckBox = findViewById(R.id.doneCheckBox);

        Bundle bundle = getIntent().getExtras();
        this.taskId = bundle.getString("taskId");


        Button saveBtn = findViewById(R.id.save_button);
        Button deleteBtn = findViewById(R.id.delete_button);
        readTaskFromDB(taskId);


        setNavigationButtons();



        saveBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick (View btn){

                boolean mandatoryFieldsAreFilled = mandatoryFieldsAreFilled();
                if (mandatoryFieldsAreFilled) {
                    updateTaskByFields(taskId);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("selection", "1");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    Toast.makeText(btn.getRootView().getContext(), "Task saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick (View btn){
                deleteTaskById(taskId);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("selection", "1");
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
                Toast.makeText(btn.getRootView().getContext(),"Task deleted", Toast.LENGTH_SHORT).show();
            }
        });


        initDatePickerDialog();
        dateTextField.setOnClickListener(view -> {
            datePickerDialog.show();
        });

    }

    private boolean mandatoryFieldsAreFilled(){
        boolean nameIsEmpty = nameTextField.getText().toString().matches("");
        boolean locationNotSelected = locationMultiSpinner.getSelectedItems().size() == 0;
        boolean durationIsEmpty = durationTextField.getText().toString().matches("");

        boolean isValid = true;
        String errorMessage = "";
        String taskName = nameTextField.getText().toString();

        if (nameIsEmpty){
            isValid = false;
            errorMessage = "Name is missing";
        }

        if (!taskName.matches("[a-zA-Z0-9 ]+")){
            isValid = false;
            errorMessage = "Name cannot contain special characters";
        }
        if (taskName.matches("[0-9 ]+")){
            isValid = false;
            errorMessage = "Name must contain characters";
        }
        if (taskName.length() > 20){
            isValid = false;
            errorMessage = "Name exceeded the limit (20)";
        }
        if (locationNotSelected) {
            isValid = false;
            errorMessage = "Location is missing";
        }
        if (durationIsEmpty){
            isValid = false;
            errorMessage = "Duration is missing";
        }

        if(!isValid){
            notifyError(errorMessage);
        }
        return isValid;
    }

    private void notifyError(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(EditExistingTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateString = dayOfMonth + "/" + monthOfYear + "/" + year;
                        dateTextField.setText(dateString);
                    }
                }, year, month, day);
    }

    private void setNavigationButtons() {
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void readTaskFromDB(String taskId){
        Task task = LogicHandler.getCurrentUser().getTasks().get(taskId);


        nameTextField.setText(task.getName());
        durationTextField.setText(String.valueOf(task.getDuration()));
        taskDescription.setText(task.getDescription());
        dateTextField.setText(task.getDeadLineDate());
        prioritySpinner.setSelection(task.getPriority());

        List<UserLocation> taskLocations = task.getLocations();
        ArrayList<Item> locationItems = null;
        if (taskLocations != null) {
            locationItems = taskLocations.stream()
                    .map(userLocation ->
                            new Item(userLocation.getName(), userLocation.getId(), userLocation))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        updateLocations(locationItems);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTaskByFields(String taskId){

        if (doneCheckBox.isChecked()){
            LogicHandler.markDoneTask(taskId);

        }
        else {

            Task task = LogicHandler.getCurrentUser().getTasks().get(taskId);

            task.setName(nameTextField.getText().toString());
            task.setDescription(taskDescription.getText().toString());
            task.setDuration(Integer.parseInt(durationTextField.getText().toString()));


            task.setPriority(prioritySpinner.getSelectedItemPosition());

            task.setLocations(locationMultiSpinner.getSelectedItems()
                    .stream().map(Item::getLocation).collect(Collectors.toList()));

            task.setDeadLineDate(dateString);

            LogicHandler.updateExistingTask(task);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteTaskById(String idToDelete){
        LogicHandler.deleteTaskById(idToDelete);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLocations(ArrayList<Item> selectedItems) {
        ArrayList<Item> items = new ArrayList<>();
        User user = LogicHandler.getCurrentUser();

        user.getArrayOfLocations().forEach(location -> {
            Item spinnerItem = new Item(location.getName(), location.getId(), location);
            items.add(spinnerItem);
            locationMultiSpinner.setItems(items);
        });

        if (selectedItems != null && selectedItems.size() > 0) {
                    locationMultiSpinner.setSelection(selectedItems);
        }
    }
}