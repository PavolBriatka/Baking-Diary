package com.example.pavol.bakingdiary.customObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientObject implements Parcelable {

    public final String mIngredientQuantity;
    public final String mIngredientMeasureUnit;
    public final String mIngredient;

    public IngredientObject(String ingredientQuantity, String ingredientMeasureUnit,
                            String ingredient) {
        mIngredientQuantity = ingredientQuantity;
        mIngredientMeasureUnit = ingredientMeasureUnit;
        mIngredient = ingredient;
    }

    private IngredientObject(Parcel parcel) {
        mIngredientQuantity = parcel.readString();
        mIngredientMeasureUnit = parcel.readString();
        mIngredient = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mIngredientQuantity);
        parcel.writeString(mIngredientMeasureUnit);
        parcel.writeString(mIngredient);
    }

    public static Parcelable.Creator<IngredientObject> CREATOR = new Parcelable.Creator<IngredientObject>() {
        @Override
        public IngredientObject createFromParcel(Parcel parcel) {
            return new IngredientObject(parcel);
        }

        @Override
        public IngredientObject[] newArray(int i) {
            return new IngredientObject[i];
        }
    };
}
