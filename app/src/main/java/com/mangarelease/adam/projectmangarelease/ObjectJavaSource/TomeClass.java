package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;

/**
 * Created by Adam on 19/03/2017.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adam on 19/03/2017.
 */

public class TomeClass {

    private String titleManga;
    private int manga_id;
    private String num_vol;
    private String desc;
    private String image;
    private int tome_id;

    public TomeClass() {
        this.image = "";
        this.titleManga = "";
        this.desc = "";
        this.desc = "";
    }


    public TomeClass(String title, String description) {
        this.image = this.retrievePicture(description);
        description = description.substring(description.indexOf("<p>"), description.indexOf("</p>"));
        description = description.replaceAll("<[^>]*>", "");
        this.desc = description;
        Pattern p = Pattern.compile("T[0-9]+");
        Matcher m = p.matcher(title);
        if (m.find()) {
            title = title.replace(m.group(),"");
            this.titleManga = title.replace("-","") ;
            this.num_vol = m.group();

        } else {
            this.titleManga = title;
        }

    }

    public String getTitleManga() {
        return titleManga;
    }

    public void setTitleManga(String titleManga) {
        this.titleManga = titleManga;
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

    protected String retrievePicture(String desc) {
        String urlpict = desc.substring(desc.indexOf("https"));
        String img = urlpict.substring(0, urlpict.indexOf("\" alt"));
        return img;
    }
}
