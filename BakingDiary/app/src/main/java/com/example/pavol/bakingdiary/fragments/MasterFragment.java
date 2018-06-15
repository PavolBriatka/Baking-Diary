package com.example.pavol.bakingdiary.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pavol.bakingdiary.R;
import com.example.pavol.bakingdiary.customAdapters.RecipeListAdapter;
import com.example.pavol.bakingdiary.customObjects.IngredientObject;
import com.example.pavol.bakingdiary.customObjects.RecipeObject;
import com.example.pavol.bakingdiary.customObjects.StepObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MasterFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<RecipeObject>> {

    public static final int LOADER_ID = 27;
    public static final String JSON_URL_PATH =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static final String PARCELABLE_ARRAY_KEY = "parcelable_array";

    private RecipeListAdapter adapter;
    onLoadingFinishedListener mListener;
    private ArrayList<RecipeObject> recipeArray;

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    public interface onLoadingFinishedListener {
        void onLoadingFinished(RecipeObject firstRecipe);
    }

    public MasterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        ButterKnife.bind(this, rootView);

        mListener = (onLoadingFinishedListener) getContext();

        String layoutTag = recyclerView.getTag().toString();

        if (layoutTag.equals(getResources().getString(R.string.tablet_portrait_tag)) ||
                layoutTag.equals(getResources().getString(R.string.phone_landscape_tag))) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }

        if (savedInstanceState != null) {
            recipeArray = savedInstanceState.getParcelableArrayList(PARCELABLE_ARRAY_KEY);
            adapter = new RecipeListAdapter(recipeArray, getContext());
            recyclerView.setAdapter(adapter);
        } else {

            adapter = new RecipeListAdapter(null, getContext());

            recyclerView.setAdapter(adapter);


            getLoaderManager().initLoader(LOADER_ID, null, this);
        }

        return rootView;
    }

    @Override
    public Loader<ArrayList<RecipeObject>> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<RecipeObject>>(getContext()) {

            ArrayList<RecipeObject> receivedRecipeObjectArray = null;

            @Override
            protected void onStartLoading() {

                if (receivedRecipeObjectArray != null) {
                    deliverResult(receivedRecipeObjectArray);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<RecipeObject> loadInBackground() {
                URL jsonUrl;
                String jsonResponse = "";

                jsonUrl = createUrl(JSON_URL_PATH);
                try {
                    jsonResponse = makeHttpRequest(jsonUrl);

                    ArrayList<RecipeObject> arrayOfRecipes = getArrayOfRecipes(jsonResponse);

                    return arrayOfRecipes;

                } catch (IOException exception) {
                    exception.printStackTrace();
                    return null;
                }
            }

            private URL createUrl(String urlString) {
                URL url = null;
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException exception) {
                    Log.e("Runtime error:", "Error with creating URL", exception);
                    return null;
                }
                return url;
            }

            private String makeHttpRequest(URL url) throws IOException {
                String jsonResponse = "";
                if (url == null) {
                    return jsonResponse;
                }
                HttpURLConnection urlConnection = null;
                InputStream inputStream = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000 /* milliseconds */);
                    urlConnection.setConnectTimeout(15000 /* milliseconds */);
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        inputStream = urlConnection.getInputStream();
                        jsonResponse = readFromStream(inputStream);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (inputStream != null) {
                        // function must handle java.io.IOException here
                        inputStream.close();
                    }
                }
                return jsonResponse;
            }

            private String readFromStream(InputStream inputStream) throws IOException {
                StringBuilder output = new StringBuilder();
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    String line = reader.readLine();
                    while (line != null) {
                        output.append(line);
                        line = reader.readLine();
                    }
                }
                return output.toString();
            }

            private ArrayList<RecipeObject> getArrayOfRecipes(String jsonResponse) {

                ArrayList<RecipeObject> mArrayOfRecipes = new ArrayList<>();

                if (TextUtils.isEmpty(jsonResponse)) {
                    return null;
                }

                try {
                    JSONArray mainArray = new JSONArray(jsonResponse);
                    for (int i = 0; i < mainArray.length(); i++) {
                        JSONObject singleRecipe = mainArray.getJSONObject(i);
                        String recipeName = singleRecipe.optString("name");

                        JSONArray ingredientsArray = singleRecipe.getJSONArray("ingredients");
                        ArrayList<IngredientObject> mListOfIngredients = new ArrayList<>();
                        for (int j = 0; j < ingredientsArray.length(); j++) {
                            JSONObject singleIngredientData = ingredientsArray.getJSONObject(j);
                            String ingredientQuantity = singleIngredientData.optString("quantity");
                            String ingredientMeasureUnit = singleIngredientData.optString("measure");
                            String ingredientName = singleIngredientData.optString("ingredient");
                            mListOfIngredients.add(new IngredientObject(ingredientQuantity, ingredientMeasureUnit, ingredientName));
                        }

                        JSONArray stepsArray = singleRecipe.getJSONArray("steps");
                        ArrayList<StepObject> mListOfSteps = new ArrayList<>();
                        for (int k = 0; k < stepsArray.length(); k++) {
                            JSONObject singleStepData = stepsArray.getJSONObject(k);
                            String shortDescription = singleStepData.optString("shortDescription");
                            String description = singleStepData.optString("description");
                            String videoUrl = singleStepData.optString("videoURL");
                            String thumbnailUrl = singleStepData.optString("thumbnailURL");
                            mListOfSteps.add(new StepObject(shortDescription, description, videoUrl, thumbnailUrl));
                        }
                        String servings = singleRecipe.optString("servings");
                        String image = singleRecipe.optString("image");

                        mArrayOfRecipes.add(new RecipeObject(recipeName, mListOfIngredients, mListOfSteps, servings, image));
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                }
                return mArrayOfRecipes;
            }


            public void deliverResult(ArrayList<RecipeObject> data) {
                receivedRecipeObjectArray = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<RecipeObject>> loader, ArrayList<RecipeObject> recipeObjectArray) {
        adapter.setMasterAdapterData(recipeObjectArray);
        recyclerView.setAdapter(adapter);
        mListener.onLoadingFinished(recipeObjectArray.get(0));
        recipeArray = recipeObjectArray;

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<RecipeObject>> loader) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(PARCELABLE_ARRAY_KEY, recipeArray);
        super.onSaveInstanceState(outState);
    }
}
