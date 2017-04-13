package com.mangarelease.adam.projectmangarelease;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.TomeClass;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Adam on 15/03/2017.
 */

public class MangaActivity extends AppCompatActivity implements View.OnClickListener {

    private Button okayBut, cancelBut, valBut;
    private EditText text_author, text_category, text_price, text_editor;
    private ImageButton editBut;
    private TextView title;
    private MangaClass manga;
    private SqLiteHelper db;
    private TableLayout table;
    private static Float scale;

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
        if (manga == null)
            Log.d("YES", "YES  " + text);
        String author_name = db.getInstance(getApplicationContext()).getAuthor(manga.getAuthor_id());
        manga.setAuthor_name(author_name);
        manga.setVolumes((ArrayList<TomeClass>) db.getInstance(getApplicationContext()).getAllVolumes(manga.getManga_id()));
        for (int i = 0; i < manga.getVolumes().size(); i++) {
            Log.d("Volumes : ", manga.getVolumes().get(i).getNum_vol());
        }
        Collections.sort(manga.getVolumes());
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
        text_price.setText("" + manga.getPrice());


        // Table Layout part
       /* Find Tablelayout defined in main.xml */
        table = (TableLayout) findViewById(R.id.tableVolume);
        table.setGravity(Gravity.CENTER_HORIZONTAL);
        createTable();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createTable() {
        Button but;
        android.widget.TableRow.LayoutParams p = new android.widget.TableRow.LayoutParams();
        p.rightMargin = dpToPixel(10, getApplicationContext()); // right-margin = 10dp
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        row.setPadding(0, 20, 20, 0);
        for (int i = 0; i < manga.getVolumes().size(); i++) {
            final String pict = manga.getVolumes().get(i).getImage();
            but = new Button(this);
            but.setId(i);
            but.setGravity(Gravity.CENTER_HORIZONTAL);
            but.setText(manga.getVolumes().get(i).getNum_vol());
            but.setLayoutParams(p);
            if (db.getInstance(getApplicationContext()).isBuy((String) but.getText())) {
                but.setTag("GREEN");
                but.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_clicked));
            } else {
                but.setTag("GREY");
                but.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_normal));
            }

            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    if (v.getTag().toString().compareTo("GREEN") != 0) {
                        v.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_clicked));
                        v.setTag("GREEN");
                        db.getInstance(getApplicationContext()).updateBuy(1, (String) b.getText());
                    } else {
                        v.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_normal));
                        v.setTag("GREY");
                        db.getInstance(getApplicationContext()).updateBuy(0, (String) b.getText());
                    }
                }
            });
            final Button finalBut = but;
            but.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    table.setVisibility(View.INVISIBLE);
                    Context context = getApplicationContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    LayoutInflater layoutInflater
                            = (LayoutInflater) getBaseContext()
                            .getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup_tome_manga, null);
                    final PopupWindow popupWindow = new PopupWindow(
                            popupView,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                    WebView web = (WebView) popupView.findViewById(R.id.popup_wb);
                    String html = "<style>img{height: 80%;max-width: 80%;}</style> <html><head></head><body><center><img src=\"" + pict + "\"></center></body></html>";
                    web.loadUrl("about:blank");
                    web.loadData(html, "text/html", null);
                    web.getSettings();
                    web.setBackgroundColor(Color.GREEN);
                    btnDismiss.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            table.setVisibility(View.VISIBLE);
                        }
                    });
                    popupWindow.showAtLocation(popupView,Gravity.CENTER_HORIZONTAL,0,-150);

                    //popupWindow.showAsDropDown(finalBut, 50, -30);
                    return true;
                }
            });
            row.addView(but);
            if (i % 2 == 0 && i != 0) {
                table.addView(row);
                row = new TableRow(this);
                row.setPadding(0, 20, 20, 0);
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            }
        }
        table.addView(row);


    }


    public static int dpToPixel(int dp, Context context) {
        if (scale == null)
            scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) dp * scale);
    }

}
