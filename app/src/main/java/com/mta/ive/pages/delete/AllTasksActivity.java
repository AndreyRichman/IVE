//package com.mta.ive.pages.delete;
//
//import android.os.Build;
//import android.os.Bundle;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.mta.ive.R;
//import com.mta.ive.logic.task.Task;
//import com.mta.ive.vm.adapter.TasksAdapter;
//
//import java.util.ArrayList;
//
//public class AllTasksActivity extends AppCompatActivity {
//
//    private TextView mainTitle, subTitle, bottomText;
//    DatabaseReference reference;
//    RecyclerView tasksRecList;
//    ArrayList<Task> tasksList;
//    TasksAdapter tasksAdapter;
//    ViewGroup root;
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_tasks_by_location);
//
//
//        tasksRecList = findViewById(R.id.tasksRecycleList);
//        tasksRecList.setLayoutManager(new LinearLayoutManager(this)); //TODO: originally: this
//        tasksList = new ArrayList<>();
////
////        //Get data from DB
//        reference = FirebaseDatabase.getInstance().getReference().child("task");
//
//        tasksAdapter = new TasksAdapter(AllTasksActivity.this, tasksList); //TODO: originally: MainActivity.this
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
////                tasksAdapter = new TasksAdapter(AllTasksActivity.this, tasksList); //TODO: originally: MainActivity.this
////                tasksRecList.setAdapter(tasksAdapter);
//                tasksAdapter.setAllTasks(tasksList);
//                tasksAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(AllTasksActivity.this,"Error pulling data", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//}