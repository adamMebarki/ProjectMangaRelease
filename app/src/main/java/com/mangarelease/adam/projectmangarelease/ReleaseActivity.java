package com.mangarelease.adam.projectmangarelease;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.mangarelease.adam.projectmangarelease.NewReleaseSource.ContentReleaseSource.ReleaseFragment;
import com.mangarelease.adam.projectmangarelease.NewReleaseSource.FilterSearchSource.ExpandableListAdapter;
import com.mangarelease.adam.projectmangarelease.NewReleaseSource.FilterSearchSource.SlidingLayout;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.ParserClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.TomeClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener {
    // The SlidingLayout which will hold both the sliding menu and our main content
    // Main content will holds our Fragment respectively
    SlidingLayout slidingLayout;


    /* Declaration Menu Filter Search variables */
    private Toolbar toolbar;
    private ImageView butMenu; // Menu button
    private Button val;
    /* Declaration Expendable component **/
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expandableList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    SqLiteHelper db;
    /*Temporary */
    private int NUM_PAGES = 5;

    /* Declaration of the content of the releasePart the parser and the fragment which will content the webview*/
    private ParserClass pars;
    private ReleaseFragment fragment;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the mainLayout
        setContentView(R.layout.activity_release);


        // instanciation of variables of Menu Filter Search
        slidingLayout = (SlidingLayout) findViewById(R.id.sliding_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // get the listview
        expandableList = (ExpandableListView) findViewById(R.id.expandableList);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expandableList.setAdapter(listAdapter);
        // handling menu button event
        butMenu = (ImageView) findViewById(R.id.menu_icon);
        butMenu.setOnClickListener(this);
        val = (Button) findViewById(R.id.validateButRel);
        val.setOnClickListener(this);


        // Parsager of the webservice and create an arraylist of elements retrieve.
        pars = new ParserClass();
        pars.execute("");
        while (pars.parsingComplete) ;

        AddTomeFavorite();
        //Instantiate the content of Release Activity
        NUM_PAGES = pars.getTomes().size();
        // Replace fragment main when activity start
        FragmentManager fm = ReleaseActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment = new ReleaseFragment((ArrayList<TomeClass>) pars.getTomes());
        ft.add(R.id.release_fragment_content, fragment);
        ft.commit();

        this.AddTomeFavorite();

    }


    /*
  * Preparing the list data Temporary
  */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Manga");
        listDataHeader.add("Categories");

        // Adding child data
        List<String> manga = new ArrayList<String>();
        manga.add("Favorites");
        manga.add("Others");


        List<String> category = new ArrayList<String>();
        category.add("Shonen");
        category.add("Shojo");
        category.add("Seinen");
        category.add("Humour");


        listDataChild.put(listDataHeader.get(0), manga); // Header, Child data
        listDataChild.put(listDataHeader.get(1), category);

    }


    public void toggleMenu(View v) {
        slidingLayout.toggleMenu();
    }

    @Override
    public void onBackPressed() {
        if (slidingLayout.isMenuShown()) {
            slidingLayout.toggleMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == val.getId()) {
            this.toggleMenu(v);

        }

        if (v.getId() == butMenu.getId()) {
            this.toggleMenu(v);
        }


    }


    public void AddTomeFavorite() {

        ArrayList<MangaClass> arrayManga = new ArrayList<>();
        arrayManga.addAll(db.getInstance(this).getAllMangas());
        if (!arrayManga.isEmpty()) {
            for (int i = 0; i < arrayManga.size(); i++) {
                String title = arrayManga.get(i).getTitle();
                Log.d("Manga Release : ", arrayManga.get(i).getTitle() + "    ");
                if (db.getInstance(this).isFollow(arrayManga.get(i).getManga_id()) == 1) { // if manga follow add automatically new tomes
                    for(int j=0;j<pars.getTomes().size();j++){
                        if(pars.getTomes().get(j).getTitleManga().compareTo(title)==0){
                           TomeClass tome = new TomeClass();
                            tome.setNum_vol(pars.getTomes().get(j).getNum_vol());
                            tome.setDesc(pars.getTomes().get(j).getDesc());
                            tome.setImage(pars.getTomes().get(j).getImage());
                            tome.setManga_id(arrayManga.get(i).getManga_id());
                            db.getInstance(this).createTome(tome,arrayManga.get(i).getManga_id());
                        }

                    }
                }
            }
        }
    }


}
