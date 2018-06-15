package com.example.pavol.bakingdiary;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.pavol.bakingdiary.customAdapters.RecipeListAdapter;
import com.example.pavol.bakingdiary.customObjects.IngredientObject;
import com.example.pavol.bakingdiary.customObjects.RecipeObject;
import com.example.pavol.bakingdiary.fragments.IngredientsFragment;
import com.example.pavol.bakingdiary.fragments.MasterFragment;
import com.example.pavol.bakingdiary.fragments.StepsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.OnItemClickListener,
        MasterFragment.onLoadingFinishedListener {


    public static final String PASSED_OBJECT_KEY = "passed_object";
    private static final String SPACE = " ";
    private static final String COLON = ":" + SPACE;
    private static final String NEW_LINE = "\n";

    private Boolean isTablet;


    @BindView(R.id.main_linear_layout)
    LinearLayout mainLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String layoutTag = mainLinearLayout.getTag().toString();

        if (layoutTag.equals(getResources().getString(R.string.tablet_landscape_tag)) || layoutTag.equals(getResources().getString(R.string.tablet_portrait_tag))) {
            isTablet = true;

            if (savedInstanceState == null) {

                FragmentManager fragmentManager = getSupportFragmentManager();

                StepsFragment stepsFragment = new StepsFragment();
                stepsFragment.setStepsRecipe(null);
                fragmentManager.beginTransaction()
                        .add(R.id.steps_fragment_frame_layout, stepsFragment)
                        .commit();

                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                ingredientsFragment.setIngredientsRecipe(null);
                fragmentManager.beginTransaction()
                        .add(R.id.ingredients_fragment_frame_layout, ingredientsFragment)
                        .commit();
            }


        } else {

            isTablet = false;
        }
    }

    @Override
    public void onItemClicked(RecipeObject passedObject) {


        String ingredientsString = createString(passedObject.mListOfIngredients);

        saveToPreferences(passedObject.mName, ingredientsString);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingDiaryWidgetProvider.class));
        BakingDiaryWidgetProvider.updateBakingDiaryWidgets(this, appWidgetManager,
                appWidgetIds);


        if (isTablet) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setStepsRecipe(passedObject);
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_fragment_frame_layout, stepsFragment)
                    .commit();

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setIngredientsRecipe(passedObject);
            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_fragment_frame_layout, ingredientsFragment)
                    .commit();
        } else {

            Intent openIngredientsActivity = new Intent(this, IngredientsActivity.class);
            openIngredientsActivity.putExtra(PASSED_OBJECT_KEY, passedObject);
            startActivity(openIngredientsActivity);
        }


    }

    private String createString(ArrayList<IngredientObject> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            String iName = list.get(i).mIngredient;
            String iAmount = list.get(i).mIngredientQuantity;
            String iUnit = list.get(i).mIngredientMeasureUnit;

            stringBuilder.append(iName);
            stringBuilder.append(COLON);
            stringBuilder.append(iAmount);
            stringBuilder.append(SPACE);
            stringBuilder.append(iUnit);
            stringBuilder.append(NEW_LINE);
        }

        return stringBuilder.toString();
    }

    private void saveToPreferences(String recipeName, String ingredients) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.recipe_name_key), recipeName);
        editor.putString(getString(R.string.ingredient_list_key), ingredients);
        editor.apply();
    }

    @Override
    public void onLoadingFinished(RecipeObject firstRecipe) {
        if (isTablet) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setStepsRecipe(firstRecipe);
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_fragment_frame_layout, stepsFragment)
                    .commit();

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setIngredientsRecipe(firstRecipe);
            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_fragment_frame_layout, ingredientsFragment)
                    .commit();
        }

    }

}
