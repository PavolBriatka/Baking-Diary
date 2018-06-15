package com.example.pavol.bakingdiary;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class IngredientsActivityIntentTest {

    private static final String PASSED_RECIPE_KEY = "passed_recipe";


    @Rule
    public IntentsTestRule<IngredientsActivity> mActivityRule = new IntentsTestRule<>(
            IngredientsActivity.class);

    @Before
    public void stubAllExternalIntents() {

        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickSeeInstructions_opensInstructions() {

        Espresso.onView(withId(R.id.steps_button_tv)).perform(click());

        intended(allOf(
                hasComponent(StepsActivity.class.getName()),
                hasExtraWithKey(PASSED_RECIPE_KEY)
        ));
    }


}
