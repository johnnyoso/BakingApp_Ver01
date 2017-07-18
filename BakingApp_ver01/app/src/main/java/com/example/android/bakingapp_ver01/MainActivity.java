package com.example.android.bakingapp_ver01;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingapp_ver01.Adapters.RecipeNameAdapter;
import com.example.android.bakingapp_ver01.Utilities.JSONUtils;
import com.example.android.bakingapp_ver01.Utilities.NetworkUtils;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements RecipeNameAdapter.RecipeNameAdapterOnClickHandler {

    private final static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private RecipeNameAdapter mRecipeNameAdapter;

    //This is for our ingredient list widget
    private static String[] ingredientListWidget;

    private static String recipeNameWidget;

    //TODO: Create a progress bar that shows loading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_recipe_name);
        mRecipeNameAdapter = new RecipeNameAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecipeNameAdapter);

        makeRecipeUrlQuery();
    }

    @Override
    public void onClick(Map<String, String> recipeMap) {

        Context context = this;

        //Clicking this will open the Recipe Information Activity
        Intent recipeInformationIntent = new Intent(context, RecipeInformationActivity.class);
        Bundle recipeInformationBundle = new Bundle();

        //TODO: do this instead of the putMapValuesToBundle method: recipeInformationBundle.putSerializable("KEY", (Serializable) recipeMap);
        recipeInformationBundle = putMapValuesToBundle(context, recipeMap, recipeInformationBundle);

        //TODO: Update the widget ingredient list info
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientListWidgetProvider.class));

        StringBuilder builder = new StringBuilder();
        for(String s : ingredientListWidget) {
            builder.append(s + "\n");
        }
        String str = builder.toString();
        Log.d(TAG, "Ingredient List is: " + str);
        IngredientListWidgetProvider.updateIngredientList(this, appWidgetManager, appWidgetIds, str, recipeNameWidget);

        //Send the bundle info to the Recipe Information Activity
        recipeInformationIntent.putExtras(recipeInformationBundle);
        startActivity(recipeInformationIntent);
    }

    //This method calls the AsyncTask and downloads the Recipe details
    private void makeRecipeUrlQuery() {
        URL recipeDataUrl = NetworkUtils.buildBakingAppUrl();
        new RecipeDataTask().execute(recipeDataUrl);
    }

    public class RecipeDataTask extends AsyncTask<URL, Void, List<Map<String, String>>> {

        @Override
        protected List<Map<String, String>> doInBackground(URL... params) {
            URL bakingAppUrl = params[0];
            List<Map<String, String>> bakingAppData = null;

            try {
                String responseFromUrl = NetworkUtils.getResponseFromHttpUrl(bakingAppUrl);
                bakingAppData = JSONUtils.getRecipeDataFromJson(MainActivity.this, responseFromUrl);

                return bakingAppData;

            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> bakingAppData) {
            if(bakingAppData != null) {

                mRecipeNameAdapter.setRecipeBundle(bakingAppData);

            } else {
                Log.e(TAG, "List is empty!!");
            }
        }
    }

    //This is a helper method that puts all values from a Map to a Bundle. Not really very efficient
    public static Bundle putMapValuesToBundle(Context context, Map<String, String> map, Bundle bundle) {

        //Main recipe information
        String recipeId = map.get(context.getString(R.string.extra_recipe_id));
        String recipeName = map.get(context.getString(R.string.extra_recipe_name));
        String recipeServings = map.get(context.getString(R.string.extra_recipe_servings));
        String recipeImage = map.get(context.getString(R.string.extra_recipe_image));

        //This is for the ingredient list widget
        recipeNameWidget = recipeName;

        //Recipe ingredients
        String ingredientListSize = map.get(context.getString(R.string.extra_ingredients_list_size));

        ingredientListWidget = new String[Integer.parseInt(ingredientListSize)];

        for(int i = 0; i < Integer.parseInt(ingredientListSize); i++) {

            String ingredientId = map.get(context.getString(R.string.extra_ingredients_id) + String.valueOf(i));
            String ingredientQuantity = map.get(context.getString(R.string.extra_ingredients_quantity) + String.valueOf(i));
            String ingredientMeasure = map.get(context.getString(R.string.extra_ingredients_measure) + String.valueOf(i));
            String ingredientIngredient = map.get(context.getString(R.string.extra_ingredients_ingredient) + String.valueOf(i));

            bundle.putString(context.getString(R.string.extra_ingredients_id) + String.valueOf(i), ingredientId);
            bundle.putString(context.getString(R.string.extra_ingredients_quantity) + String.valueOf(i), ingredientQuantity);
            bundle.putString(context.getString(R.string.extra_ingredients_measure) + String.valueOf(i), ingredientMeasure);
            bundle.putString(context.getString(R.string.extra_ingredients_ingredient) + String.valueOf(i), ingredientIngredient);

            //This is for the ingredient list widget
            ingredientListWidget[i] = ingredientIngredient + " " + ingredientQuantity + " " + ingredientMeasure;
        }
        //Recipe steps
        String stepListSize = map.get(context.getString(R.string.extra_step_list_size));

        for(int j = 0; j < Integer.parseInt(stepListSize); j++) {
            String stepId = map.get(context.getString(R.string.extra_step_id) + String.valueOf(j));
            String stepShortDescription = map.get(context.getString(R.string.extra_step_short_description) + String.valueOf(j));
            String stepDescription = map.get(context.getString(R.string.extra_step_description) + String.valueOf(j));
            String stepVideoUrl = map.get(context.getString(R.string.extra_step_video_url) + String.valueOf(j));
            String stepThumbnailUrl = map.get(context.getString(R.string.extra_step_thumbnail_url) + String.valueOf(j));

            bundle.putString(context.getString(R.string.extra_step_id) + String.valueOf(j), stepId);
            bundle.putString(context.getString(R.string.extra_step_short_description) + String.valueOf(j), stepShortDescription);
            bundle.putString(context.getString(R.string.extra_step_description) + String.valueOf(j), stepDescription);
            bundle.putString(context.getString(R.string.extra_step_video_url) + String.valueOf(j), stepVideoUrl);
            bundle.putString(context.getString(R.string.extra_step_thumbnail_url) + String.valueOf(j), stepThumbnailUrl);
        }
        //Now put them all in a Bundle

        //Main Recipe Information
        bundle.putString(context.getString(R.string.extra_recipe_id), recipeId);
        bundle.putString(context.getString(R.string.extra_recipe_name), recipeName);
        bundle.putString(context.getString(R.string.extra_recipe_servings), recipeServings);
        bundle.putString(context.getString(R.string.extra_recipe_image), recipeImage);

        //Recipe Ingredients
        bundle.putString(context.getString(R.string.extra_ingredients_list_size), ingredientListSize);

        //Recipe Steps
        bundle.putString(context.getString(R.string.extra_step_list_size), stepListSize);

        return bundle;
    }
}
