package com.udacity.baketime.baketime;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.baketime.baketime.adapter.IngredientsListAdapter;
import com.udacity.baketime.baketime.models.Ingredient;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import timber.log.Timber;


public class IngredientsFragment extends Fragment {


    public IngredientsFragment() {
    }


    public static Fragment newInstance(String ingredients, String name) {
        IngredientsFragment myfrag = new IngredientsFragment();

        Bundle bundle = new Bundle();

        bundle.putString("list", ingredients);
        bundle.putString("name", name);


        myfrag.setArguments(bundle);
        return myfrag;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_ingredients_list, container, false);

        if (getArguments() != null) {

            String name = getArguments().getString("name");

            Gson gson = new Gson();

            Type collectionType = new TypeToken<Collection<Ingredient>>() {
            }.getType();
            List<Ingredient> ingredients = gson.fromJson(getArguments().getString("list"), collectionType);

            RecyclerView recipeRecyclerView = rootview.findViewById(R.id.rv_ingredients);
            recipeRecyclerView.setFocusable(false);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            Timber.d("Showing Data Now");

            recipeRecyclerView.setLayoutManager(mLayoutManager);
            recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());


            IngredientsListAdapter adapter = new IngredientsListAdapter(getActivity(), ingredients, name);
            recipeRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        return rootview;
    }
}