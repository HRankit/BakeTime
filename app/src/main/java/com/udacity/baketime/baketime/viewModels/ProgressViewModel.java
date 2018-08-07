package com.udacity.baketime.baketime.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import static com.udacity.baketime.baketime.misc.MiscFunctions.PROGRESS_UNDEFINED;


public class ProgressViewModel extends AndroidViewModel {

    private static MutableLiveData<Integer> progressStatus;

    public ProgressViewModel(@NonNull final Application application) {
        super(application);
        progressStatus = new MutableLiveData<>();

    }

    public MutableLiveData<Integer> getStatus() {
        if (progressStatus == null) {
            progressStatus = new MutableLiveData<>();
            progressStatus.postValue(PROGRESS_UNDEFINED);
        }
        return progressStatus;
    }

    public static void setStatus(Integer status) {
        progressStatus.postValue(status);
    }
}