package com.example.android.bakingapp_ver01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.android.bakingapp_ver01.Fragments.DetailedStepsFragment;
import com.example.android.bakingapp_ver01.Fragments.ExoPlayerFragment;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * Created by johnosorio on 18/06/2017.
 */

public class DetailedStepsActivity extends AppCompatActivity {

    private static final String TAG = DetailedStepsActivity.class.getSimpleName();

    private int stepDetailedId;

    private int recipeCompleteStepListSize;

    private Button backButton;

    private Button nextButton;

    private Bundle stepDetailedBundle;

    private List<Map<String, String>> recipeCompleteStepList;

    private FragmentManager fragmentManager;

    private ExoPlayerFragment exoPlayerFragment;

    private DetailedStepsFragment detailedStepsFragment;

    private long exoPlayerCurrentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This stores the current position of the ExoPlayer after onDestroy such as Orientation change
        if(savedInstanceState != null) {
            exoPlayerCurrentPosition = savedInstanceState.getLong("position_key");
            Log.d(TAG, "playback position is: " + exoPlayerCurrentPosition);
        }

        Intent stepDetailedIntent = getIntent();

        stepDetailedBundle = stepDetailedIntent.getExtras();

        //Retrieve the step data array from the bundle
        String[] stepDataArray = stepDetailedBundle.getStringArray(getString(R.string.extra_step_array));

        String stepDetailedDescription = stepDataArray[2];

        //This is to know what step we are currently in. To be used for our next or back buttons
        stepDetailedId = Integer.parseInt(stepDataArray[0]);

        //Retrieve the complete step list from Recipe Information Intent
        recipeCompleteStepList = (List<Map<String, String>>) stepDetailedBundle.getSerializable(getString(R.string.extra_step_complete_list));

        setContentView(R.layout.detailed_steps_activity);

        backButton = (Button)findViewById(R.id.back_button);

        nextButton = (Button)findViewById(R.id.next_button);

        //This is to be used by the next and back buttons
        recipeCompleteStepListSize = recipeCompleteStepList.size();

        //This is to make the Next Button disappear when we reach the last step in the recipe
        if(stepDetailedId == recipeCompleteStepListSize - 1) {
            nextButton.setVisibility(GONE);
        }
        //This is to make the Back Button disappear when we are at the first step of the recipe
        else if (stepDetailedId == 0) {
                backButton.setVisibility(GONE);
        }

        fragmentManager = getSupportFragmentManager();

        exoPlayerFragment = new ExoPlayerFragment();
        exoPlayerFragment.setStepArray(stepDataArray);
        exoPlayerFragment.setExoplayerCurrentPosition(exoPlayerCurrentPosition);
        fragmentManager.beginTransaction()
                .add(R.id.simple_exoplayer_fragment, exoPlayerFragment)
                .commit();

        detailedStepsFragment = new DetailedStepsFragment();
        detailedStepsFragment.setStepDescription(stepDetailedDescription);
        fragmentManager.beginTransaction()
                .add(R.id.step_detailed_description_fragment, detailedStepsFragment)
                .commit();
    }


    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        exoPlayerFragment.releasePlayer();
        exoPlayerFragment.setMediaSessionInactive();
        Log.d(TAG, "This is onDestroy");
    }


    public void nextButtonClick(View view) {

        if(stepDetailedId < recipeCompleteStepListSize - 1) {

            exoPlayerFragment.stopExoPlayer();

            Intent intent = new Intent(DetailedStepsActivity.this, DetailedStepsActivity.class);

            Map<String, String> nextStepMap = recipeCompleteStepList.get(stepDetailedId + 1);
            String nextStepId = nextStepMap.get(getString(R.string.extra_step_id) + String.valueOf(stepDetailedId + 1));
            String nextStepShortDescription = nextStepMap.get(getString(R.string.extra_step_short_description) + String.valueOf(stepDetailedId + 1));
            String nextStepDescription = nextStepMap.get(getString(R.string.extra_step_description) + String.valueOf(stepDetailedId + 1));
            String nextStepVideoUrl = nextStepMap.get(getString(R.string.extra_step_video_url) + String.valueOf(stepDetailedId + 1));
            String nextStepThumbnailUrl = nextStepMap.get(getString(R.string.extra_step_thumbnail_url) + String.valueOf(stepDetailedId + 1));

            String[] nextStepArray = {
                    nextStepId,
                    nextStepShortDescription,
                    nextStepDescription,
                    nextStepVideoUrl,
                    nextStepThumbnailUrl};

            Log.d(TAG, nextStepId + " " + nextStepDescription);

            Bundle bundle = new Bundle();
            bundle.putStringArray(getString(R.string.extra_step_array), nextStepArray);

            //Send the stepListMap to Detailed Steps Activity
            bundle.putSerializable(getString(R.string.extra_step_complete_list), (Serializable) recipeCompleteStepList);
            intent.putExtras(bundle);

            finish();

            startActivity(intent);

        }
    }

    public void backButtonClick(View view) {
        if(stepDetailedId > 0) {

            exoPlayerFragment.stopExoPlayer();

            Intent intent = new Intent(DetailedStepsActivity.this, DetailedStepsActivity.class);

            Map<String, String> nextStepMap = recipeCompleteStepList.get(stepDetailedId - 1);
            String nextStepId = nextStepMap.get(getString(R.string.extra_step_id) + String.valueOf(stepDetailedId - 1));
            String nextStepShortDescription = nextStepMap.get(getString(R.string.extra_step_short_description) + String.valueOf(stepDetailedId - 1));
            String nextStepDescription = nextStepMap.get(getString(R.string.extra_step_description) + String.valueOf(stepDetailedId - 1));
            String nextStepVideoUrl = nextStepMap.get(getString(R.string.extra_step_video_url) + String.valueOf(stepDetailedId - 1));
            String nextStepThumbnailUrl = nextStepMap.get(getString(R.string.extra_step_thumbnail_url) + String.valueOf(stepDetailedId - 1));

            String[] nextStepArray = {
                    nextStepId,
                    nextStepShortDescription,
                    nextStepDescription,
                    nextStepVideoUrl,
                    nextStepThumbnailUrl};

            Log.d(TAG, nextStepId + " " + nextStepDescription);

            Bundle bundle = new Bundle();
            bundle.putStringArray(getString(R.string.extra_step_array), nextStepArray);

            //Send the stepListMap to Detailed Steps Activity
            bundle.putSerializable(getString(R.string.extra_step_complete_list), (Serializable) recipeCompleteStepList);
            intent.putExtras(bundle);

            finish();

            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        exoPlayerCurrentPosition = exoPlayerFragment.getPlayerCurrentPosition();
        //Log.d(TAG, "Player current position is: " + exoPlayerCurrentPosition);
        outState.putLong("position_key", exoPlayerCurrentPosition);
    }
}
