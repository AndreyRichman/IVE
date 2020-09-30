package com.mta.ive.pages.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.mta.ive.R;
import com.mta.ive.logic.location.ActivityManageLocations;
import com.mta.ive.logic.task.Task;
import com.mta.ive.pages.home.addtask.EditExistingTaskActivity;
import com.mta.ive.pages.home.home.AddLocationFragment;
import com.mta.ive.pages.home.tasksbylocation.StatisticsActivity;
import com.mta.ive.pages.settings.AppSettings;
import com.mta.ive.pages.settings.UserSettings;
import com.mta.ive.vm.adapter.TasksAdapter;

import java.util.ArrayList;
//import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);



//        String email = getIntent().getStringExtra("email");
//        userName = getIntent().getStringExtra("userName");
//
//        LogicHandler.createUserIfNotExist(email, userName);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

//    public String getUserName(){
//        return userName;
//    }

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
////                            setContentView(R.layout.delete_activity_new_task_2);
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

//    public TasksAdapter getTasksAdapter(){
//        tasksList = new ArrayList<>();
//
//        tasksAdapter = new TasksAdapter(HomeActivity.this, tasksList); //TODO: originally: MainActivity.this
//
//        return tasksAdapter;
//    }
//    public void updateLocationFragment(){
//        tasksRecList = findViewById(R.id.tasksRecycleList);
//        tasksRecList.setLayoutManager(new LinearLayoutManager(this)); //TODO: originally: this
//        tasksList = new ArrayList<>();
////
////        //Get data from DB
//        reference = FirebaseDatabase.getInstance().getReference().child("task");
//
//        tasksAdapter = new TasksAdapter(HomeActivity.this, tasksList); //TODO: originally: MainActivity.this
//        tasksRecList.setAdapter(tasksAdapter);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    Task task = dataSnapshot1.getValue(Task.class);
//                    tasksList.add(task);
//                }
//
//                tasksAdapter = new TasksAdapter(HomeActivity.this, tasksList); //TODO: originally: MainActivity.this
//                tasksRecList.setAdapter(tasksAdapter);
//                tasksAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(HomeActivity.this,"Error pulling data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void openEditTaskPage(Bundle bundle){
//        findViewById(R.id.navigation_add).callOnClick();


//        Intent editTaskPage = new Intent(this, EditExistingTaskActivity.class);
//        editTaskPage.putExtras(bundle);
//        startActivity(editTaskPage);


        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent editTaskPage = new Intent(this, EditExistingTaskActivity.class);
        editTaskPage.putExtras(bundle);
        startActivityForResult(editTaskPage, LAUNCH_SECOND_ACTIVITY);
//        tasksAdapter.notifyDataSetChanged();
    }

    public void openAddLocationPage(Bundle bundle){
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent addLocationPage = new Intent(this, AddLocationFragment.class);
        addLocationPage.putExtras(bundle);
        startActivityForResult(addLocationPage, LAUNCH_SECOND_ACTIVITY);
    }

    public void openManageAppSettings(Bundle bundle){
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent modifyAppSettings = new Intent(this, AppSettings.class);
        modifyAppSettings.putExtras(bundle);
        startActivityForResult(modifyAppSettings, LAUNCH_SECOND_ACTIVITY);
    }

    public void openManagePersonalSettings(Bundle bundle){
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent modifyUserSettings = new Intent(this, UserSettings.class);
        modifyUserSettings.putExtras(bundle);
        startActivityForResult(modifyUserSettings, LAUNCH_SECOND_ACTIVITY);
    }

    public void openManageLocationsPage(Bundle bundle){
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent addLocationPage = new Intent(this, ActivityManageLocations.class);
        addLocationPage.putExtras(bundle);
        startActivityForResult(addLocationPage, LAUNCH_SECOND_ACTIVITY);
    }

    public void openStatisticsPage(Bundle bundle){
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent statisticsPage = new Intent(this, StatisticsActivity.class);
        statisticsPage.putExtras(bundle);
        startActivityForResult(statisticsPage, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            String selection = data.getStringExtra("selection");

            Integer option = Integer.parseInt(selection);
            switch (option) {
                case 1:
                    findViewById(R.id.navigation_location).callOnClick();
                    break;
                case 2:
                    findViewById(R.id.navigation_add).callOnClick();
                    break;
                case 3:
                    findViewById(R.id.navigation_user).callOnClick();
                    break;
            }
//            switchToTabAccordingToSelection(Integer.parseInt(selection));
        }
//        if (resultCode == Activity.RESULT_CANCELED) {
//            //Write your code if there's no result
//
//        }
    }//onActivityResult

    private void switchToTabAccordingToSelection(int selection) {
        switch (selection){
            case 1:
                findViewById(R.id.navigation_location).callOnClick();
                break;
            case 2:
                findViewById(R.id.navigation_add).callOnClick();
                break;
            case 3:
                findViewById(R.id.navigation_user).callOnClick();
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

//    public void goToTasksLocationPage(){
//        findViewById(R.id.navigation_location).callOnClick();
//    }
}