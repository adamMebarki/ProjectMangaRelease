package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;

import java.util.ArrayList;

/**
 * Created by Adam on 29/03/2017.
 */

public class FavoriteArray {

    private ArrayList<MangaClass> favorites;

    private FavoriteArray() {
        favorites = new ArrayList<MangaClass>();
    }

    private static FavoriteArray instance;

    public static FavoriteArray getInstance() {
        if (instance == null) instance = new FavoriteArray();
        return instance;
    }

    public ArrayList getFavorites(){
        return favorites;
    }


}
