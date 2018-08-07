package com.udacity.baketime.baketime.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.baketime.baketime.R;
import com.udacity.baketime.baketime.misc.MyPreferences;
import com.udacity.baketime.baketime.models.RecipeResult;

import java.util.List;

import static com.udacity.baketime.baketime.misc.MiscFunctions.JSON_DATA;
import static com.udacity.baketime.baketime.misc.MiscFunctions.isEmpty;


public class IngredientWidgetProvider extends AppWidgetProvider {


    public static final String POSITION_TO_SHOW = "pos_to_show";
    private static final String NEXT = "NEXT";
    private static final String ACTION_MENU_CLICKED = "MenuClicked";
    private static final String PREVIOUS = "PREVIOUS";
    private static Integer POSITION = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the widgets with the remote adapter
        updateViews(context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    private void updateViews(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {


            // Here we setup the intent which points to the StackViewService which will
            // provide the views for this collection.
            Intent intent = new Intent(context, IngredientsWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.putExtra(POSITION_TO_SHOW, POSITION);
            intent.setAction(ACTION_MENU_CLICKED);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_layout);
            rv.setRemoteAdapter(appWidgetId, R.id.gridView1, intent);
            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            rv.setEmptyView(R.id.gridView1, R.id.empty_view);

            // Here we setup the a pending intent template. Individuals items of a collection
            // cannot setup their own pending intents, instead, the collection as a whole can
            // setup a pending intent template, and the individual items can set a fillInIntent
            // to create unique before on an item to item basis.


            Intent toastIntent = new Intent(context, IngredientWidgetProvider.class);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.gridView1, toastPendingIntent);


            rv.setTextViewText(R.id.label_recipe_name, getRecipeNameAtPosition(context, POSITION));

            rv.setOnClickPendingIntent(R.id.next, getPendingSelfIntent(context, NEXT));
            rv.setOnClickPendingIntent(R.id.previous, getPendingSelfIntent(context, PREVIOUS));


            appWidgetManager.updateAppWidget(appWidgetId, rv);


        }
    }

    private String getRecipeNameAtPosition(Context mContext, int position) {
        MyPreferences yourPrefrence = MyPreferences.getInstance(mContext);
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<List<RecipeResult>>() {
        }.getType();
        String json = yourPrefrence.getData(JSON_DATA);
        if (!isEmpty(json)) {
            List<RecipeResult> arrayList = gson.fromJson(json, type);
            return arrayList.get(position).getName();
        } else {
            return "Bake Time";

        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case NEXT: {

                if (POSITION <= 2) {
                    POSITION = POSITION + 1;
                } else {
                    POSITION = 0;

                }

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_layout);
                ComponentName appWidget = new ComponentName(context, IngredientWidgetProvider.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidget, views);
                int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                        new ComponentName(context, IngredientWidgetProvider.class));

                updateViews(context, appWidgetManager, appWidgetIds);

                break;
            }
            case PREVIOUS: {
                if (POSITION == 0) {
                    POSITION = 3;
                } else {
                    POSITION = POSITION - 1;
                }
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_layout);
                ComponentName appWidget = new ComponentName(context, IngredientWidgetProvider.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidget, views);
                int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                        new ComponentName(context, IngredientWidgetProvider.class));

                updateViews(context, appWidgetManager, appWidgetIds);

                break;
            }
            default: {

                AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
                int[] appWidgetId = AppWidgetManager.getInstance(context).getAppWidgetIds(
                        new ComponentName(context, IngredientWidgetProvider.class));
                mgr.updateAppWidget(appWidgetId, views);
                break;
            }
        }

        super.onReceive(context, intent);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }


}
