package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;

import com.mangarelease.adam.projectmangarelease.LibrarySource.listViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.FIRST_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.SECOND_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.THIRD_COLUMN;

/**
 * Created by Adam on 02/04/2017.
 */

public class ConfirmDialog extends DialogFragment {

    private Activity act;
    private ListView lv;
    private listViewAdapter lva;
    private SqLiteHelper db;

    public ConfirmDialog(Activity act, ListView lv, listViewAdapter lva) {
        this.act = act;
        this.lv = lv;
        this.lva = lva;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Coucou")
                .setPositiveButton("Validate", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        validateChoice(lva);
                        //  act.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        initialiseList(lv, lva);

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    public void initialiseList(ListView lv, listViewAdapter lva) {
        ArrayList<HashMap> list;
        ArrayList<MangaClass> array = new ArrayList<>();
        array.addAll(db.getInstance(act.getApplicationContext()).getAllMangas());
        list = new ArrayList<HashMap>();
        HashMap tmp;
        for (int i = 0; i < array.size(); i++) {
            tmp = new HashMap();
            tmp.put(FIRST_COLUMN, "" + array.get(i).getTitle());
            tmp.put(SECOND_COLUMN, "");
            tmp.put(THIRD_COLUMN, "");
            list.add(tmp);
        }
       // lv.removeAllViewsInLayout();
        lv.setAdapter(lva);
    }


    public void validateChoice(listViewAdapter lva) {
        ArrayList<MangaClass> array = new ArrayList<>();
        array.addAll(db.getInstance(act.getApplicationContext()).getAllMangas());
        for (int i = 0; i < array.size(); i++) {
            Log.d("ConfirmDialog Manga : ", array.get(i).getTitle());
            Log.d("ConfirmDialog Manga ID : ", array.get(i).getManga_id() + "");
        }
        if (!lva.getArrayFollow().isEmpty()) {
            //db.getInstance(act.getApplicationContext()).updateFavorite(, 1);
        }

        if (!lva.getArrayNoFollow().isEmpty()) {
            //     db.getInstance(act.getApplicationContext()).updateFavorite(, 0);
        }
        if (!lva.getArrayTrashSelected().isEmpty()) {
        }

    }
}
