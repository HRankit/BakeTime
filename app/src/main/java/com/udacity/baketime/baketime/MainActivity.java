package com.udacity.baketime.baketime;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.baketime.baketime.IdlingResource.SimpleIdlingResource;
import com.udacity.baketime.baketime.adapter.RecipeListAdapter;
import com.udacity.baketime.baketime.misc.MyPreferences;
import com.udacity.baketime.baketime.models.RecipeResult;
import com.udacity.baketime.baketime.viewModels.ProgressViewModel;
import com.udacity.baketime.baketime.viewModels.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.udacity.baketime.baketime.misc.MiscFunctions.EXTRA_WIDGET_DATA;
import static com.udacity.baketime.baketime.misc.MiscFunctions.INTENT_FROM_WIDGET;
import static com.udacity.baketime.baketime.misc.MiscFunctions.JSON_DATA;
import static com.udacity.baketime.baketime.misc.MiscFunctions.MAIN_ACTIVITY;
import static com.udacity.baketime.baketime.misc.MiscFunctions.NUMBER_OF_STEPS;
import static com.udacity.baketime.baketime.misc.MiscFunctions.PROGRESS_FAILED;
import static com.udacity.baketime.baketime.misc.MiscFunctions.PROGRESS_LOADING;
import static com.udacity.baketime.baketime.misc.MiscFunctions.PROGRESS_SUCCESSFULL;
import static com.udacity.baketime.baketime.misc.MiscFunctions.PROGRESS_UNDEFINED;
import static com.udacity.baketime.baketime.misc.MiscFunctions.RECIPE_DETAILS;
import static com.udacity.baketime.baketime.misc.MiscFunctions.RECIPE_NAME;
import static com.udacity.baketime.baketime.misc.MiscFunctions.WHICH_ACTIVITY;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.ItemClickListener {

    private final String BUNDLE_RECYCLER_LAYOUT = "saved_recyclerview_state";
    private RecyclerView recipeRecyclerView;
    private List<RecipeResult> recipesResults;
    private Integer intentFromWidget = null;
    private ProgressBar progressBar2;
    private CardView errorCardView;
    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (getIntent().hasExtra(INTENT_FROM_WIDGET)) {
            Integer positions = getIntent().getExtras().getInt(INTENT_FROM_WIDGET);
            Toast.makeText(this, "Touched view " + positions, Toast.LENGTH_SHORT).show();
            intentFromWidget = positions;

            dataCameFromWidget(positions);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipeRecyclerView != null) {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recipeRecyclerView.getLayoutManager().onSaveInstanceState());
        }
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recipeRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree());

        initViews();

        initViewModel();

        if (getIntent().hasExtra(INTENT_FROM_WIDGET)) {
            Integer positions = getIntent().getExtras().getInt(INTENT_FROM_WIDGET);
//            Toast.makeText(this, "Touched view " + positions, Toast.LENGTH_SHORT).show();
            intentFromWidget = positions;

            dataCameFromWidget(positions);
        }

        getIdlingResource();
    }

    private void initViews() {
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.VISIBLE);
        progressBar2.setScaleY(3f);

        errorCardView = findViewById(R.id.errorCardView);
        errorCardView.setVisibility(View.GONE);
    }


    private void initViewModel() {
        initProgressViewModel();
        initRecipieViewModel();
    }

    private void initRecipieViewModel() {
        RecipeViewModel recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        recipeViewModel.getRecipiesResults().observe(this, new Observer<List<RecipeResult>>() {
            @Override
            public void onChanged(@Nullable List<RecipeResult> recipes) {
                Timber.d("Changed");
                recipesResults = recipes;
                if (intentFromWidget != null) {
                    dataCameFromWidget(intentFromWidget);
                }
                generateDataList(recipesResults);

            }
        });
    }

    private void initProgressViewModel() {
        ProgressViewModel progressViewModel = ViewModelProviders.of(this).get(ProgressViewModel.class);
        progressViewModel.getStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer recipes) {
                Timber.d(getResources().getString(R.string.progressViewModelPlaceholder), recipes);
                if (recipes != null) {
                    callback(recipes);
                } else {
                    callback(PROGRESS_UNDEFINED);
                }

            }
        });
    }

    private void callback(Integer progressStatus) {

        if (progressStatus.equals(PROGRESS_LOADING)) {
            showHideErrorPlaceholder(View.VISIBLE, View.GONE);
        } else if (progressStatus.equals(PROGRESS_FAILED)) {
            showHideErrorPlaceholder(View.GONE, View.VISIBLE);
        } else if (progressStatus.equals(PROGRESS_SUCCESSFULL)) {
            showHideErrorPlaceholder(View.GONE, View.GONE);
        } else if (progressStatus.equals(PROGRESS_UNDEFINED)) {
            showHideErrorPlaceholder(View.GONE, View.VISIBLE);
        }
    }

    private void showHideErrorPlaceholder(int gone, int visible) {
        progressBar2.setVisibility(gone);
        errorCardView.setVisibility(visible);
    }


    private void generateDataList(List<RecipeResult> recipeList) {

        recipeRecyclerView = findViewById(R.id.rv_recipesList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        recipeRecyclerView.setLayoutManager(mLayoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());


        RecipeListAdapter adapter = new RecipeListAdapter(recipeList, this);
        recipeRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        MyPreferences yourPrefrence = MyPreferences.getInstance(this);
        yourPrefrence.saveDataInt(WHICH_ACTIVITY, MAIN_ACTIVITY);
        yourPrefrence.saveDataInt(NUMBER_OF_STEPS, adapter.getItemCount());
        yourPrefrence.saveDataInt(NUMBER_OF_STEPS, adapter.getItemCount());


        List<String> strings = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            strings.add(recipeList.get(i).getName());
        }


        Gson gson = new Gson();
        String json = gson.toJson(strings);
        yourPrefrence.saveData(EXTRA_WIDGET_DATA, json);

        yourPrefrence.saveData(JSON_DATA, gson.toJson(recipesResults));

        progressBar2.setVisibility(View.GONE);

        Toast.makeText(this, getResources().getString(R.string.ToastMessage), Toast.LENGTH_LONG).show();
    }


    private void dataCameFromWidget(int position) {
        if (recipesResults != null && recipesResults.size() > 0) {
            RecipeResult rr = recipesResults.get(position);
            onItemClickListener(0, rr);
        } else {
            initViewModel();
        }
    }


    @Override
    public void onItemClickListener(int itemId, RecipeResult rr) {
        Gson gson = new Gson();
        Bundle extras = new Bundle();
        extras.putString(RECIPE_DETAILS, gson.toJson(rr));
        extras.putString(RECIPE_NAME, rr.getName());
        Intent i = new Intent(this, DetailsActivity.class);
        i.putExtras(extras);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void tryAgain(View view) {
        RecipeViewModel.performRequest(getApplication());
    }
}
