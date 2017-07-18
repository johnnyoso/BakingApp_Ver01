package com.example.android.bakingapp_ver01;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientListWidgetProvider extends AppWidgetProvider {

    private static final String TAG = IngredientListWidgetProvider.class.getSimpleName();
    private static String mIngredientList;
    private static String mRecipeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String ingredientList, String recipeName) {

        mIngredientList = ingredientList;
        mRecipeName = recipeName;

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intent, 0);

        views.setOnClickPendingIntent(R.id.ingredient_list_widget_text, pendingIntent);

        Log.d(TAG,"Ingredient List is: " + ingredientList);
        views.setTextViewText(R.id.ingredient_list_widget_text, ingredientList);
        views.setTextViewText(R.id.recipe_name_widget, recipeName);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, mIngredientList, mRecipeName);
        }
    }
    public static void updateIngredientList(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String ingredientList, String recipeName) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, ingredientList, recipeName);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

