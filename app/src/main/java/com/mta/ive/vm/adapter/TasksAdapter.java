package com.mta.ive.vm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mta.ive.R;
import com.mta.ive.logic.task.Task;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

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
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new TasksViewHolder(LayoutInflater.from( context)
//                .inflate(R.layout.task_item, parent, false));
        return new TasksViewHolder(LayoutInflater.from( parent.getContext())
                .inflate(R.layout.task_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder tasksHolder, int position) {
        tasksHolder.taskTitle.setText(allTasks.get(position).getTitle());
        tasksHolder.taskDescription.setText(allTasks.get(position).getDescription());
        tasksHolder.taskDuration.setText(allTasks.get(position).getDuration());
    }

    @Override
    public int getItemCount() {
        return this.allTasks.size();
    }

    class TasksViewHolder extends  RecyclerView.ViewHolder{

        TextView taskTitle, taskDescription, taskDuration;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle = (TextView) itemView.findViewById(R.id.taskTitle);
            taskDescription = (TextView) itemView.findViewById(R.id.taskDescription);
            taskDuration = (TextView) itemView.findViewById(R.id.taskDuration);
        }
    }
}
