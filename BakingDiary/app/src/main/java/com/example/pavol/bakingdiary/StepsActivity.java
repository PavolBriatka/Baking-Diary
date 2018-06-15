package com.example.pavol.bakingdiary;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.pavol.bakingdiary.customObjects.RecipeObject;
import com.example.pavol.bakingdiary.fragments.StepsFragment;

import butterknife.ButterKnife;

public class StepsActivity extends AppCompatActivity {

    private RecipeObject stepsRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps_activity);
        ButterKnife.bind(this);

        Intent passedObject = getIntent();
        stepsRecipe = passedObject.getParcelableExtra(IngredientsActivity.PASSED_RECIPE_KEY);

        if (savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setStepsRecipe(stepsRecipe);
            fragmentManager.beginTransaction()
                    .add(R.id.steps_fragment_frame_layout, stepsFragment)
                    .commit();
        }


    }
}
