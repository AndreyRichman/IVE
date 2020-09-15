package com.example.ive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ive.deviceManager.DeviceManager;
import com.example.ive.pages.AllTasksActivity;
import com.example.ive.pages.HomeActivity;
import com.example.ive.pages.LobbyActivity;
import com.example.ive.pages.LoginActivity;
import com.example.ive.pages.NewTaskActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DeviceManager d=new DeviceManager();
        d.getLocationFromDevice(this);



    }


    public void openLoginPage(View btn) {
        Intent loginPage = new Intent(this, LoginActivity.class);
        loginPage.putExtra("PAGE_NAME", "LOGIN PAGE");
        startActivity(loginPage);
    }

    public void openLobbyPage(View btn) {
        Intent lobbyPage = new Intent(this, LobbyActivity.class);
        lobbyPage.putExtra("PAGE_NAME", "LOBBY PAGE");
        startActivity(lobbyPage);
    }

    public void openNewTask(View btn) {
        Intent newTaskPage = new Intent(this, NewTaskActivity.class);
        newTaskPage.putExtra("PAGE_NAME", "NEW TASK PAGE");
        startActivity(newTaskPage);
    }

    public void openAllTasks(View btn) {
        Intent allTasksPage = new Intent(this, AllTasksActivity.class);
        allTasksPage.putExtra("PAGE_NAME", "ALL TASKS PAGE");
        startActivity(allTasksPage);
    }

    public void goMainPage(View btn){
        Intent homePage = new Intent(this, HomeActivity.class);
        startActivity(homePage);
    }

}