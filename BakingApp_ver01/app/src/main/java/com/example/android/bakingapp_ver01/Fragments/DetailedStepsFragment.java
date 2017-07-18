package com.example.android.bakingapp_ver01.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp_ver01.R;

/**
 * Created by johnosorio on 27/06/2017.
 */

public class DetailedStepsFragment extends Fragment {

    private String mStepDescription;

    private TextView stepDetailedTextView;

    public DetailedStepsFragment() {}

    public void setStepDescription(String stepDescription){
        mStepDescription = stepDescription;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_detailed_step_description, container, false);

        stepDetailedTextView = (TextView)rootView.findViewById(R.id.step_detailed_description);

        stepDetailedTextView.setText(mStepDescription);

        return rootView;

    }
}
