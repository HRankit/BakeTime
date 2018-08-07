package com.udacity.baketime.baketime.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.udacity.baketime.baketime.models.RecipeResult;
import com.udacity.baketime.baketime.network.GetDataService;
import com.udacity.baketime.baketime.network.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.udacity.baketime.baketime.misc.MiscFunctions.PROGRESS_FAILED;
import static com.udacity.baketime.baketime.misc.MiscFunctions.PROGRESS_LOADING;
import static com.udacity.baketime.baketime.misc.MiscFunctions.PROGRESS_SUCCESSFULL;


public class RecipeViewModel extends AndroidViewModel {

    private static final String TAG = "RecipeViewModel";
    private static MutableLiveData<List<RecipeResult>> recipeList;
    private static List<RecipeResult> result;


    public RecipeViewModel(@NonNull final Application application) {

        super(application);
        performRequest(application);

    }

    public static void performRequest(@NonNull final Application application) {
        GetDataService tmdbWebService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RecipeResult>> call = tmdbWebService.getRecipes();

        ProgressViewModel.setStatus(PROGRESS_LOADING);

        call.enqueue(new Callback<List<RecipeResult>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeResult>> call, @NonNull Response<List<RecipeResult>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Timber.e("Performed Request %s", TAG);
                    result = response.body();
                    recipeList.postValue(result);
                    ProgressViewModel.setStatus(PROGRESS_SUCCESSFULL);

                } else {
                    Timber.e("onResponse error %s", response.message());
                    ProgressViewModel.setStatus(PROGRESS_FAILED);

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipeResult>> call, @NonNull Throwable t) {
                ProgressViewModel.setStatus(PROGRESS_FAILED);
                Toast.makeText(application.getApplicationContext(), "Something went wrong...Please try later!" + t.toString(), Toast.LENGTH_SHORT).show();
                Timber.e("onResponse error %s", t.toString());
            }
        });
    }


    public MutableLiveData<List<RecipeResult>> getRecipiesResults() {
        if (recipeList == null) {
            recipeList = new MutableLiveData<>();
        }
        return recipeList;
    }
}
