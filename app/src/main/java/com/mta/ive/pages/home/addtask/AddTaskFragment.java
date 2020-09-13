package com.mta.ive.pages.home.addtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;


public class AddTaskFragment extends Fragment {

    Button saveBtn;
    Button deleteBtn;
    DatabaseReference databaseReference;
    TextView nameTextField, descriptionTextField, duration;
    Spinner urgency;

    MultiSelectionSpinner mySpinner;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_task, container, false);
        nameTextField = view.findViewById(R.id.task_name);
        duration = view.findViewById(R.id.duration);
        descriptionTextField = view.findViewById(R.id.description);
        urgency = view.findViewById(R.id.urgency);

        saveBtn = view.findViewById(R.id.save_button);
        deleteBtn = view.findViewById(R.id.delete_button);
//        Toast.makeText(view.getContext(),"New Task Page", Toast.LENGTH_SHORT).show();


        updateLocations();
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View btn){

                Toast.makeText(getContext(), "Task was added", Toast.LENGTH_SHORT).show();

                Task task = new Task();
//                String taskId = task.getId();
//                databaseReference = FirebaseDatabase.getInstance().getReference()
//                        .child("task").child(String.valueOf(taskId));

                task.setName(nameTextField.getText().toString());
                task.setDescription(descriptionTextField.getText().toString());
                task.setDuration(duration.getText().toString());

                LogicHandler.saveTask(task);

                btn.getRootView().findViewById(R.id.navigation_location).callOnClick();

//                databaseReference.setValue(task);

            }
        });


        return view;//inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    private void updateLocations() {
        DatabaseReference reference = LogicHandler.getAllLocationsDBReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Item> items = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()){
                    UserLocation location = data.getValue(UserLocation.class);

                    Item spinnerItem = new Item(location.getName(), location.getId());
                    items.add(spinnerItem);
                }
                mySpinner = (MultiSelectionSpinner) view.findViewById(R.id.spinner_locations);
                mySpinner.setItems(items);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}