package com.mta.ive.vm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mta.ive.R;
import com.mta.ive.logic.task.Task;
import com.mta.ive.pages.home.addtask.AddTaskFragment;

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

        tasksHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editTaskPage = new Intent(context, AddTaskFragment.class);
//                View root = view.getRootView();
//                Activity activity = (Activity) root.getContext();
//                activity.getFragmentManager().beginTransaction().replace(R.id.nav_view, (Fragment) new AddTaskFragment()).commit();
//                ((FragmentActivity) view.getContext()).getFragmentManager().beginTransaction()
//                        .replace(R.id.nav_view , new AddTaskFragment())
//                        .commit();
                ((FragmentActivity)view.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_view, new AddTaskFragment())
                                .commit();
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
