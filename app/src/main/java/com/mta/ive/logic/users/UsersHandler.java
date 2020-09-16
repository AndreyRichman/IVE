package com.mta.ive.logic.users;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersHandler {
    private static UsersHandler instance;
    private User currentUser;

    public static UsersHandler getInstance(){
        if (instance == null){
            instance = new UsersHandler();
        }
        return instance;
    }

    public boolean userExist(String email){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");




        return false;
    }

    public User getUser(String email){
        return null;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void createUserIfNotExist(String email, String userName) {
        /*
        * reference = FirebaseDatabase.getInstance().getReference()
                .child("task").child(String.valueOf(id));
                * */
//        FirebaseDatabase.getInstance().getReference()
//                .child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
//
        FirebaseDatabase.getInstance().getReference()
                .child("users").child(String.valueOf(email.hashCode())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    createUser(email, userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createUser(String email, String userName){
        User userToAdd = new User(userName, email);

        FirebaseDatabase.getInstance().getReference().child("users").child(String.valueOf(email.hashCode())).setValue(userToAdd);

    }
}
