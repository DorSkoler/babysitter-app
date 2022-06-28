package com.example.myapplication.ui.explore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class ExploreViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private FirebaseAuth mAuth;

    public ExploreViewModel() {
        mText = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser().getEmail() != null)
            mText.setValue(mAuth.getCurrentUser().getEmail());
        else
            mText.setValue("null");
    }

    public LiveData<String> getText() {
        return mText;
    }
}