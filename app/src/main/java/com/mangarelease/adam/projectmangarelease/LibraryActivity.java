package com.mangarelease.adam.projectmangarelease;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mangarelease.adam.projectmangarelease.LibrarySource.listViewAdapter;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.ConfirmDialog;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.FIRST_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.SECOND_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.THIRD_COLUMN;

public class LibraryActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ArrayList<HashMap> list;
    private Button retBut, valBut;
    ListView lview;
    int impactPoint = 0;
    SqLiteHelper db;
    listViewAdapter adapter;
    int tailleList=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        retBut = (Button) findViewById(R.id.returnButLib);
        retBut.setOnClickListener(this);
        valBut = (Button) findViewById(R.id.validateButLib);
        valBut.setOnClickListener(this);
        lview = (ListView) findViewById(R.id.listview);
        ArrayList<MangaClass> array = new ArrayList<>();
        array.addAll(db.getInstance(getApplicationContext()).getAllMangas());
        tailleList = array.size();
        for (int i = 0; i < array.size(); i++) {
            Log.d("Library Manga : ", array.get(i).getTitle());
            Log.d("Library Manga ID : ", array.get(i).getManga_id() + "");
        }
        populateList2(array);
        adapter = new listViewAdapter(this, list, array);
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


    private void populateList2(ArrayList<MangaClass> array) {
        list = new ArrayList<HashMap>();
        HashMap tmp;
        for (int i = 0; i < array.size(); i++) {
            tmp = new HashMap();
            tmp.put(FIRST_COLUMN, "" + array.get(i).getTitle());
            tmp.put(SECOND_COLUMN, "");
            tmp.put(THIRD_COLUMN, "");
            list.add(tmp);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == retBut.getId()) {
            this.finish();

        } else if (v.getId() == valBut.getId()) {
            Log.d("ArrayLib","HERE");
                if(adapter !=null){
                    for(int i=0;i<adapter.getArrayTrashSelected().size();i++){
                        Log.d("ArrayLibTrash",""+ adapter.getArrayTrashSelected().get(i).toString());
                    }
                    for(int i=0;i<adapter.getArrayFollow().size();i++){
                        Log.d("ArrayLibFollow",""+ adapter.getArrayFollow().get(i).toString());
                    }
                    for(int i=0;i<adapter.getArrayNoFollow().size();i++){
                        Log.d("ArrayLibNoFollow",""+ adapter.getArrayNoFollow().get(i).toString());
                    }
                    Log.d("Stop : ","-------------------------------------------------------------");
                }
            ConfirmDialog diag = new ConfirmDialog(this,lview,adapter);
            diag.show(getSupportFragmentManager(),"Test");
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == lview.getId()) {
            //  Toast toast;
            int point = lview.pointToPosition((int) event.getX(), (int) event.getY());
            CharSequence text = "" + point;
            //  int duration = Toast.LENGTH_SHORT;
            Log.d("Point : ", point + "");
            if (point != -1) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        impactPoint = (int) event.getY();
                        //  toast = Toast.makeText(v.getContext(), "DOWN : " + impactPoint, duration);
                        //  toast.show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        int dif = impactPoint - (int) event.getY();
                        // toast = Toast.makeText(v.getContext(), "UP : " + dif + "  impact point : " + impactPoint, duration);
                        // toast.show();

                        // Open activity description manga
                        if (dif == 0) {
                            point = lview.pointToPosition((int) event.getX(), (int) event.getY());
                            //  toast = Toast.makeText(v.getContext(), "Click : " + list.get(point).get(FIRST_COLUMN), duration);
                            //  toast.show();
                            Intent in = new Intent(this, MangaActivity.class);
                            in.putExtra("title", (String) list.get(point).get(FIRST_COLUMN));
                            startActivity(in);
                        }
                        break;
                }
            }
        }

        return false;
    }




}
