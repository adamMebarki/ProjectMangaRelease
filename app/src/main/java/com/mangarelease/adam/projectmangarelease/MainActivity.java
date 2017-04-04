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
        quitBut = (Button) findViewById(R.id.quitButMain);
        releaseBut = (Button) findViewById(R.id.releaseButMain);
        libraryBut = (Button) findViewById(R.id.libraryButMain);
        quitBut.setOnClickListener(this);
        releaseBut.setOnClickListener(this);
        libraryBut.setOnClickListener(this);
        db.getInstance(getApplicationContext());
        fav.getInstance();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v.getId() == releaseBut.getId()) {
            Intent in = new Intent(this, ReleaseActivity.class);
            startActivity(in);
        } else if (v.getId() == libraryBut.getId()) {
            Intent in = new Intent(this, LibraryActivity.class);
            startActivity(in);
        } else {
            this.finish();
        }
    }
}
