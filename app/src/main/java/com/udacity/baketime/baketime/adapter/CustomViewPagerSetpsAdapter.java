package com.udacity.baketime.baketime.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.udacity.baketime.baketime.AllStepsFragment;
import com.udacity.baketime.baketime.IngredientsFragment;
import com.udacity.baketime.baketime.models.RecipeResult;
import com.udacity.baketime.baketime.models.Step;


/**
 * Created by Warcode on 11/25/2015.
 */
class CustomViewPagerSetpsAdapter extends FragmentStatePagerAdapter {

    private final RecipeResult mRecipeResult;


    public CustomViewPagerSetpsAdapter(FragmentManager fm, Context context, RecipeResult recipeResult) {
        super(fm);

        mRecipeResult = recipeResult;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String returnString;
        switch (position) {
            case 0:
                returnString = "Ingredients";
                break;
            case 1:
                returnString = "Introduction";
                break;
            default:
                returnString = "Step " + Integer.valueOf(position - 1).toString();
                break;
        }
        return returnString;
    }


    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            Gson gson = new Gson();
            String ingredients = gson.toJson(mRecipeResult.getIngredients());
            return IngredientsFragment.newInstance(
                    ingredients, mRecipeResult.getName()
            );


        } else {
            Step stepDetails = mRecipeResult.getSteps().get(position - 1);

            return AllStepsFragment.newInstance(position, stepDetails.getShortDescription(),
                    stepDetails.getDescription(), stepDetails.getVideoURL(), stepDetails.getThumbnailURL());

        }


    }

    @Override
    public int getCount() {
        return mRecipeResult.getSteps().size() + 1;
    }


}