package com.mta.ive.pages.home.addtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mta.ive.R;


public class AddTaskFragment extends Fragment {

//    private AddTaskViewModel addTaskViewModel;
    LayoutInflater inflater;
    ViewGroup container;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;


//        View buttonAddNewTask = container.findViewById(R.id.addTaskButton);

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        Button addTaskButton = (Button) view.findViewById(R.id.addTaskButton);

//        addTaskButton.setOnClickListener(view1 -> {
//            Intent intent = new Intent(getActivity(),NewTaskActivity.class);
//            getActivity().startActivity(intent);
//
////            Intent intent = new Intent(container.getContext(), NewTaskActivity.class);
////            startActivity(intent);
//        });

        return inflater.inflate(R.layout.fragment_add, container, false);

//
//        addTaskViewModel =
//                ViewModelProviders.of(this).get(AddTaskViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_add, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        addTaskViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
    }

//    public void openNewTaskPage(View btn){
//        getActivity().startActivity(getContext(), NewTaskActivity.class);
//        Intent intent = new Intent(container.getContext(), NewTaskActivity.class);
//        startActivity(intent);
//        View view = inflater.inflate(R.layout.fragment_add, container, false);
//        Button addTaskButton = (Button) view.findViewById(R.id.addTaskButton);
//
//        addTaskButton.setOnClickListener(view1 -> {
//            Intent intent = new Intent(container.getContext(), NewTaskActivity.class);
//            startActivity(intent);
//        });
////        Intent intent = new Intent(getActivity(), NewTaskActivity.class);
////        startActivity(intent);
////        View view = inflater.inflate(R.layout.fragment_location, container, false);
////        Intent newTaskPage = new Intent(view.getContext(), NewTaskActivity.class);
////        newTaskPage.putExtra("PAGE_NAME", "NEW TASK PAGE");
////        startActivity(newTaskPage);
//
//    }
}