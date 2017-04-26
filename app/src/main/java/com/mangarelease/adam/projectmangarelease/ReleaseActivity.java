package com.mangarelease.adam.projectmangarelease;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.mangarelease.adam.projectmangarelease.NewReleaseSource.ContentReleaseSource.ReleaseFragment;
import com.mangarelease.adam.projectmangarelease.NewReleaseSource.ContentReleaseSource.EmptyFragment;
import com.mangarelease.adam.projectmangarelease.NewReleaseSource.FilterSearchSource.ExpandableListAdapter;
import com.mangarelease.adam.projectmangarelease.NewReleaseSource.FilterSearchSource.SlidingLayout;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.TomeClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ReleaseActivity extends AppCompatActivity implements View.OnClickListener, ExpandableListView.OnChildClickListener {
    // The SlidingLayout which will hold both the sliding menu and our main content
    // Main content will holds our Fragment respectively
    private SlidingLayout slidingLayout;


    private ImageView menuRelButton; // Menu button on the top left of the screen
    private Button validateFilterButton; // button to validate the filter.

    /* Declaration Expendable component **/
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expandableListFilter;
    private List<String> listDataHeader; //
    private HashMap<String, List<String>> listDataChild;


    /* Declaration of the content of the releasePart the parser and the fragment which will content the webview*/
    private Fragment fragment;
    private FragmentTransaction ft;
    private FragmentManager fm;
    private List<TomeClass> tomes;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the mainLayout
        setContentView(R.layout.activity_release);


        // instanciation of variables of Menu Filter Search
        slidingLayout = (SlidingLayout) findViewById(R.id.sliding_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // get the listview
        expandableListFilter = (ExpandableListView) findViewById(R.id.expandableList);
        // preparing list data of the filter Search
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expandableListFilter.setAdapter(listAdapter);
        // Listview on child click listener
        expandableListFilter.setOnChildClickListener(this);

        // handling menu button event
        menuRelButton = (ImageView) findViewById(R.id.menu_icon);
        menuRelButton.setOnClickListener(this);
        validateFilterButton = (Button) findViewById(R.id.validateButRel);
        validateFilterButton.setOnClickListener(this);

        // Recup of the list of new Releases tomes
        Intent i = getIntent();
        tomes = (List<TomeClass>) i.getSerializableExtra("sampleObject");

        //Instantiate the content of Release Activity blank or with the list of new tomes
        // Replace fragment main when activity start
        fm = ReleaseActivity.this.getSupportFragmentManager();
        ft = fm.beginTransaction();
        if (tomes.isEmpty()) {
            fragment = new EmptyFragment("Problem of connection, please try again later.");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            fragment = new ReleaseFragment((ArrayList<TomeClass>) tomes);
        }
        //
        ft.add(R.id.release_fragment_content, fragment);
        ft.commit();
        AddTomeFavorite();
    }


    /**
     * Preparing the list data of the Filter Search
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding Child Header data
        listDataHeader.add("Manga");
        listDataHeader.add("Categories");

        // Adding Child data
        List<String> manga = new ArrayList<>();
        manga.add("Favorites");
        manga.add("Others");
        List<String> category = new ArrayList<>();
        category.add("Shonen");
        category.add("Shojo");
        category.add("Seinen");
        category.add("Humour");


        listDataChild.put(listDataHeader.get(0), manga); // Header, Child data
        listDataChild.put(listDataHeader.get(1), category);

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
        // If user click on the validate button of the filter Search Menu close it and
        // Run the filter algorithm and replace the fragment by a new with the filter result
        if (v.getId() == validateFilterButton.getId()) {
            slidingLayout.toggleMenu();
            // call the method to filter the releases with th information of filter provide by the user
            ArrayList<TomeClass> filterAr = FilterSearch((ArrayList<TomeClass>) tomes, listAdapter.stateCategory, listAdapter.stateManga);
            if (!filterAr.isEmpty()) {
                ft = fm.beginTransaction();
                fragment = new ReleaseFragment(filterAr);
                ft.replace(R.id.release_fragment_content, fragment);
                ft.commit();
            } else { // if no result show an empty fragment
                ft = fm.beginTransaction();
                fragment = new EmptyFragment("Sorry your search returned no results.");
                ft.replace(R.id.release_fragment_content, fragment);
                ft.commit();
            }
        }

        if (v.getId() == menuRelButton.getId()) {
            //this.toggleMenu(v);
            slidingLayout.toggleMenu();
        }
    }

    /**
     * When a manga is already in the database and it is checked as a favorite/follow, it will add automatically
     * the new tomes of this favorite manga in the database. Just after the parsage.
     */
    private void AddTomeFavorite() {
        SqLiteHelper db = null;
        ArrayList<MangaClass> arrayManga = new ArrayList<>();
        arrayManga.addAll(SqLiteHelper.getInstance(this).getAllMangas());
        if (!arrayManga.isEmpty()) {
            // See all of manga if they are favorite/follow or not
            for (int i = 0; i < arrayManga.size(); i++) {
                String title = arrayManga.get(i).getTitle();
                // if manga is followed, add automatically new tomes retrieve from the parsage
                if (SqLiteHelper.getInstance(this).isFollow(arrayManga.get(i).getManga_id()) == 1) {
                    for (int j = 0; j < tomes.size(); j++) {
                        // If title of the manga from the tome is the same, create the tome in the database.
                        if (tomes.get(j).getTitleManga().compareTo(title) == 0) {
                            TomeClass tome = new TomeClass();
                            tome.setNum_vol(tomes.get(j).getNum_vol());
                            tome.setDesc(tomes.get(j).getDesc());
                            tome.setImage(tomes.get(j).getImage());
                            tome.setManga_id(arrayManga.get(i).getManga_id());
                            SqLiteHelper.getInstance(this).createTome(tome, arrayManga.get(i).getManga_id());
                        }

                    }
                }
            }
        }
    }

    /**
     * @param tomes       Contains the new releases retrieve from the parsage and after it contains
     *                    the list of tome which are filtering by the user to filter again and again
     * @param tabCategory Contains informations about which checkbox is checked or not for the category section
     * @param tabManga    Contains informations about which checkbox is checked or not for the Manga section
     * @return ArrayList<TomeClass> return the new filter ArrayList which will be use by the fragment
     */
    private ArrayList<TomeClass> FilterSearch(ArrayList<TomeClass> tomes, int[] tabCategory, int[] tabManga) {
        ArrayList<TomeClass> arrayFilter = new ArrayList<>();
        SqLiteHelper db = null;

        if (tabManga[0] == 1) {  // checkbox favorites checked == true
            for (int i = 0; i < tomes.size(); i++) {
                if (SqLiteHelper.getInstance(getApplicationContext()).getManga_id(tomes.get(i).getTitleManga()) != 0) { // Manga is favorite
                    arrayFilter.add(tomes.get(i));
                }
            }
        }
        if (tabManga[1] == 1) { // checkbox Others checked == true
            for (int i = 0; i < tomes.size(); i++) {
                if (SqLiteHelper.getInstance(getApplicationContext()).getManga_id(tomes.get(i).getTitleManga()) == 0) { // Manga is not favorite
                    arrayFilter.add(tomes.get(i));
                }
            }
        }
        // For the four categories if not checkbox are not checked, delete  tomes of the categories unckeck by the user
        if (tabCategory[0] == 0) { //Shonen checkbox checked == false
            Iterator<TomeClass> iter = arrayFilter.iterator();
            while (iter.hasNext()) {
                TomeClass tome = iter.next();
                if (tome.getCategory().compareTo("Shonen") == 0) {
                    iter.remove();
                }
            }
        }
        if (tabCategory[1] == 0) { // Shojo checkbox checked == false
            Iterator<TomeClass> iter = arrayFilter.iterator();
            while (iter.hasNext()) {
                TomeClass tome = iter.next();
                if (tome.getCategory().compareTo("Shojo") == 0) {
                    iter.remove();
                }
            }
        }
        if (tabCategory[2] == 0) { // Seinen checkbox checked == false
            Iterator<TomeClass> iter = arrayFilter.iterator();
            while (iter.hasNext()) {
                TomeClass tome = iter.next();
                if (tome.getCategory().compareTo("Seinen") == 0) {
                    iter.remove();
                }
            }
        }
        if (tabCategory[3] == 0) { // Humour checkbox checked == false
            Iterator<TomeClass> iter = arrayFilter.iterator();
            while (iter.hasNext()) {
                TomeClass tome = iter.next();
                if (tome.getCategory().compareTo("Humour") == 0) {
                    iter.remove();
                }
            }
        }
        return arrayFilter;
    }


    @Override
    // Recup the checkbox clicked on the expandableListFilter of the filter Search Menu
    // Save the state of the checkbox checked == true == 1 || checked == false == 0
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        CheckBox cb = listAdapter.getCheckBox(groupPosition, childPosition, v);
        if (cb.isChecked()) {
            if (groupPosition == 1) // Manga Section
                listAdapter.stateCategory[childPosition] = 0;
            if (groupPosition == 0) // Category Section
                listAdapter.stateManga[childPosition] = 0;
            cb.setChecked(false);
        } else {
            if (groupPosition == 1)
                listAdapter.stateCategory[childPosition] = 1;
            if (groupPosition == 0)
                listAdapter.stateManga[childPosition] = 1;
            cb.setChecked(true);
        }
        return true;
    }
}