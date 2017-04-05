package com.mangarelease.adam.projectmangarelease;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.TomeClass;

import java.util.ArrayList;

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
    private TableLayout table;
    private TableRow row;
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
        String author_name = db.getInstance(getApplicationContext()).getAuthor(manga.getAuthor_id());
        manga.setAuthor_name(author_name);
        manga.setVolumes((ArrayList<TomeClass>) db.getInstance(getApplicationContext()).getAllVolumes(manga.getManga_id()));
        for(int i=0;i<manga.getVolumes().size();i++){
            Log.d("Volumes : ",manga.getVolumes().get(i).getNum_vol());
        }
        okayBut.setOnClickListener(this);
        editBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);
        valBut.setOnClickListener(this);

        text_author = (EditText) findViewById(R.id.text_author);
        text_author.setEnabled(false);
        text_author.setText(manga.getAuthor_name());
        text_editor = (EditText) findViewById(R.id.text_editor);
        text_editor.setEnabled(false);
        text_editor.setText(manga.getEditor_name());
        text_category = (EditText) findViewById(R.id.text_category);
        text_category.setEnabled(false);
        text_category.setText(manga.getCategory());
        text_price = (EditText) findViewById(R.id.text_price);
        text_price.setEnabled(false);
        text_price.setText(""+manga.getPrice());


        // Table Layout part
       /* Find Tablelayout defined in main.xml */
        table = (TableLayout) findViewById(R.id.tableVolume);
        table.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,1f));

        createTable();
      /*  Button b = new Button(this);
        b.setText("Button 1 ");
        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
        tr.addView(b);

        Button b2 = new Button(this);
        b2.setText("Button 2 ");
        b2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
        tr.addView(b2);

        Button b3 = new Button(this);
        b3.setText("Button 3 ");
        b3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(b3);
        table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
*/
    }

    @Override
    public void onClick(View v) {

         switch (v.getId()){
             case R.id.editBut:
                 editBut.setVisibility(View.INVISIBLE);
                 okayBut.setVisibility(View.INVISIBLE);
                 cancelBut.setVisibility(View.VISIBLE);
                 valBut.setVisibility(View.VISIBLE);
                 text_author.setEnabled(true);
                 text_editor.setEnabled(true);
                 text_category.setEnabled(true);
                 text_price.setEnabled(true);
                 break;
             case R.id.mangaOk:
                 this.finish();
                 break;
             case R.id.cancelEditBut:
                 editBut.setVisibility(View.VISIBLE);
                 okayBut.setVisibility(View.VISIBLE);
                 cancelBut.setVisibility(View.INVISIBLE);
                 valBut.setVisibility(View.INVISIBLE);
                 text_author.setEnabled(false);
                 text_editor.setEnabled(false);
                 text_category.setEnabled(false);
                 text_price.setEnabled(false);
                 break;
             case R.id.valEditBut:
                 editBut.setVisibility(View.VISIBLE);
                 okayBut.setVisibility(View.VISIBLE);
                 cancelBut.setVisibility(View.INVISIBLE);
                 valBut.setVisibility(View.INVISIBLE);
                 text_author.setEnabled(false);
                 text_editor.setEnabled(false);
                 text_category.setEnabled(false);
                 text_price.setEnabled(false);
                 // Make change on database
                 break;
             default:
                 break;


         }


    }


    public void createTable(){
        Button but;
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
        for(int i=1;i<=30;i++){
            but = new Button(this);
            but.setText("Button "+i);
            but.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
            row.addView(but);
            if(i%3==0){
                table.addView(row);
                row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,1f));
            }
        }


    }


}
