package com.mta.ive.pages.home.addtask;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.location.LocationWithTasksWrapper;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.User;
import com.mta.ive.pages.home.HomeActivity;
import com.mta.ive.vm.adapter.multiselect.Item;
import com.mta.ive.vm.adapter.multiselect.MultiSelectionSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;


public class AddTaskFragment extends Fragment {

    Button saveBtn;
    Button deleteBtn;
    DatabaseReference databaseReference;
    TextView nameTextField, descriptionTextField, durationTextField;
    EditText  dateTextField;
    Spinner prioritySpinner;

    DatePickerDialog datePickerDialog;
    MultiSelectionSpinner locationMultiSpinner;
    View view;

    int day;
    int month;
    int year;
    String dateString;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_task, container, false);

        nameTextField = view.findViewById(R.id.task_name);
        durationTextField = view.findViewById(R.id.duration);
        descriptionTextField = view.findViewById(R.id.description);
        prioritySpinner = view.findViewById(R.id.urgency);
        locationMultiSpinner = (MultiSelectionSpinner) view.findViewById(R.id.spinner_locations);
        dateTextField = view.findViewById(R.id.editTextDate);
        dateTextField.setInputType(InputType.TYPE_NULL);



        saveBtn = view.findViewById(R.id.save_button);
        deleteBtn = view.findViewById(R.id.delete_button);
//        Toast.makeText(view.getContext(),"New Task Page", Toast.LENGTH_SHORT).show();

        validateLocationsExist();
        updateLocations();


        initDatePickerDialog();
        dateTextField.setOnClickListener(view -> {
            datePickerDialog.show();
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {



            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick (View btn){

                boolean mandatoryFieldsAreFilled = mandatoryFieldsAreFilled();

                if (mandatoryFieldsAreFilled) {


                    Task task = new Task();

                    task.setName(nameTextField.getText().toString());
                    task.setDescription(descriptionTextField.getText().toString());
                    task.setDuration(Integer.parseInt(durationTextField.getText().toString()));
                    task.setPriority(prioritySpinner.getSelectedItemPosition());

                    task.setLocations(locationMultiSpinner.getSelectedItems()
                            .stream().map(Item::getLocation).collect(Collectors.toList()));

                    task.setDeadLineDate(dateString);

                    boolean needToUpdateLocationIndex = needToUpdateLocationIndex(task);

                    String idOfLastSelectedLocation = "";
                    if (needToUpdateLocationIndex){
                        int lastSelectedIndex = LogicHandler.getLastSelectedIndex();
                        idOfLastSelectedLocation = LogicHandler.getSwichableLocationsWithAll()
                                .get(lastSelectedIndex).getLocation().getId();
                    }

                    LogicHandler.saveTask(task);

                    if (needToUpdateLocationIndex){
                        int newIndex = getNewIndexByLocationId(idOfLastSelectedLocation);
                        LogicHandler.updateLastSelectedLocationIndex(newIndex);
                    }



                    //TODO: deside if this part is really irrelevant
                    btn.getRootView().findViewById(R.id.navigation_location).callOnClick();
                    Toast.makeText(getContext(), "Task was added", Toast.LENGTH_SHORT).show();

                }
            }
        });


        return view;//inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    private int getNewIndexByLocationId(String idOfLastSelectedLocation) {
        int newIndex = 0;
        int index = 0;
        for (LocationWithTasksWrapper location: LogicHandler.getSwichableLocationsWithAll()){
            if (location.getLocation().getId().equals(idOfLastSelectedLocation)){
                newIndex = index;
                break;
            }
            index++;
        }
        return newIndex;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean needToUpdateLocationIndex(Task task) {
        boolean needToUpdateSelectedIndex = false;
        for (UserLocation location: task.getLocations()) {
            boolean locationIsEmpty = LogicHandler.getTasksOfCurrentUserInLocation(location).size() == 0;

            if (locationIsEmpty){
                needToUpdateSelectedIndex = true;
            }
        }

        return needToUpdateSelectedIndex;
    }

    private void validateLocationsExist() {
        int numberOfLocation = LogicHandler.getCurrentUser().getLocations().size();

        if (numberOfLocation == 0){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Bundle bundle = new Bundle();
                            ((HomeActivity)view.getContext()).openManageLocationsPage(bundle);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
//                            ((HomeActivity)view.getContext());
//                            view.findViewById(R.id.navigation_location).callOnClick();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Let's add  some locations first?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
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

        if (!taskName.matches("[a-zA-Z0-9]+")){
            isValid = false;
            errorMessage = "Name cannot contain special characters";
        }
        if (taskName.matches("[0-9]+")){
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateString = dayOfMonth + "/" + monthOfYear  + "/" + year;
                        dateTextField.setText(dateString);
                    }
                }, year, month, day);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLocations() {
        ArrayList<Item> items = new ArrayList<>();
        User user = LogicHandler.getCurrentUser();

        if (user.getArrayOfLocations().size() > 0) {
            user.getArrayOfLocations().forEach(location -> {
                Item spinnerItem = new Item(location.getName(), location.getId(), location);
                items.add(spinnerItem);
            });
        }
        locationMultiSpinner.setItems(items);


//        DatabaseReference reference = LogicHandler.getAllLocationsDBReference();
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                ArrayList<Item> items = new ArrayList<>();
//                for(DataSnapshot data: snapshot.getChildren()){
//                    UserLocation location = data.getValue(UserLocation.class);
//
//                    Item spinnerItem = new Item(location.getName(), location.getId(), location);
//                    items.add(spinnerItem);
//                }
//                locationMultiSpinner.setItems(items);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}