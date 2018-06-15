package com.example.pavol.bakingdiary.customObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RecipeObject implements Parcelable {

    public final String mName;
    public final ArrayList<IngredientObject> mListOfIngredients;
    public final ArrayList<StepObject> mListOfSteps;
    public final String mNumberOfServings;
    public final String mImage;

    public RecipeObject(String name, ArrayList<IngredientObject> listOfIngredients,
                        ArrayList<StepObject> listOfSteps, String numberOfServings, String image) {

        mName = name;
        mListOfIngredients = listOfIngredients;
        mListOfSteps = listOfSteps;
        mNumberOfServings = numberOfServings;
        mImage = image;
    }

    private RecipeObject(Parcel parcel) {
        mName = parcel.readString();
        mListOfIngredients = parcel.readArrayList(getClass().getClassLoader());
        mListOfSteps = parcel.readArrayList(getClass().getClassLoader());
        mNumberOfServings = parcel.readString();
        mImage = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeList(mListOfIngredients);
        parcel.writeList(mListOfSteps);
        parcel.writeString(mNumberOfServings);
        parcel.writeString(mImage);
    }

    public static Parcelable.Creator<RecipeObject> CREATOR = new Parcelable.Creator<RecipeObject>() {
        @Override
        public RecipeObject createFromParcel(Parcel parcel) {
            return new RecipeObject(parcel);
        }

        @Override
        public RecipeObject[] newArray(int i) {
            return new RecipeObject[i];
        }
    };
}
