package com.mta.ive.pages.home.tasksbylocation;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.R;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.User;
import com.mta.ive.pages.home.HomeActivity;
import com.mta.ive.vm.adapter.TasksAdapter;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TasksByLocationFragment extends Fragment {

    private TextView mainTitle, subTitle, bottomText;
    DatabaseReference reference;
    RecyclerView tasksRecList;
    ArrayList<Task> tasksList;
    TasksAdapter tasksAdapter;
    ViewGroup root;

    View view;

    public void setTasksAdapter(TasksAdapter tasksAdapter) {
        this.tasksAdapter = tasksAdapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        tasksList = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_tasks_by_location, container, false);
        tasksRecList = view.findViewById(R.id.tasksRecycleList);
        tasksRecList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        tasksRecList.setAdapter(new TasksAdapter(view.getContext(), tasksList));

        updateAllUserFields();

        updateUserTitle(view);


//        reference = FirebaseDatabase.getInstance().getReference().child("task");

//        reference = LogicHandler.getAllTasksDBReference();
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                tasksList = new ArrayList<>();
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Task task = dataSnapshot1.getValue(Task.class);
//                    tasksList.add(task);
//                }
//
//                tasksAdapter = new TasksAdapter(view.getContext(), tasksList); //TODO: originally: MainActivity.this
//                tasksRecList.setAdapter(tasksAdapter);
//                tasksAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(),"Error pulling data", Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateAllUserFields() {
        User user = LogicHandler.getCurrentUser();

        if (user != null) {
            updateAllUserFieldsByUser(user);
        }
        else {
            loadUserFromDBAndUpdateUI();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateAllUserFieldsByUser(User user) {
        tasksAdapter = new TasksAdapter(view.getContext(), user.getArrayOfTasks()); //TODO: originally: MainActivity.this
        tasksRecList.setAdapter(tasksAdapter);
        tasksAdapter.notifyDataSetChanged();

    }
    private void loadUserFromDBAndUpdateUI() {
        String userEmail = LogicHandler.getCurrentUserEmail();
        DatabaseReference userReference = LogicHandler.getCurrentUserDBReferenceById(userEmail);

        userReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                LogicHandler.setCurrentUser(currentUser);
                updateAllUserFieldsByUser(currentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateUserTitle(View view) {
        String username = ((HomeActivity)getActivity()).getUserName();

        TextView title = view.findViewById(R.id.tasksListMainTitle);
        title.setText("Hello "+ username + "! \n Here are your tasks at Work");
    }
}