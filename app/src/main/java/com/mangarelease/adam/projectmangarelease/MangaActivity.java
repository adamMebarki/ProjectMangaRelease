package com.mangarelease.adam.projectmangarelease;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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

import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.TomeClass;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Adam on 15/03/2017.
 * Activity display all information of a manga and its list of  its tomes
 * Open when user click on the title of the manga in the LibraryActivity
 */

public class MangaActivity extends AppCompatActivity implements View.OnClickListener {

    private Button okayBut, cancelBut, valBut, btnDismiss;
    private EditText text_author, text_category, text_price, text_editor;
    private ImageButton editBut;
    private TextView title;
    private MangaClass manga;
    private SqLiteHelper db;
    private TableLayout table;
    private static Float scale;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);

        // instantiation of the element from the layout
        title = (TextView) findViewById(R.id.mangaTitle);
        okayBut = (Button) findViewById(R.id.mangaOk);
        editBut = (ImageButton) findViewById(R.id.editBut);
        cancelBut = (Button) findViewById(R.id.cancelEditBut);
        valBut = (Button) findViewById(R.id.valEditBut);

        // get the title of the manga and get the manga from the db and the author
        String text = getIntent().getStringExtra("title");
        title.setText(text);
        manga = db.getInstance(getApplicationContext()).getManga(text);
        String author_name = db.getInstance(getApplicationContext()).getAuthor(manga.getAuthor_id());
        manga.setAuthor_name(author_name);
        manga.setVolumes((ArrayList<TomeClass>) db.getInstance(getApplicationContext()).getAllVolumes(manga.getManga_id()));
        Collections.sort(manga.getVolumes()); // sort the tomes of the manga

        okayBut.setOnClickListener(this);
        editBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);
        valBut.setOnClickListener(this);

        // By default all edittext can't be change. Only when editbut is clicked
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
            case R.id.dismiss:
                popupWindow.dismiss();
                table.setVisibility(View.VISIBLE);
                editBut.setEnabled(true);
                break;
            case R.id.editBut:
                // Active the edittexts and make visible the buttons edition to validate or cancel
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
                // return to the LibraryActivity
                this.finish();
                break;
            case R.id.cancelEditBut:
                // Cancel every change made by the user and return to the normal state of the MangaActivity
                editBut.setVisibility(View.VISIBLE);
                okayBut.setVisibility(View.VISIBLE);
                cancelBut.setVisibility(View.INVISIBLE);
                valBut.setVisibility(View.INVISIBLE);
                text_author.setEnabled(false);
                text_editor.setEnabled(false);
                text_category.setEnabled(false);
                text_price.setEnabled(false);
                text_author.setText(manga.getAuthor_name());
                text_editor.setText(manga.getEditor_name());
                text_category.setText(manga.getCategory());
                text_price.setText(manga.getPrice()+"");
                break;
            case R.id.valEditBut:
                // Valid modification made on the editText save in the db and return in the normal state of the MangaActivity
                editBut.setVisibility(View.VISIBLE);
                okayBut.setVisibility(View.VISIBLE);
                cancelBut.setVisibility(View.INVISIBLE);
                valBut.setVisibility(View.INVISIBLE);
                text_author.setEnabled(false);
                text_editor.setEnabled(false);
                text_category.setEnabled(false);
                text_price.setEnabled(false);
                // Make change on database
                manga.setAuthor_name(text_author.getText().toString());
                manga.setEditor_name(text_editor.getText().toString());
                manga.setCategory(text_category.getText().toString());
                Double price = Double.parseDouble(text_price.getText().toString());
                manga.setPrice(price);
                String name = text_author.getText().toString();
                // If the author add not in the db create it and update the author_id of the manga
                if (!db.getInstance(getApplicationContext()).AuthorExists(name)) {
                    int author_id = (int) db.getInstance(getApplicationContext()).createAuthor(name);
                    manga.setAuthor_id(author_id);
                    db.getInstance(getApplicationContext()).updateManga(manga);
                } else {
                    int id = db.getInstance(getApplicationContext()).getAuthor_id(name);
                    manga.setAuthor_id(id);
                    db.getInstance(getApplicationContext()).updateManga(manga);
                }
                break;
            default:
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void createTable() {
        Button but;
        // three buttons per row
        android.widget.TableRow.LayoutParams p = new android.widget.TableRow.LayoutParams();
        p.rightMargin = dpToPixel(10, getApplicationContext()); // right-margin = 10dp
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1f));
        row.setPadding(0, 20, 20, 0);
        // Better to start at 1 for calculation
        for (int i = 1; i <= manga.getVolumes().size(); i++) {
            // get the informatio to display on the screen picture and resume
            final String pict = manga.getVolumes().get(i - 1).getImage();
            final String desc = manga.getVolumes().get(i - 1).getDesc();
            // create the button related to the current tome
            but = new Button(this);
            but.setId(i - 1);
            but.setGravity(Gravity.CENTER_HORIZONTAL);
            but.setText(manga.getVolumes().get(i - 1).getNum_vol());
            but.setLayoutParams(p);
            // Check if the tome was bought by the user and change the color in consequence
            if (db.getInstance(getApplicationContext()).isBuy((String) but.getText())) {
                but.setTag("GREEN");
                but.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_clicked));
            } else {
                but.setTag("GREY");
                but.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_normal));
            }

            // No other choice to make like this. I do not have enough experience to do it properly. Maybe after
            // Shor Click modify the status of the tome isbuy or is not buy by the user
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
            //Unfortunatly it is the same here... Please forgive me ...
            // long click create the popup window which display the picture and the resume of the tome
            but.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    table.setVisibility(View.INVISIBLE);
                    editBut.setEnabled(false);
                    LayoutInflater layoutInflater
                            = (LayoutInflater) getBaseContext()
                            .getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup_tome_manga, null);

                    createPopupWindow(popupView,desc,pict);
                    return true;
                }
            });
            row.addView(but);
            if (i % 3 == 0 && i != 0) {
                table.addView(row);
                row = new TableRow(this);
                row.setPadding(0, 20, 20, 0);
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1f));
            }
        }
        table.addView(row);


    }


    public void createPopupWindow(View popupView,String desc,String pict) {
         popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
        WebView web = (WebView) popupView.findViewById(R.id.popup_wb);
        TextView desc_tome = (TextView) popupView.findViewById(R.id.desc_content_popup);
        desc_tome.setText(desc);
        desc_tome.setMovementMethod(new ScrollingMovementMethod());
        String html = "<style>img{height: 90%;max-width: 100%;}</style> <html><head></head><body><img src=\"" + pict + "\"></body></html>";
        web.loadUrl("about:blank");
        web.loadData(html, "text/html", null);
        web.getSettings();
        web.setBackgroundColor(Color.TRANSPARENT);
        // Nothing to say...
        btnDismiss.setOnClickListener(this);
        /*btnDismiss.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                table.setVisibility(View.VISIBLE);
                editBut.setEnabled(true);
            }
        });*/
        popupWindow.showAtLocation(popupView, Gravity.CENTER_HORIZONTAL, 0, -150);

    }

    /**
     * Useful method to position the buttons on the tableLayout like I want. No need to go further more.
     * @param dp int
     * @param context Context get current context
     * @return
     */
    public static int dpToPixel(int dp, Context context) {
        if (scale == null)
            scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) dp * scale);
    }

}
