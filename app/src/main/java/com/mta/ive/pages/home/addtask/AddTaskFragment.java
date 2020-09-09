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

import com.google.firebase.database.DatabaseReference;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.task.Task;


public class AddTaskFragment extends Fragment {

    Button saveBtn;
    Button deleteBtn;
    DatabaseReference databaseReference;
    TextView nameTextField, descriptionTextField, duration;
    Spinner urgency;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);
        nameTextField = view.findViewById(R.id.task_name);
        duration = view.findViewById(R.id.duration);
        descriptionTextField = view.findViewById(R.id.description);
        urgency = view.findViewById(R.id.urgency);

        saveBtn = view.findViewById(R.id.save_button);
        deleteBtn = view.findViewById(R.id.delete_button);
//        Toast.makeText(view.getContext(),"New Task Page", Toast.LENGTH_SHORT).show();

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


        return view;//inflater.inflate(R.layout.fragment_task_edit, container, false);
    }
}