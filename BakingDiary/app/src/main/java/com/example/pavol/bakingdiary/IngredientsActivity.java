package com.example.pavol.bakingdiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.pavol.bakingdiary.customObjects.RecipeObject;
import com.example.pavol.bakingdiary.fragments.IngredientsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {

    private RecipeObject IngredientsRecipe;
    public static final String PASSED_RECIPE_KEY = "passed_recipe";

    @BindView(R.id.steps_button_tv)
    TextView stepsButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_activity);
        ButterKnife.bind(this);

        Intent passedObject = getIntent();
        IngredientsRecipe = passedObject.getParcelableExtra(MainActivity.PASSED_OBJECT_KEY);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setIngredientsRecipe(IngredientsRecipe);
            fragmentManager.beginTransaction()
                    .add(R.id.ingredients_fragment_frame_layout, ingredientsFragment)
                    .commit();
        }

        stepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openStepsActivity = new Intent(getBaseContext(), StepsActivity.class);
                openStepsActivity.putExtra(PASSED_RECIPE_KEY, IngredientsRecipe);
                startActivity(openStepsActivity);

            }
        });
    }
}
