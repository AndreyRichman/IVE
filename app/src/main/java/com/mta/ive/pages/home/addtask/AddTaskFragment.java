package com.mta.ive.pages.home.addtask;

import android.app.DatePickerDialog;
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

import java.time.LocalDate;
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


        updateLocations();


        initDatePickerDialog();
        dateTextField.setOnClickListener(view -> {
            datePickerDialog.show();
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {



            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick (View btn){

                Toast.makeText(getContext(), "Task was added", Toast.LENGTH_SHORT).show();

                Task task = new Task();

                task.setName(nameTextField.getText().toString());
                task.setDescription(descriptionTextField.getText().toString());
                task.setDuration(Integer.parseInt(durationTextField.getText().toString()));
                task.setPriority(prioritySpinner.getSelectedItemPosition());

                task.setLocations(locationMultiSpinner.getSelectedItems()
                        .stream().map(Item::getLocation).collect(Collectors.toList()));

                task.setDeadLineDate(dateString);

                LogicHandler.saveTask(task);

                btn.getRootView().findViewById(R.id.navigation_location).callOnClick();


            }
        });


        return view;//inflater.inflate(R.layout.fragment_add_task, container, false);
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
                locationMultiSpinner.setItems(items);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}