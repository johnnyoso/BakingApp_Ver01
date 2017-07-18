package com.example.android.bakingapp_ver01.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp_ver01.R;

import java.util.List;
import java.util.Map;

/**
 * Created by johnosorio on 17/06/2017.
 */

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListAdapterViewHolder> {

    private static final String TAG = StepListAdapter.class.getSimpleName();

    private List<Map<String, String>> mStepMapList;


    private final StepListAdapterOnClickHandler mClickHandler;


    public interface StepListAdapterOnClickHandler {

        void onClick(String[] currentStepArray);
    }

    public StepListAdapter(StepListAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
    }

    public class StepListAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView stepNameTextView;

        public StepListAdapterViewHolder(View view){
            super(view);

            stepNameTextView = (TextView)view.findViewById(R.id.step_short_description);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            int adapterPosition = getAdapterPosition();

            Map<String, String> stepMap = mStepMapList.get(adapterPosition);

            String stepId = stepMap.get(context.getString(R.string.extra_step_id) + String.valueOf(adapterPosition));
            String stepShortDescription = stepMap.get(context.getString(R.string.extra_step_short_description) + String.valueOf(adapterPosition));
            String stepDescription = stepMap.get(context.getString(R.string.extra_step_description) + String.valueOf(adapterPosition));
            String stepVideoUrl = stepMap.get(context.getString(R.string.extra_step_video_url) + String.valueOf(adapterPosition));
            String stepThumbnailUrl = stepMap.get(context.getString(R.string.extra_step_thumbnail_url) + String.valueOf(adapterPosition));

            String[] stepDataArray = {stepId, stepShortDescription, stepDescription, stepVideoUrl, stepThumbnailUrl};
            mClickHandler.onClick(stepDataArray);

        }
    }
    @Override
    public StepListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.steps_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new StepListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepListAdapterViewHolder holder, int position) {

        Context context = holder.stepNameTextView.getContext();
        String currentStepShortDescription = mStepMapList.get(position).get(context.getString(R.string.extra_step_short_description)+String.valueOf(position));

        holder.stepNameTextView.setText(currentStepShortDescription);
    }

    @Override
    public int getItemCount() {
        if(mStepMapList.size() == 0){
            return 0;
        }
        return mStepMapList.size();
    }

    public void setStepArray(List<Map<String, String>> stepMapList) {
        mStepMapList = stepMapList;
        notifyDataSetChanged();
    }

}
