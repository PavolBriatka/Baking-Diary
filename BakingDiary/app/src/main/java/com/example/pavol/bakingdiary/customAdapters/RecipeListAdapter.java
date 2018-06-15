package com.example.pavol.bakingdiary.customAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pavol.bakingdiary.R;
import com.example.pavol.bakingdiary.customObjects.RecipeObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {


    private ArrayList<RecipeObject> listOfRecipes;
    private Context context;

    OnItemClickListener itemListener;

    public interface OnItemClickListener {
        void onItemClicked(RecipeObject passedObject);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_tv)
        TextView recipeNameTv;
        @BindView(R.id.servings_tv)
        TextView servingsTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public RecipeListAdapter(ArrayList<RecipeObject> list, Context context) {
        this.listOfRecipes = list;
        this.itemListener = (OnItemClickListener) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.master_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final RecipeObject currentObject = listOfRecipes.get(position);

        holder.recipeNameTv.setText(currentObject.mName);

        String servingsString = "Servings: " + currentObject.mNumberOfServings;
        holder.servingsTv.setText(servingsString);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onItemClicked(currentObject);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listOfRecipes == null) return 0;
        return listOfRecipes.size();
    }

    public void setMasterAdapterData(ArrayList<RecipeObject> passedArrayList) {
        listOfRecipes = passedArrayList;
        notifyDataSetChanged();
    }
}
