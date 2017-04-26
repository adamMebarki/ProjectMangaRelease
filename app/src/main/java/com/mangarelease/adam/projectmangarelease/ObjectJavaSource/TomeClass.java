package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;

/**
 * Created by Adam on 19/03/2017.
 *  Class contains all of the information of a tome
 * Use to retrieve and add to the db.
 * Use to save the information get from the xml
 * Use to show information on a manga in a list or a activity
 * Contains informations of the manga that the tomes belongs to it. (titleManga, Category)
 * Purpose : avoid many manipulation of datas (example create mangaclass during the xml parsage, ... )
 *
 */

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TomeClass implements Comparable<TomeClass>,Serializable{

    private String titleManga; // title of the manga of the tome retrieve from the parsage
    private int manga_id; // id of the manga, the tome belongs to it
    private String num_vol; // numero of the tome
    private String desc;  // description/resume of the tome
    private String image; // url of the picture of the tome
    private int tome_id; // id of the tome in the table Tomes of the db
    private int isBuy;   // int use to know if the tome is buy or not buy the user
    private String category;  // category of the manga retrieve from the parsage

    public TomeClass() {
        this.image = "";
        this.titleManga = "";
        this.desc = "";
        this.category = "";
        this.isBuy = 0;
    }


    /**
     * Constructor of TomeClass use in the parsage. One of the most important part of the project
     * Many operations are made to recup all data it need for the application
     *
     * @param title         String title of the manga composition : "title_manga - T[0-9][0-9].." retrieve fromt the tag of the xml
     * @param description   String description retrieve from the tag of the xml composition : HTML code with link of the picture
     *                      and resume of the tome
     * @param category      String category of the manga will be save here. Given to a mangaClass if the manga is created.
     */
    public TomeClass(String title, String description, String category) {
        this.image = this.retrievePicture(description);
        // take only the resume of the tome between the tag <p></p>
        description = description.substring(description.indexOf("<p>"), description.indexOf("</p>"));
        description = description.replaceAll("<[^>]*>", "");
        this.desc = description;
        // Separation of the title of the manga and the number of the tome
        Pattern p = Pattern.compile("T[0-9]+");
        Matcher m = p.matcher(title);
        // replace empty space. Standardize the title for the db and better manipulation
        if (m.find()) {
            title = title.replace(m.group(), "");
            title = title.replace("  ", " ");
            this.titleManga = title.replace("-", "");
            this.titleManga = titleManga.trim().toUpperCase();
            this.num_vol = m.group();
        } else {
            this.titleManga = title.trim().toUpperCase();
        }
        if (this.titleManga.contains("'"))
            this.titleManga = this.titleManga.replaceAll("'", "''");
        this.category = category;
        this.isBuy = 0; // buy default the tome is not buy

    }

    /**
     * Split the String desc to retrieve only the url of the picture of the tome
     * @param desc  String description retrieve from the xml.
     * @return  String url of the picture of the tome
     */
    private String retrievePicture(String desc) {
        String urlpict = desc.substring(desc.indexOf("https"));
        return urlpict.substring(0, urlpict.indexOf("\" alt"));
    }


    @Override
    public int compareTo(@NonNull TomeClass tome) {
        int num1 = Integer.parseInt(num_vol.replaceAll("T", ""));
        int num2 = Integer.parseInt(tome.getNum_vol().replaceAll("T", ""));
        if (num1 > num2)
            return 1;
        else
            return -1;
    }

    /**
     * Getter and Setter of the TomeClass
     *
     */

    public String getTitleManga() {
        return titleManga;
    }

    public void setNum_vol(String num_vol) {
        this.num_vol = num_vol;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTome_id() {
        return tome_id;
    }

    public void setTome_id(int tome_id) {
        this.tome_id = tome_id;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public String getNum_vol() {
        return num_vol;
    }

    public int getManga_id() {
        return manga_id;
    }

    public void setManga_id(int manga_id) {
        this.manga_id = manga_id;
    }

    public String getCategory() {
        return category;
    }

    public int getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(int ib) {
        this.isBuy = ib;
    }


}
