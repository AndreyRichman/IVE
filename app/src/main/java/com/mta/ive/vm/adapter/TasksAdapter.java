package com.mta.ive.vm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mta.ive.R;
import com.mta.ive.logic.task.Task;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    Context context;
    ArrayList<Task> allTasks;

    public TasksAdapter(Context context, ArrayList<Task> tasks){
        this.context = context;
        this.allTasks = tasks;
    }

    public ArrayList<Task> getAllTasks() {
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
