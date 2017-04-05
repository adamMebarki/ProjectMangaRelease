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
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;

import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.FIRST_COLUMN;

public class LibraryActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private Button retBut, valBut;
    ListView lview;
    int impactPoint = 0;
    SqLiteHelper db;
    listViewAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        retBut = (Button) findViewById(R.id.returnButLib);
        retBut.setOnClickListener(this);
        valBut = (Button) findViewById(R.id.validateButLib);
        valBut.setOnClickListener(this);
        lview = (ListView) findViewById(R.id.listview);
        adapter = new listViewAdapter(this);
        lview.setAdapter(adapter);
        lview.setOnTouchListener(this);
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
                            Log.d("Library activity", (String) adapter.getList().get(point).get(FIRST_COLUMN));
                            //  toast = Toast.makeText(v.getContext(), "Click : " + list.get(point).get(FIRST_COLUMN), duration);
                            //  toast.show();
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
