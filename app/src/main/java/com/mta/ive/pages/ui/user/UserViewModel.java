package com.mta.ive.pages.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserViewModel() {
        //TODO: DELETE ME!
        mText = new MutableLiveData<>();
        mText.setValue("This is User fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}