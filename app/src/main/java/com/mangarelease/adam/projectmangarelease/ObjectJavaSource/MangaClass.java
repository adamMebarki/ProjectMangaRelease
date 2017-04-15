package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Adam on 19/03/2017.
 * Class contains all of the information of a manga
 * Use to retrieve and add to the db.
 * Use to show information on a manga in a list or a activity
 * A manga is only created when the user add in the favorite list. Not before.
 */

public class MangaClass implements Parcelable {


    private int manga_id;  // id of the manga retrieve from the db once it is created
    private String title;  // title of the manga
    private double price;  // price 0.0 by default
    private String editor_name; //  name of the publishers none by default
    private String author_name; // name of the authors  none by default
    private int author_id;  // id of the author 1 by default if not provide during the parsage
    private String category;  // category of the manga
    private ArrayList<TomeClass> volumes;  // list of the volumes of the manga retrieve from the db

    /**
     * Constructor of MangaClass
     */
    public MangaClass() {
        this.manga_id=0; //
        this.title="";
        this.price = 0.0;
        this.editor_name = "";
        this.author_name = "";
        this.author_id = 1;
        this.category="";
        volumes = new ArrayList<>();
    }

    /**
     * Use to send the manga throught activity
     * @param in
     */
    private MangaClass(Parcel in) {
        this.manga_id = in.readInt();
        this.title = in.readString();
        this.price = in.readDouble();
        this.editor_name = in.readString();
        this.author_name = in.readString();
        this.author_id = in.readInt();
        this.category = in.readString();
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

    /**
     * Getter and Setter of all of the variables of MangaClass
     *
     */

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







}
