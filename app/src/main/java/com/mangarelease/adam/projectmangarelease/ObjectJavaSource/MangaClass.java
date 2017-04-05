package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Adam on 19/03/2017.
 */

public class MangaClass implements Parcelable {


    private int manga_id;
    private String title;
    private double price;
    private String editor_name;
    private String author_name;
    private int author_id;
    private String category;
    private ArrayList<TomeClass> volumes;


    public MangaClass() {
        this.manga_id=1; // no author name by default
        this.title="";
        this.price = 0.0;
        this.editor_name = "";
        this.author_name = "";
        this.author_id = 1;
        this.category="";
        volumes = new ArrayList<>();
    }


    private MangaClass(Parcel in) {
        this.manga_id = in.readInt();
        this.title = in.readString();
        this.price = in.readDouble();
        this.editor_name = in.readString();
        this.author_name = in.readString();
        this.author_id = in.readInt();
        this.category = in.readString();
    }

    public int getManga_id() {
        return manga_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public void setManga_id(int manga_id) {
        this.manga_id = manga_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.toUpperCase();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getEditor_name() {
        return editor_name;
    }

    public void setEditor_name(String editor_name) {
        this.editor_name = editor_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<TomeClass> getVolumes() {
        return volumes;
    }

    public void setVolumes(ArrayList<TomeClass> volumes) {
        this.volumes.clear();
        this.volumes.addAll(volumes);
    }

    public void addVolume(TomeClass tome) {
        volumes.add(tome);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.manga_id);
        dest.writeString(this.title);
        dest.writeDouble(this.price);
        dest.writeString(this.editor_name);
        dest.writeString(this.author_name);
        dest.writeInt(this.author_id);
        dest.writeString(this.category);
    }



    public static final Parcelable.Creator<MangaClass> CREATOR = new Parcelable.Creator<MangaClass>() {
        public MangaClass createFromParcel(Parcel in) {
            return new MangaClass(in);
        }

        public MangaClass[] newArray(int size) {
            return new MangaClass[size];

        }
    };
}
