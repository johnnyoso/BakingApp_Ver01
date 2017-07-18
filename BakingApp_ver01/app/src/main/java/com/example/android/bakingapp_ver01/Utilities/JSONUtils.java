package com.example.android.bakingapp_ver01.Utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.bakingapp_ver01.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john.osorio on 13/06/2017.
 */

public class JSONUtils {

    private final static String TAG = JSONUtils.class.getSimpleName();

    public static List<Map<String, String>> getRecipeDataFromJson(Context context, String responseFromWebsiteAsURL) throws JSONException{

        //For the RECIPE MAIN INFO
        final String RECIPE_ID = context.getString(R.string.recipe_id);
        final String RECIPE_NAME = context.getString(R.string.recipe_name);
        final String RECIPE_INGREDIENTS = context.getString(R.string.recipe_ingredients);
        final String RECIPE_STEPS = context.getString(R.string.recipe_steps);
        final String RECIPE_SERVINGS = context.getString(R.string.recipe_servings);
        final String RECIPE_IMAGE = context.getString(R.string.recipe_image);

        //For the INGREDIENTS INFORMATION
        final String INGREDIENTS_QUANTITY = context.getString(R.string.ingredients_quantity);
        final String INGREDIENTS_MEASURE = context.getString(R.string.ingredients_measure);
        final String INGREDIENTS_INGREDIENT = context.getString(R.string.ingredients_ingredient);

        //For the RECIPE STEPS
        final String STEP_ID = context.getString(R.string.step_id);
        final String STEP_SHORT_DESCRIPTION = context.getString(R.string.step_short_desription);
        final String STEP_DESCRIPTION = context.getString(R.string.step_description);
        final String STEP_VIDEO_URL = context.getString(R.string.step_video_url);
        final String STEP_THUMBNAIL_URL = context.getString(R.string.step_video_url);

        //The overall bundle of data to be sent out
        List<Map<String, String>> recipeBundle = new ArrayList<Map<String, String>>();

        JSONArray recipeInformationArray = new JSONArray(responseFromWebsiteAsURL);

        int arraySize = recipeInformationArray.length();

        for(int k = 0; k < arraySize; k++){

            JSONObject recipeInformationJson = recipeInformationArray.getJSONObject(k);

            //Store everything in this HASH MAP
            Map<String, String> recipeInformation = new HashMap<>();

            //Get the store the MAIN RECIPE INFORMATION
            String recipeId = recipeInformationJson.getString(RECIPE_ID);
            String recipeName = recipeInformationJson.getString(RECIPE_NAME);
            String recipeServings = recipeInformationJson.getString(RECIPE_SERVINGS);
            String recipeImage = recipeInformationJson.getString(RECIPE_IMAGE);

            recipeInformation.put(context.getString(R.string.extra_recipe_id), recipeId);
            recipeInformation.put(context.getString(R.string.extra_recipe_name), recipeName);
            recipeInformation.put(context.getString(R.string.extra_recipe_servings), recipeServings);
            recipeInformation.put(context.getString(R.string.extra_recipe_image), recipeImage);

            //Get and store the INGREDIENTS INFORMATION
            JSONArray recipeIngredientArray = recipeInformationJson.getJSONArray(RECIPE_INGREDIENTS);

            int recipeIngredientArraySize = recipeIngredientArray.length();

            //This is so we know how many ingredients there are for the recipe
            recipeInformation.put(context.getString(R.string.extra_ingredients_list_size), String.valueOf(recipeIngredientArraySize));

            for(int i = 0; i < recipeIngredientArraySize; i++) {

                JSONObject currentIngredientInformation = recipeIngredientArray.getJSONObject(i);

                String quantity = currentIngredientInformation.getString(INGREDIENTS_QUANTITY);
                String measure = currentIngredientInformation.getString(INGREDIENTS_MEASURE);
                String ingredient = currentIngredientInformation.getString(INGREDIENTS_INGREDIENT);

                //Let's just put an ID for each ingredient??
                recipeInformation.put(context.getString(R.string.extra_ingredients_id) + String.valueOf(i), String.valueOf(i));
                recipeInformation.put(context.getString(R.string.extra_ingredients_quantity) + String.valueOf(i), quantity);
                recipeInformation.put(context.getString(R.string.extra_ingredients_measure) + String.valueOf(i), measure);
                recipeInformation.put(context.getString(R.string.extra_ingredients_ingredient) + String.valueOf(i), ingredient);
            }

            //Get and store the RECIPE STEPS INFORMATION
            JSONArray recipeStepsArray = recipeInformationJson.getJSONArray(RECIPE_STEPS);

            int recipeStepsArraySize = recipeStepsArray.length();

            //This is so we know how many steps there are for the recipe
            recipeInformation.put(context.getString(R.string.extra_step_list_size), String.valueOf(recipeStepsArraySize));

            for(int j = 0; j < recipeStepsArraySize; j++){

                JSONObject currentStepInformation = recipeStepsArray.getJSONObject(j);

                String id = currentStepInformation.getString(STEP_ID);
                String shortDescription = currentStepInformation.getString(STEP_SHORT_DESCRIPTION);
                String description = currentStepInformation.getString(STEP_DESCRIPTION);
                String videoUrl = currentStepInformation.getString(STEP_VIDEO_URL);
                String thumbnailUrl = currentStepInformation.getString(STEP_THUMBNAIL_URL);

                recipeInformation.put(context.getString(R.string.extra_step_id) + String.valueOf(j), id);
                recipeInformation.put(context.getString(R.string.extra_step_short_description) + String.valueOf(j), shortDescription);
                recipeInformation.put(context.getString(R.string.extra_step_description) + String.valueOf(j), description);
                recipeInformation.put(context.getString(R.string.extra_step_video_url) + String.valueOf(j), videoUrl);
                recipeInformation.put(context.getString(R.string.extra_step_thumbnail_url) + String.valueOf(j), thumbnailUrl);

            }

                recipeBundle.add(k, recipeInformation);
            }

        return recipeBundle;
    }
}
