package com.mta.ive.pages.ui.location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class LocationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LocationViewModel() {
        //TODO: DELETE ME!
        mText = new MutableLiveData<>();
        mText.setValue("Current Location!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}