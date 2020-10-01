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

    public void setAllTasks(List<Task> allTasks) {
        this.allTasks = allTasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new TaskViewHolder(LayoutInflater.from( parent.getContext())
                .inflate(R.layout.task_item2, parent, false));
    }

    // Enter UI fieds values here:
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder tasksHolder, int position) {
        tasksHolder.taskTitle.setText(allTasks.get(position).getName());
        tasksHolder.taskDescription.setText(allTasks.get(position).getDescription());
        String duration = allTasks.get(position).getDuration() + " Minutes";

        tasksHolder.taskDuration.setText(duration);

        hideLineIfNeeded(tasksHolder, position);

        tasksHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String taskId = allTasks.get(position).getId();

                Bundle bundle = new Bundle();
                bundle.putString("taskId", taskId);
                ((HomeActivity)context).openEditTaskPage(bundle);

            }
        });
    }

    private void hideLineIfNeeded(@NonNull TaskViewHolder locationViewHolder, int position) {
        if (position == allTasks.size() - 1){
            locationViewHolder.hideTaskLine();
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (this.allTasks != null){
            count = this.allTasks.size();
        }

        return count;
    }
}
