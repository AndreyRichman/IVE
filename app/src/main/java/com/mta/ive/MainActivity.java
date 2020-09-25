package com.mta.ive;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mta.ive.logic.LogicHandler;
import com.mta.ive.logic.users.User;
//import com.mta.ive.pages.delete.AllTasksActivity;
import com.mta.ive.pages.home.HomeActivity;


public class MainActivity extends AppCompatActivity {


    Class<HomeActivity> homeAcivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        String email = getIntent().getStringExtra("email");
        String userName = getIntent().getStringExtra("userName");

        homeAcivity = HomeActivity.class;
        LogicHandler.createUserIfNotExist(email, userName);
        loadUserFromDB();
    }

    private void loadUserFromDB() {
        String userEmail = LogicHandler.getCurrentUserEmail();
        DatabaseReference userReference = LogicHandler.getCurrentUserDBReferenceById(userEmail);

        userReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                LogicHandler.setCurrentUser(currentUser);
                LogicHandler.loadUserLocationsAndTasksMaps();

                Intent homePage = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homePage);//, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    public void openSignUpInPage(View btn) {
//        Intent loginPage = new Intent(this, SignUpInActivity.class);
//        startActivity(loginPage);
//    }
//
//    public void openLoginPage(View btn) {
//        Intent loginPage = new Intent(this, LoginActivity.class);
//        loginPage.putExtra("PAGE_NAME", "LOGIN PAGE");
//        startActivity(loginPage);
//    }
//
//    public void openSignUpPage(View btn) {
//        Intent signupPage = new Intent(this, SignUpActivity.class);
//        signupPage.putExtra("PAGE_NAME", "SIGNUP PAGE");
//        startActivity(signupPage);
//    }
//
//    public void openLobbyPage(View btn) {
//        Intent lobbyPage = new Intent(this, LobbyActivity.class);
//        lobbyPage.putExtra("PAGE_NAME", "LOBBY PAGE");
//        startActivity(lobbyPage);
//    }
//
//    public void openNewTask(View btn) {
//        Intent newTaskPage = new Intent(this, NewTaskActivity.class);
//        newTaskPage.putExtra("PAGE_NAME", "NEW TASK PAGE");
//        startActivity(newTaskPage);
//    }
//
//    public void openAllTasks(View btn) {
//        Intent allTasksPage = new Intent(this, AllTasksActivity.class);
//        allTasksPage.putExtra("PAGE_NAME", "ALL TASKS PAGE");
//        startActivity(allTasksPage);
//    }
//
//    public void goMainPage(View btn){
//        Intent homePage = new Intent(this, HomeActivity.class);
//        startActivity(homePage);
//    }

}