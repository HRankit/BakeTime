package com.udacity.baketime.baketime.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.baketime.baketime.R;
import com.udacity.baketime.baketime.misc.MyPreferences;
import com.udacity.baketime.baketime.models.Ingredient;
import com.udacity.baketime.baketime.models.RecipeResult;

import java.util.ArrayList;
import java.util.List;

import static com.udacity.baketime.baketime.misc.MiscFunctions.JSON_DATA;
import static com.udacity.baketime.baketime.misc.MiscFunctions.MAIN_ACTIVITY;
import static com.udacity.baketime.baketime.misc.MiscFunctions.NUMBER_OF_STEPS;
import static com.udacity.baketime.baketime.misc.MiscFunctions.WHICH_ACTIVITY;
import static com.udacity.baketime.baketime.misc.MiscFunctions.isEmpty;
import static com.udacity.baketime.baketime.widget.IngredientWidgetProvider.POSITION_TO_SHOW;

public class IngredientsWidgetService extends RemoteViewsService {
    private int position = 0;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if (intent != null) {
            position = intent.getIntExtra(POSITION_TO_SHOW, 0);
        }
        return new IngredientsRemoteViewsFactory(this.getApplicationContext(), position);
    }

}

class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<WidgetItem> mWidgetItems = new ArrayList<>();
    private final List<WidgetItem> mWidgetQty = new ArrayList<>();
    private final List<Integer> mImages = new ArrayList<>();
    private final Context mContext;
    private int positions;
    private int mStepsCount;


    IngredientsRemoteViewsFactory(Context context, int pos) {
        mContext = context;
        positions = pos;
    }

    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        MyPreferences yourPrefrence = MyPreferences.getInstance(mContext);
        mStepsCount = yourPrefrence.getDataInt(NUMBER_OF_STEPS);
        if (yourPrefrence.getDataInt(WHICH_ACTIVITY) == MAIN_ACTIVITY) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<List<RecipeResult>>() {
            }.getType();
            String json = yourPrefrence.getData(JSON_DATA);
            if (!isEmpty(json)) {
                List<RecipeResult> arrayList = gson.fromJson(json, type);
                if (arrayList.size() > positions) {
                    List<Ingredient> ingredients = arrayList.get(positions).getIngredients();
                    mStepsCount = ingredients.size();
                    for (int i = 0; i < ingredients.size(); i++) {
                        mWidgetItems.add(new WidgetItem(ingredients.get(i).getIngredient()));
                        mWidgetQty.add((new WidgetItem(ingredients.get(i).getQuantity()
                                + " " + ingredients.get(i).getMeasure())));
                        mImages.add(processMeasureDrawable(ingredients.get(i).getMeasure()));
                    }
                } else {
                    positions = 0;
                    List<Ingredient> ingredients = arrayList.get(positions).getIngredients();
                    mStepsCount = ingredients.size();

                    for (int i = 0; i < ingredients.size(); i++) {
                        mWidgetItems.add(new WidgetItem(ingredients.get(i).getIngredient()));
                        mWidgetQty.add((new WidgetItem(ingredients.get(i).getQuantity()
                                + " " + ingredients.get(i).getMeasure())));
                        mImages.add(processMeasureDrawable(ingredients.get(i).getMeasure()));
                    }
                }

            }
        }

    }


    public RemoteViews getViewAt(int position) {
        // position will always range from 0 to getCount() - 1.

        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget);
        rv.setTextViewText(R.id.ingredientName, mWidgetItems.get(position).text);
        rv.setTextViewText(R.id.ingredientMeasure, mWidgetQty.get(position).text);
        rv.setImageViewResource(R.id.iv_measure, mImages.get(position));

        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
//        Bundle extras = new Bundle();
//        extras.putInt(EXTRA_ITEM, position);
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtras(extras);
//
//        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);


        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.


        // Return the remote views object.
        return rv;
    }

    private int processMeasureDrawable(String measure) {
        int returnint;
        switch (measure) {
            case "CUP":
                returnint = R.mipmap.cup;
                break;
            case "TBLSP":
                returnint = R.mipmap.tablespoon;
                break;
            case "TSP":
                returnint = R.mipmap.teaspoon;
                break;
            case "K":
                returnint = R.mipmap.kg;
                break;
            case "G":
                returnint = R.mipmap.gram;
                break;
            case "OZ":
                returnint = R.mipmap.oz;
                break;
            case "UNIT":
                returnint = R.mipmap.unit;
                break;
            default:
                returnint = R.drawable.ic_launcher_background;
                break;

        }

        return returnint;
    }


    public void onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear();
    }

    public int getCount() {
        return mStepsCount;
    }


    public RemoteViews getLoadingView() {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
    }
}