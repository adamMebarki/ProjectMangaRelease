package com.mangarelease.adam.projectmangarelease;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.FavoriteArray;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button quitBut, releaseBut, libraryBut;
    private SqLiteHelper db;
    private FavoriteArray fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instanciation of the three buttons of the main Activity
        quitBut = (Button) findViewById(R.id.quitButMain);
        releaseBut = (Button) findViewById(R.id.releaseButMain);
        libraryBut = (Button) findViewById(R.id.libraryButMain);
        quitBut.setOnClickListener(this);
        releaseBut.setOnClickListener(this);
        libraryBut.setOnClickListener(this);
        // Create the instance of the db which will be share in the whole project
        db.getInstance(getApplicationContext());
        // If no create, will create the row for this. It will be the row by default for every new favorite
        // if the author is not specified.
        db.getInstance(getApplicationContext()).createAuthor("No Specify");

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v.getId() == releaseBut.getId()) { // open the release activity with the new releases of manga
            Intent in = new Intent(this, ReleaseActivity.class);
            startActivity(in);
        } else if (v.getId() == libraryBut.getId()) { // open the library activity to manage the favorites manga
            Intent in = new Intent(this, LibraryActivity.class);
            startActivity(in);
        } else {
            this.finish();
        }
    }
}
