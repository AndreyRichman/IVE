package com.mta.ive.vm.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mta.ive.R;

class TaskViewHolder extends  RecyclerView.ViewHolder{

    TextView taskTitle, taskDescription, taskDuration;
    ImageView taskLine;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        taskTitle = (TextView) itemView.findViewById(R.id.taskTitle);
        taskDescription = (TextView) itemView.findViewById(R.id.taskDescription);
        taskDuration = (TextView) itemView.findViewById(R.id.taskDuration);
        taskLine = (ImageView) itemView.findViewById(R.id.task_line);
    }

    public void hideTaskLine(){
        taskLine.setVisibility(View.INVISIBLE);
    }
}
