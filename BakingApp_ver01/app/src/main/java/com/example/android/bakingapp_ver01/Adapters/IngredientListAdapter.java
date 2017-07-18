package com.example.android.bakingapp_ver01.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp_ver01.R;


/**
 * Created by johnosorio on 17/06/2017.
 */

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientListAdapterViewHolder> {

    private static final String TAG = IngredientListAdapter.class.getSimpleName();

    private String[] mIngredientArray;

    public class IngredientListAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView ingredientListTextView;

        public IngredientListAdapterViewHolder(View view) {
            super(view);

            ingredientListTextView = (TextView)view.findViewById(R.id.ingredient_name_quantity_measurement);
        }
    }
    @Override
    public IngredientListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new IngredientListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientListAdapterViewHolder holder, int position) {

        String ingredientNameQuantityMeasure = mIngredientArray[position];

        holder.ingredientListTextView.setText(ingredientNameQuantityMeasure);
    }

    @Override
    public int getItemCount() {
        if(mIngredientArray.length == 0) {
            return 0;
        }
        return mIngredientArray.length;
    }

    public void setIngredientArray(String[] ingredientArray) {
        mIngredientArray = ingredientArray;
        notifyDataSetChanged();
    }
}
