package com.example.project1.ui.reservation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class reservationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public reservationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Haaa reservationaaaat hhh");
    }

    public LiveData<String> getText() {
        return mText;
    }
}