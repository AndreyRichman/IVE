package com.mta.ive.pages.home;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.R;
import com.mta.ive.logic.task.Task;
import com.mta.ive.vm.adapter.TasksAdapter;

import java.util.ArrayList;
//import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    //TODO deide if this is needed or cn be removed
//    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                    Fragment selectedFragment = null;
//
//                    switch (menuItem.getItemId()){
//                        case R.id.navigation_location:
//                            selectedFragment = new LocationFragment();
//                            TasksAdapter adapter = getTasksAdapter();
//                            ((LocationFragment)selectedFragment).setTasksAdapter(adapter);
//
//                            break;
//                        case R.id.navigation_home:
//                            selectedFragment = new HomeFragment();
//                            break;
//
//                        case R.id.navigation_add:
////                            selectedFragment = new AddTaskFragment();
//
//                            Intent newTaskPage = new Intent(HomeActivity.this, NewTaskActivity.class);
//                            startActivity(newTaskPage);
//
////                            setContentView(R.layout.activity_new_task);
//                            return false;
//
////                            break;
//
//                        case R.id.navigation_user:
//                            selectedFragment = new UserFragment();
//                            break;
//                    }
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
//                            selectedFragment).commit();
//
////                    updateLocationFragment();
//                    return true;
//                }
//            };

    DatabaseReference reference;
    RecyclerView tasksRecList;
    ArrayList<Task> tasksList;
    TasksAdapter tasksAdapter;

    public TasksAdapter getTasksAdapter(){
        tasksList = new ArrayList<>();

        tasksAdapter = new TasksAdapter(HomeActivity.this, tasksList); //TODO: originally: MainActivity.this

        return tasksAdapter;
    }
    private void updateLocationFragment(){
        tasksRecList = findViewById(R.id.tasksRecycleList);
        tasksRecList.setLayoutManager(new LinearLayoutManager(this)); //TODO: originally: this
        tasksList = new ArrayList<>();
//
//        //Get data from DB
        reference = FirebaseDatabase.getInstance().getReference().child("task");

        tasksAdapter = new TasksAdapter(HomeActivity.this, tasksList); //TODO: originally: MainActivity.this
        tasksRecList.setAdapter(tasksAdapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Task task = dataSnapshot1.getValue(Task.class);
                    tasksList.add(task);
                }

                tasksAdapter = new TasksAdapter(HomeActivity.this, tasksList); //TODO: originally: MainActivity.this
                tasksRecList.setAdapter(tasksAdapter);
                tasksAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this,"Error pulling data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}