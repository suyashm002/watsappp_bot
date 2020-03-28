package com.suyash.autoreply.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PlatformsList implements Parcelable {

      String platformName;
      String autoMessage;

    protected PlatformsList(Parcel in) {
        platformName = in.readString();
        autoMessage = in.readString();
    }

    public PlatformsList(){}

    public static final Creator<PlatformsList> CREATOR = new Creator<PlatformsList>() {
        @Override
        public PlatformsList createFromParcel(Parcel in) {
            return new PlatformsList(in);
        }

        @Override
        public PlatformsList[] newArray(int size) {
            return new PlatformsList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(platformName);
        dest.writeString(autoMessage);
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAutoMessage() {
        return autoMessage;
    }

    public void setAutoMessage(String autoMessage) {
        this.autoMessage = autoMessage;
    }
}
