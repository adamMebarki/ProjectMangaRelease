package com.mangarelease.adam.projectmangarelease;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mangarelease.adam.projectmangarelease.LibrarySource.listViewAdapter;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.ConfirmDialog;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;

import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.FIRST_COLUMN;

public class LibraryActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private Button returnLibButton, validateLibButton;
    private ListView listMangaView;
    private int impactPoint = 0;
    SqLiteHelper db;
    private listViewAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        // instantiation of element of the layout library
        returnLibButton = (Button) findViewById(R.id.returnButLib);
        returnLibButton.setOnClickListener(this);
        validateLibButton = (Button) findViewById(R.id.validateButLib);
        validateLibButton.setOnClickListener(this);
        listMangaView = (ListView) findViewById(R.id.listview);
        adapter = new listViewAdapter(this);
        listMangaView.setAdapter(adapter);
        listMangaView.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == returnLibButton.getId()) { // return to the MainActivity
            this.finish();
        } else if (v.getId() == validateLibButton.getId()) { // validate all changement made by the user on the list or return to the Main
            // Activity id nothing.
            if (adapter.getArrayTrashSelected().isEmpty() && adapter.getArrayFollow().isEmpty() && adapter.getArrayNoFollow().isEmpty()) {
                this.finish();
            } else {
                // If changement  open a Dialog to confirm before apply.
                ConfirmDialog diag = new ConfirmDialog(this, listMangaView, adapter);
                diag.show(getSupportFragmentManager(), "Test");
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // Get the X on the list when user touch the screen
        // Determine also if the user click on a title of a manga by using the impactpoint or just slide the list up or down.
        //
        if (v.getId() == listMangaView.getId()) {
            int point = listMangaView.pointToPosition((int) event.getX(), (int) event.getY());
            if (point != -1) { // Prevent error by clicking outside the list when the list is short
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        impactPoint = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        int dif = impactPoint - (int) event.getY();
                        // Open Manga Activity
                        if (dif == 0) { // when user touch and release his finger in the same point == click
                            point = listMangaView.pointToPosition((int) event.getX(), (int) event.getY());
                            Intent in = new Intent(this, MangaActivity.class);
                            in.putExtra("title", (String) adapter.getList().get(point).get(FIRST_COLUMN));
                            startActivity(in);
                        }
                        break;
                }
            }
        }

        return false;
    }


}
