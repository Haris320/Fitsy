package com.example.fitsy;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchStuff implements Parcelable{

    private String image_url;
    private String title;
    private String id;

    public SearchStuff(String image_url, String title, String id){
        this.image_url = image_url;
        this.title = title;
        this.id = id;
    }

    protected SearchStuff(Parcel in){
        title = in.readString();
        image_url = in.readString();
        id = in.readString();
    }

    public static final Parcelable.Creator<SearchStuff> CREATOR = new Parcelable.Creator<SearchStuff>() {
        @Override
        public SearchStuff createFromParcel(Parcel in) {
            return new SearchStuff(in);
        }

        @Override
        public SearchStuff[] newArray(int size) {
            return new SearchStuff[size];
        }
    };


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(image_url);
        dest.writeString(id);
    }
}
