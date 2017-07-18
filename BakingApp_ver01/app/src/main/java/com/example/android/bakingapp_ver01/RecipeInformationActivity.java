package com.example.android.bakingapp_ver01;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;


import com.example.android.bakingapp_ver01.Adapters.IngredientListAdapter;
import com.example.android.bakingapp_ver01.Adapters.StepListAdapter;
import com.example.android.bakingapp_ver01.Fragments.DetailedStepsFragment;
import com.example.android.bakingapp_ver01.Fragments.ExoPlayerFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by johnosorio on 17/06/2017.
 */

public class RecipeInformationActivity extends AppCompatActivity
        implements StepListAdapter.StepListAdapterOnClickHandler{

    private static final String TAG = RecipeInformationActivity.class.getSimpleName();

    private String[] ingredientNameArray;
    private List<Map<String, String>> stepListMapList;

    private RecyclerView mIngredientRecyclerView;
    private RecyclerView mStepListRecyclerView;

    private TextView recipeTitle;

    private IngredientListAdapter mIngredientListAdapter;
    private StepListAdapter mStepListAdapter;

    //This is the Bundle that we send over to Detailed Steps Activity
    private Bundle recipeInformationBundle;

    private FragmentManager fragmentManager;

    private ExoPlayerFragment exoPlayerFragment;

    private DetailedStepsFragment detailedStepsFragment;

    private boolean mTwoPane;

    private String[] mStepDataArrayForFragment;

    private long exoPlayerCurrentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getBoolean(R.bool.landscape_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.recipe_information_activity);

        recipeTitle = (TextView)findViewById(R.id.recipe_title);

        //Get the intent from Main Activity
        Intent recipeInformationIntent = getIntent();

        //Check if there is an existing bundle from savedInstance
        if(savedInstanceState != null) {
            Log.d(TAG, "RecipeInformationBundle is not null");
            recipeInformationBundle = savedInstanceState.getBundle("bundle");
        } else {
            Log.d(TAG, "RecipeInformationBundle is null");
            recipeInformationBundle = recipeInformationIntent.getExtras();
        }

        //This determines if device is a Tablet and in Landscape orientation
        if(findViewById(R.id.two_pane_layout) != null) {
            mTwoPane = true;

            //Play the first video upon onCreate
            playFragments(setStepListArrayForFragment(recipeInformationBundle), exoPlayerCurrentPosition);
            setIngredientListRecyclerView(recipeInformationBundle);
            setStepListRecyclerView(recipeInformationBundle);

        } else {

            setIngredientListRecyclerView(recipeInformationBundle);
            setStepListRecyclerView(recipeInformationBundle);
        }
    }

    @Override
    public void onClick(String[] currentStepArray) {

        //This is where we send the detailed step by step instruction to the next activity
        //Send the stepDescription and videoUrl and thumbnail Url

        Intent stepDetailedIntent = new Intent(this, DetailedStepsActivity.class);
        Bundle stepBundle = new Bundle();
        stepBundle.putStringArray(getString(R.string.extra_step_array), currentStepArray);

        //Send the stepListMap to Detailed Steps Activity
        stepBundle.putSerializable(getString(R.string.extra_step_complete_list), (Serializable) stepListMapList);

        stepBundle.putBundle("another_bundle", recipeInformationBundle);

        stepDetailedIntent.putExtras(stepBundle);

        //This is for when we use a tablet and it's in landscape mode
        if(mTwoPane) {

            //Clear any existing exoplayer sessions
            exoPlayerFragment.releasePlayer();
            exoPlayerFragment.setMediaSessionInactive();

            //Clear any existing recipe step
            detailedStepsFragment.setStepDescription("");

            //Remove the previous fragments before creating new ones
            fragmentManager.beginTransaction().remove(exoPlayerFragment).commit();
            fragmentManager.beginTransaction().remove(detailedStepsFragment).commit();

            playFragments(currentStepArray, exoPlayerCurrentPosition);

        } else {
            startActivity(stepDetailedIntent);
        }
    }

    public void setIngredientListRecyclerView(Bundle recipeInformationBundle) {

        //Set the recipe name text view
        recipeTitle.setText(recipeInformationBundle.getString(getString(R.string.extra_recipe_name)));

        //Prepare the Ingredient List Recycler View
        mIngredientListAdapter = new IngredientListAdapter();
        mIngredientRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_ingredient_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mIngredientRecyclerView.setLayoutManager(linearLayoutManager);
        mIngredientRecyclerView.setHasFixedSize(true);
        mIngredientRecyclerView.setAdapter(mIngredientListAdapter);

        //Get the Ingredient List from the Bundle
        int ingredientNameArraySize =  Integer.parseInt(recipeInformationBundle.getString(getString(R.string.extra_ingredients_list_size)));

        //Initialize the array
        ingredientNameArray = new String[ingredientNameArraySize];

        for(int i = 0; i < ingredientNameArraySize; i++) {
            String ingredientName = recipeInformationBundle.getString(getString(R.string.extra_ingredients_ingredient)+String.valueOf(i));
            String ingredientQuantity = recipeInformationBundle.getString(getString(R.string.extra_ingredients_quantity)+String.valueOf(i));
            String ingredientMeasure = recipeInformationBundle.getString(getString(R.string.extra_ingredients_measure)+String.valueOf(i));
            ingredientNameArray[i] = ingredientName + " " + ingredientQuantity + " " + ingredientMeasure;
        }
        //Set the Ingredient List Adapter with the Ingredient Array
        mIngredientListAdapter.setIngredientArray(ingredientNameArray);
    }

    public void setStepListRecyclerView(Bundle recipeInformationBundle) {
        mStepListAdapter = new StepListAdapter(this);
        mStepListRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_steps_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mStepListRecyclerView.setLayoutManager(linearLayoutManager);
        mStepListRecyclerView.setHasFixedSize(true);
        mStepListRecyclerView.setAdapter(mStepListAdapter);

        int stepListArraySize = Integer.parseInt(recipeInformationBundle.getString(getString(R.string.extra_step_list_size)));

        stepListMapList = new ArrayList<Map<String, String>>();

        for(int i = 0; i < stepListArraySize; i++){
            Map<String, String> stepListMap = new HashMap<>();

            String stepId = recipeInformationBundle.getString(getString(R.string.extra_step_id) + String.valueOf(i));
            String stepShortDescription = recipeInformationBundle.getString(getString(R.string.extra_step_short_description) + String.valueOf(i));
            String stepDescription = recipeInformationBundle.getString(getString(R.string.extra_step_description) + String.valueOf(i));
            String stepVideoUrl = recipeInformationBundle.getString(getString(R.string.extra_step_video_url) + String.valueOf(i));
            String stepThumbnailUrl = recipeInformationBundle.getString(getString(R.string.extra_step_thumbnail_url) + String.valueOf(i));

            stepListMap.put(getString(R.string.extra_step_id) + String.valueOf(i), stepId);
            stepListMap.put(getString(R.string.extra_step_short_description) + String.valueOf(i), stepShortDescription);
            stepListMap.put(getString(R.string.extra_step_description) + String.valueOf(i), stepDescription);
            stepListMap.put(getString(R.string.extra_step_video_url) + String.valueOf(i), stepVideoUrl);
            stepListMap.put(getString(R.string.extra_step_thumbnail_url) + String.valueOf(i), stepThumbnailUrl);

            stepListMapList.add(i, stepListMap);
        }
        mStepListAdapter.setStepArray(stepListMapList);

    }

    //Create a helper method to produce a string array for inital video play: just play the first step/instruction video
    public String[] setStepListArrayForFragment(Bundle recipeInformationBundle) {

        String stepId = recipeInformationBundle.getString(getString(R.string.extra_step_id) + "0");
        String stepShortDescription = recipeInformationBundle.getString(getString(R.string.extra_step_short_description) + "0");
        String stepDescription = recipeInformationBundle.getString(getString(R.string.extra_step_description) + "0");
        String stepVideoUrl = recipeInformationBundle.getString(getString(R.string.extra_step_video_url) + "0");
        String stepThumbnailUrl = recipeInformationBundle.getString(getString(R.string.extra_step_thumbnail_url) + "0");

        mStepDataArrayForFragment = new String[] {stepId, stepShortDescription, stepDescription, stepVideoUrl, stepThumbnailUrl};

        return mStepDataArrayForFragment;
    }

    //Create a helper method for playing the fragments
    public void playFragments(String[] stepArray, long currentPosition) {

        String detailedInstruction = stepArray[2];

        fragmentManager = getSupportFragmentManager();

        //Set the fragments
        exoPlayerFragment = new ExoPlayerFragment();
        exoPlayerFragment.setStepArray(stepArray);
        exoPlayerFragment.setExoplayerCurrentPosition(currentPosition);
        fragmentManager.beginTransaction()
                .add(R.id.simple_exoplayer_fragment, exoPlayerFragment)
                .commit();

        detailedStepsFragment = new DetailedStepsFragment();
        detailedStepsFragment.setStepDescription(detailedInstruction);
        fragmentManager.beginTransaction()
                .add(R.id.step_detailed_description_fragment, detailedStepsFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "This is onDestroy");

        //Only release player if it's a tablet and landscape mode
        if(mTwoPane) {
            exoPlayerFragment.releasePlayer();
            exoPlayerFragment.setMediaSessionInactive();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //Store the existing recipe bundle to be carried over to the onCreate when we flip the orientation
        outState.putBundle("bundle", recipeInformationBundle);
    }
}
