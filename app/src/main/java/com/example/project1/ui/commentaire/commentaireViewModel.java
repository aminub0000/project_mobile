package com.example.project1.ui.commentaire;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class commentaireViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public commentaireViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("haa lcommentairate hhh");
    }

    public LiveData<String> getText() {
        return mText;
    }
}