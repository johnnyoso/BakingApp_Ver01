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
 * Created by johnosorio on 16/06/2017.
 */

public class RecipeNameAdapter extends RecyclerView.Adapter<RecipeNameAdapter.RecipeNameAdapterViewHolder> {

    private static final String TAG = RecipeNameAdapter.class.getSimpleName();

    private List<Map<String, String>> mRecipeBundle;

    //An on-click handler that makes it easy for an Activity to interface with the recylerView
    private final RecipeNameAdapterOnClickHandler mClickHandler;

    //This is the interface that recieves onClick messages
    public interface RecipeNameAdapterOnClickHandler {

        void onClick(Map<String, String> currentRecipeBundle);
    }

    public RecipeNameAdapter(RecipeNameAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
    }

    public class RecipeNameAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView recipeNameTextView;

        public RecipeNameAdapterViewHolder(View view) {
            super(view);

            recipeNameTextView = (TextView)view.findViewById(R.id.recipe_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            Map<String, String> currentRecipeData = mRecipeBundle.get(adapterPosition);
            mClickHandler.onClick(currentRecipeData);

            Log.d(TAG, "Adapter is at position: " + adapterPosition);
        }
    }

    @Override
    public RecipeNameAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new RecipeNameAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeNameAdapterViewHolder holder, int position) {

        Context context = holder.recipeNameTextView.getContext();
        String currentRecipeName = mRecipeBundle.get(position).get(context.getString(R.string.extra_recipe_name));
        holder.recipeNameTextView.setText(currentRecipeName);
    }

    @Override
    public int getItemCount() {
        if(mRecipeBundle == null) {
            return 0;
        }
        return mRecipeBundle.size();
    }

    public void setRecipeBundle(List<Map<String, String>> recipeBundle) {
        mRecipeBundle = recipeBundle;
        notifyDataSetChanged();
    }
}
