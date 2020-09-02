package com.mta.ive.pages.ui.addtask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddTaskViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddTaskViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Add New Task!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}