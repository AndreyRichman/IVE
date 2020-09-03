package com.mta.ive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mta.ive.pages.delete.AllTasksActivity;
import com.mta.ive.pages.home.HomeActivity;
import com.mta.ive.pages.delete.LobbyActivity;
import com.mta.ive.pages.login.LoginActivity;
import com.mta.ive.pages.task.NewTaskActivity;
import com.mta.ive.pages.login.SignUpActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void openLoginPage(View btn) {
        Intent loginPage = new Intent(this, LoginActivity.class);
        loginPage.putExtra("PAGE_NAME", "LOGIN PAGE");
        startActivity(loginPage);
    }

    public void openSignUpPage(View btn) {
        Intent signupPage = new Intent(this, SignUpActivity.class);
        signupPage.putExtra("PAGE_NAME", "SIGNUP PAGE");
        startActivity(signupPage);
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