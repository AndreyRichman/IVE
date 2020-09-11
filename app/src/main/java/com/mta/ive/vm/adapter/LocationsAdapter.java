package com.mta.ive.vm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mta.ive.R;
import com.mta.ive.logic.location.UserLocation;
import com.mta.ive.logic.task.Task;
import com.mta.ive.pages.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    Context context;
    List<UserLocation> alluserLocations;

    public LocationsAdapter(Context context, List<UserLocation> tasks){
        this.context = context;
        this.alluserLocations = tasks;
    }

    public List<UserLocation> getAlluserLocations() {
        return alluserLocations;
    }

    public void setAlluserLocations(ArrayList<UserLocation> alluserLocations) {
        this.alluserLocations = alluserLocations;
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
        tasksHolder.taskTitle.setText(alluserLocations.get(position).getName());


        tasksHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String locationId = alluserLocations.get(position).getId();

                Bundle bundle = new Bundle();
                bundle.putString("locationId", locationId);
//                ((HomeActivity)context).openEditTaskPage(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.alluserLocations.size();
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