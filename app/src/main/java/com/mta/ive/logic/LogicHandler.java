package com.mta.ive.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.UsersHandler;

public class LogicHandler {
    private static DatabaseReference reference;

//    public static void saveUser(User user){
//
//    }

//    public static void createUserIfNotExist(String email, String userName){
//
//    }
//    public static User getUserByEmail(String email, String userName){
//
//        return null;
//    }

    public static void saveTask(Task task){
//        int id = task.getId();
//        reference = FirebaseDatabase.getInstance().getReference()
//                .child("task").child(String.valueOf(id));
//
//        reference.setValue(task);

        //add task to user
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//        FirebaseDatabase.getInstance().getReference()
//                .child("users")
//                .child(String.valueOf(email.hashCode()))
//                .child("tasks")
//                .push().setValue(task);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("tasks")
                .push();

//                .child(task.getId())
//                .setValue(task);
        String taskId = ref.getKey();
        task.setId(taskId);
//        task.setTaskID(taskId);
        ref.setValue(task);
    }

    public static void saveLocation(UserLocation userLocation){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("locations")
                .push();

        String locationId = ref.getKey();
        userLocation.setId(locationId);

        ref.setValue(userLocation);
    }

    public static void updateExistingTask(Task task){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("tasks")
                .child(task.getId())
                .setValue(task);

    }

    public static DatabaseReference getAllTasksDBReference(){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        return FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("tasks");
//        return FirebaseDatabase.getInstance().getReference().child("task");

    }

    public static DatabaseReference getAllLocationsDBReference(){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        return FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("locations");
    }

    public static DatabaseReference getTaskDBReferenceById(String taskId){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        return FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("tasks")
                .child(String.valueOf(taskId));

    }

    public static void deleteTaskById(String taskId){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(String.valueOf(email.hashCode()))
                .child("tasks")
                .child(String.valueOf(taskId)).removeValue();
    }

    public static void createUserIfNotExist(String email, String userName) {

        UsersHandler.getInstance().createUserIfNotExist(email, userName);
    }

}
