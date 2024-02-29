package com.example.cornerfinder.ui.editpreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditPreferencesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EditPreferencesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Preferencias");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
