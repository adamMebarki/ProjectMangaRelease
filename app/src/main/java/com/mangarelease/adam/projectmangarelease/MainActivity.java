package com.mangarelease.adam.projectmangarelease;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.ParserClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button quitMainButton, releaseMainButton, libraryMainButton;
    private SqLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instanciation of the three buttons of the main Activity
        quitMainButton = (Button) findViewById(R.id.quitButMain);
        releaseMainButton = (Button) findViewById(R.id.releaseButMain);
        libraryMainButton = (Button) findViewById(R.id.libraryButMain);
        quitMainButton.setOnClickListener(this);
        releaseMainButton.setOnClickListener(this);
        libraryMainButton.setOnClickListener(this);
        // Create the instance of the db which will be share in the whole project
        db.getInstance(getApplicationContext());
        // If no create, will create the row for this. It will be the row by default for every new favorite
        // if the author is not specified.
        db.getInstance(getApplicationContext()).createAuthor("No Specify");

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v.getId() == releaseMainButton.getId()) { // open the release activity with the new releases of manga
            // Start the parsage to recup data in an asynctask class which launch at the end the releaseActivity
            ParserClass pars = new ParserClass(MainActivity.this);
            pars.execute("");

        } else if (v.getId() == libraryMainButton.getId()) { // open the library activity to manage the favorites manga
            Intent in = new Intent(this, LibraryActivity.class);
            startActivity(in);
        } else {
            this.finish();
        }
    }
}
