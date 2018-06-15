package com.example.pavol.bakingdiary.customAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pavol.bakingdiary.R;
import com.example.pavol.bakingdiary.customObjects.IngredientObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder> {

    private ArrayList<IngredientObject> ingredientList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_tv)
        TextView ingredient;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public IngredientListAdapter(ArrayList<IngredientObject> list) {
        this.ingredientList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.ingredient_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final IngredientObject currentObject = ingredientList.get(position);
        String name = currentObject.mIngredient;
        String quantity = currentObject.mIngredientQuantity;
        String unit = currentObject.mIngredientMeasureUnit;
        String wholeInformation = name + ": " + quantity + " " + unit;

        holder.ingredient.setText(wholeInformation);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
