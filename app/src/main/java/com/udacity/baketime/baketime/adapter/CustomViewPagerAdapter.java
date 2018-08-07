package com.udacity.baketime.baketime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.baketime.baketime.R;
import com.udacity.baketime.baketime.models.RecipeResult;
import com.udacity.baketime.baketime.models.Step;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import timber.log.Timber;

import static com.udacity.baketime.baketime.misc.MiscFunctions.CapitalizeString;
import static com.udacity.baketime.baketime.misc.MiscFunctions.isEmpty;

public class CustomViewPagerAdapter extends PagerAdapter {

    private final Context mContext;

    private final RecipeResult mRecipeResult;

    public CustomViewPagerAdapter(Context context, RecipeResult recipeResult) {
        mContext = context;
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


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Timber.d("THis is the position %s", position);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView;
        if (position == 0) {
            assert inflater != null;
            itemView = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
            itemView.setTag("View" + position);


            RecyclerView recipeRecyclerView = itemView.findViewById(R.id.rv_ingredients);
            recipeRecyclerView.setFocusable(false);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 1);
            Timber.d("Showing Data Now");

            recipeRecyclerView.setLayoutManager(mLayoutManager);
            recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());


            IngredientsListAdapter adapter = new IngredientsListAdapter(mContext, mRecipeResult.getIngredients(), mRecipeResult.getName());
            recipeRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            container.addView(itemView);
        } else {
            assert inflater != null;
            itemView = inflater.inflate(R.layout.step_card_item, container,
                    false);
            Step stepDetails = mRecipeResult.getSteps().get(position - 1);

            itemView.setTag("View" + position);

            TextView step_description = itemView.findViewById(R.id.step_description);
            step_description.setText(getUTFdata(stepDetails.getDescription()));

            TextView step_short_descrpition = itemView.findViewById(R.id.step_short_descrpition);
            step_short_descrpition.setText(getUTFdata(stepDetails.getShortDescription()));

            TextView step_video_url = itemView.findViewById(R.id.step_video_url);
            step_video_url.setText(stepDetails.getVideoURL());

            TextView step_thumbnail_url = itemView.findViewById(R.id.step_thumbnail_url);
            step_thumbnail_url.setText(stepDetails.getThumbnailURL());

            if (isEmpty(stepDetails.getVideoURL()) && isEmpty(stepDetails.getThumbnailURL())) {
                TextView label_not_playing = itemView.findViewById(R.id.label_not_playing);
                label_not_playing.setVisibility(View.GONE);
            }
            container.addView(itemView);
        }


        return itemView;
    }


    private String getUTFdata(String str) {
        String stringg = "";
        try {
            stringg = URLEncoder.encode(str, "UTF-8");
            stringg = URLDecoder.decode(stringg, "UTF-8");
            stringg = CapitalizeString(stringg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringg;
    }


    @Override
    public int getCount() {
        return mRecipeResult.getSteps().size() + 1;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

}
