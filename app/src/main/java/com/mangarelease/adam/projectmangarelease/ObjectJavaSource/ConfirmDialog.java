package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import com.mangarelease.adam.projectmangarelease.LibrarySource.listViewAdapter;

import java.util.ArrayList;

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
                          act.finish();
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
        lva.clear();
        lv.setAdapter(lva);
    }


    public void validateChoice(listViewAdapter lva) {
        ArrayList<MangaClass> array = new ArrayList<>();
        array.addAll(lva.arrayManga);
        if (!lva.getArrayFollow().isEmpty()) {  // Manga are now follow
            int position = 0;
            for (int i = 0; i < lva.getArrayFollow().size(); i++) {
                position = (int) lva.getArrayFollow().get(i);
                db.getInstance(act.getApplicationContext()).updateFavorite(array.get(position).getManga_id(), 1);
            }
        }

        if (!lva.getArrayNoFollow().isEmpty()) { // Manga are now not follow
            int position = 0;
            for (int i = 0; i < lva.getArrayNoFollow().size(); i++) {
                position = (int) lva.getArrayNoFollow().get(i);
                db.getInstance(act.getApplicationContext()).updateFavorite(array.get(position).getManga_id(), 0);
            }

        }
        if (!lva.getArrayTrashSelected().isEmpty()) { // Manga will be deleted
            int position = 0;
            for (int i = 0; i < lva.getArrayTrashSelected().size(); i++) {
                position = (int) lva.getArrayTrashSelected().get(i);
                db.getInstance(act.getApplicationContext()).deleteFavorite(array.get(position).getManga_id());
                db.getInstance(act.getApplicationContext()).deleteManga(array.get(position).getManga_id());
                db.getInstance(act.getApplicationContext()).deleteAllTomes(array.get(position).getManga_id());
            }

            lva.arrayManga.clear();
            lva.arrayManga.addAll(db.getInstance(act.getApplicationContext()).getAllMangas());
            lva.list.clear();
            lva.populateList2();
            lva.notifyDataSetChanged();
            lv.setAdapter(lva);
        }

    }
}