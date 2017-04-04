package com.mangarelease.adam.projectmangarelease;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;

/**
 * Created by Adam on 15/03/2017.
 */

public class MangaActivity extends AppCompatActivity implements View.OnClickListener {

    private Button okayBut, cancelBut, valBut;
    private EditText text_author,text_category,text_price,text_editor;
    private ImageButton editBut;
    private TextView title;
    private MangaClass manga;
    private SqLiteHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);
        title = (TextView) findViewById(R.id.mangaTitle);
        okayBut = (Button) findViewById(R.id.mangaOk);
        editBut = (ImageButton) findViewById(R.id.editBut);
        cancelBut = (Button) findViewById(R.id.cancelEditBut);
        valBut = (Button) findViewById(R.id.valEditBut);
        String text = getIntent().getStringExtra("title");
        title.setText(text);
        manga = db.getInstance(getApplicationContext()).getManga(text);
        okayBut.setOnClickListener(this);
        editBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);
        valBut.setOnClickListener(this);

        text_author = (EditText) findViewById(R.id.text_author);
        text_editor = (EditText) findViewById(R.id.text_editor);
        text_category = (EditText) findViewById(R.id.text_category);
        text_price = (EditText) findViewById(R.id.text_price);
    }

    @Override
    public void onClick(View v) {

         switch (v.getId()){
             case R.id.editBut:
                 editBut.setVisibility(View.INVISIBLE);
                 okayBut.setVisibility(View.INVISIBLE);
                 cancelBut.setVisibility(View.VISIBLE);
                 valBut.setVisibility(View.VISIBLE);
                 break;
             case R.id.mangaOk:
                 this.finish();
                 break;
             case R.id.cancelEditBut:
                 editBut.setVisibility(View.VISIBLE);
                 okayBut.setVisibility(View.VISIBLE);
                 cancelBut.setVisibility(View.INVISIBLE);
                 valBut.setVisibility(View.INVISIBLE);
                 break;
             case R.id.valEditBut:
                 editBut.setVisibility(View.VISIBLE);
                 okayBut.setVisibility(View.VISIBLE);
                 cancelBut.setVisibility(View.INVISIBLE);
                 valBut.setVisibility(View.INVISIBLE);
                 break;
             default:
                 break;


         }


    }
}
