package com.example.pavol.bakingdiary.customObjects;

import android.os.Parcel;
import android.os.Parcelable;

public class StepObject implements Parcelable {
    public final String mShortDescription;
    public final String mDescription;
    public final String mVideoUrl;
    public final String mThumbnailUrl;

    public StepObject(String shortDescription, String description,
                      String videoUrl, String thumbnailUrl) {

        mShortDescription = shortDescription;
        mDescription = description;
        mVideoUrl = videoUrl;
        mThumbnailUrl = thumbnailUrl;
    }

    private StepObject(Parcel parcel) {
        mShortDescription = parcel.readString();
        mDescription = parcel.readString();
        mVideoUrl = parcel.readString();
        mThumbnailUrl = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mShortDescription);
        parcel.writeString(mDescription);
        parcel.writeString(mVideoUrl);
        parcel.writeString(mThumbnailUrl);
    }

    public static Parcelable.Creator<StepObject> CREATOR = new Parcelable.Creator<StepObject>() {
        @Override
        public StepObject createFromParcel(Parcel parcel) {
            return new StepObject(parcel);
        }

        @Override
        public StepObject[] newArray(int i) {
            return new StepObject[i];
        }
    };
}
