package com.example.pavol.bakingdiary.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pavol.bakingdiary.R;
import com.example.pavol.bakingdiary.customAdapters.IngredientListAdapter;
import com.example.pavol.bakingdiary.customObjects.IngredientObject;
import com.example.pavol.bakingdiary.customObjects.RecipeObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    private static final String RECIPE_SAVE_KEY = "saved_recipe";

    private RecipeObject ingredientsRecipe;
    private ArrayList<IngredientObject> ingredientList;

    @BindView(R.id.ingredients_page_recycler_view)
    RecyclerView detailPageRecyclerView;


    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            ingredientsRecipe = savedInstanceState.getParcelable(RECIPE_SAVE_KEY);
        }

        View ingredientView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, ingredientView);

        if (ingredientsRecipe == null) {
            ingredientList = new ArrayList<>();
            ingredientList.add(new IngredientObject("", "", "No recipe chosen."));

            detailPageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));

            IngredientListAdapter adapter = new IngredientListAdapter(ingredientList);
            detailPageRecyclerView.setAdapter(adapter);

        } else {
            getActivity().setTitle(ingredientsRecipe.mName);
            ingredientList = ingredientsRecipe.mListOfIngredients;

            String layoutTag = detailPageRecyclerView.getTag().toString();

            if (layoutTag.equals(getResources().getString(R.string.tablet_portrait_tag)) ||
                    layoutTag.equals(getResources().getString(R.string.phone_landscape_tag))) {
                detailPageRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            } else {
                detailPageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

            IngredientListAdapter adapter = new IngredientListAdapter(ingredientList);
            detailPageRecyclerView.setAdapter(adapter);
        }

        return ingredientView;
    }

    public void setIngredientsRecipe(RecipeObject passedRecipe) {
        ingredientsRecipe = passedRecipe;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_SAVE_KEY, ingredientsRecipe);
    }
}
