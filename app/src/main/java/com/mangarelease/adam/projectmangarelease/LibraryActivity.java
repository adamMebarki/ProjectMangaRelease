package com.mangarelease.adam.projectmangarelease;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mangarelease.adam.projectmangarelease.LibrarySource.listViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.FIRST_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.SECOND_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.THIRD_COLUMN;

public class LibraryActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ArrayList<HashMap> list;
    private Button retBut;
    ListView lview;
    int  impactPoint=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        retBut = (Button) findViewById(R.id.returnButLib);
        retBut.setOnClickListener(this);
        lview = (ListView) findViewById(R.id.listview);
        populateList();
        listViewAdapter adapter = new listViewAdapter(this, list);
        lview.setAdapter(adapter);
        lview.setOnTouchListener(this);

    }

    private void populateList() {

        list = new ArrayList<HashMap>();

        HashMap temp = new HashMap();
        temp.put(FIRST_COLUMN, "Colored Notebooks");
        temp.put(SECOND_COLUMN, "");
        temp.put(THIRD_COLUMN, "");
        list.add(temp);

        HashMap temp1 = new HashMap();
        temp1.put(FIRST_COLUMN, "Diaries");
        temp1.put(SECOND_COLUMN, "");
        temp1.put(THIRD_COLUMN, "");
        list.add(temp1);

        HashMap temp2 = new HashMap();
        temp2.put(FIRST_COLUMN, "Note Books and Stationery");
        temp2.put(SECOND_COLUMN, "");
        temp2.put(THIRD_COLUMN, "");
        list.add(temp2);

        HashMap temp3 = new HashMap();
        temp3.put(FIRST_COLUMN, "Corporate Diaries");
        temp3.put(SECOND_COLUMN, "");
        temp3.put(THIRD_COLUMN, "");
        list.add(temp3);

        HashMap temp4 = new HashMap();
        temp4.put(FIRST_COLUMN, "Writing Pad");
        temp4.put(SECOND_COLUMN, "");
        temp4.put(THIRD_COLUMN, "");
        list.add(temp4);

        HashMap temp5 = new HashMap();
        temp5.put(FIRST_COLUMN, "Writing Pad");
        temp5.put(SECOND_COLUMN, "");
        temp5.put(THIRD_COLUMN, "");
        list.add(temp5);

        HashMap temp6 = new HashMap();
        temp6.put(FIRST_COLUMN, "Writing Pad");
        temp6.put(SECOND_COLUMN, "");
        temp6.put(THIRD_COLUMN, "");
        list.add(temp6);
        HashMap temp7 = new HashMap();
        temp7.put(FIRST_COLUMN, "Writing Pad");
        temp7.put(SECOND_COLUMN, "");
        temp7.put(THIRD_COLUMN, "");
        list.add(temp7);

        HashMap temp8 = new HashMap();
        temp8.put(FIRST_COLUMN, "Writing Pad");
        temp8.put(SECOND_COLUMN, "");
        temp8.put(THIRD_COLUMN, "");
        list.add(temp8);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == retBut.getId()) {
            this.finish();

        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == lview.getId()) {

            Toast toast;
            int point = lview.pointToPosition((int) event.getX(), (int) event.getY());
            CharSequence text = "" + point;
            int duration = Toast.LENGTH_SHORT;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    impactPoint = (int) event.getY();
                    toast = Toast.makeText(v.getContext(), "DOWN : "  + impactPoint, duration);
                    toast.show();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    int dif = impactPoint - (int) event.getY();
                    toast = Toast.makeText(v.getContext(), "UP : " + dif + "  impact point : " + impactPoint, duration);
                    toast.show();

                    if(dif==0){
                        point = lview.pointToPosition((int) event.getX(), (int) event.getY());
                        toast = Toast.makeText(v.getContext(), "Click : "  +list.get(point).get(FIRST_COLUMN), duration);
                        toast.show();
                        Intent in = new Intent(this,MangaActivity.class);
                        in.putExtra("title", (String) list.get(point).get(FIRST_COLUMN));
                        startActivity(in);

                    }
                    break;
            }
        }

        return false;
    }


}
