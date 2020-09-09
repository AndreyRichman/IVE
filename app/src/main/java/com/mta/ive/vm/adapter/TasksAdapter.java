package com.mta.ive.vm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mta.ive.R;
import com.mta.ive.logic.task.Task;
import com.mta.ive.pages.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    Context context;
    List<Task> allTasks;

    public TasksAdapter(Context context, List<Task> tasks){
        this.context = context;
        this.allTasks = tasks;
    }

    public List<Task> getAllTasks() {
        return allTasks;
    }

    public void setAllTasks(ArrayList<Task> allTasks) {
        this.allTasks = allTasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new TaskViewHolder(LayoutInflater.from( parent.getContext())
                .inflate(R.layout.task_item, parent, false));
    }

    // Enter UI fieds values here:
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder tasksHolder, int position) {
        tasksHolder.taskTitle.setText(allTasks.get(position).getName());
        tasksHolder.taskDescription.setText(allTasks.get(position).getDescription());
        String duration = allTasks.get(position).getDuration() + " Minutes";

        tasksHolder.taskDuration.setText(duration);

        tasksHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                view.findViewById(R.id.navigation_add).callOnClick();

//                view.getRootView().findViewById(R.id.navigation_add).callOnClick();

                String taskName = allTasks.get(position).getName();
                String taskDescription = allTasks.get(position).getDescription();
                String taskDuration = allTasks.get(position).getDuration();
                String taskId = allTasks.get(position).getId();

                Bundle bundle = new Bundle();
                bundle.putString("taskId", taskId);
                ((HomeActivity)context).openEditTaskPage(bundle);
//                TextView v = view.getRootView().findViewById(R.id.task_name);
//                v.setText("Andreyyyy");


//                 setContentView(R.layout.activity_home);

//                view.getRootView().findViewById(R.layout)
//                Intent editTaskPage = new Intent(context, AddTaskFragment.class);

//                Fragment currentFragment = ((FragmentActivity) view.getContext()).getSupportFra
//                ((FragmentActivity)view.getContext()).getSupportFragmentManager()
//                        .beginTransaction()
//
////                        .hide(view.getRootView().getF)
////                        .show( new TasksByLocationFragment())
//                        .replace(R.id.contentFragment, new AddTaskFragment())
//                                .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.allTasks.size();
    }



//    class TaskViewHolder extends  RecyclerView.ViewHolder{
//
//        TextView taskTitle, taskDescription, taskDuration;
//
//        public TaskViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            taskTitle = (TextView) itemView.findViewById(R.id.taskTitle);
//            taskDescription = (TextView) itemView.findViewById(R.id.taskDescription);
//            taskDuration = (TextView) itemView.findViewById(R.id.taskDuration);
//        }
//    }
}
