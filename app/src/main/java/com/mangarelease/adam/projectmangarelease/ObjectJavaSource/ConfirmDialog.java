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
 * Class ConfirmDialog uses to confirm the changement make by an user on his favorite list.
 *
 */

public class ConfirmDialog extends DialogFragment {

    private Activity activity;
    private ListView listView;
    private listViewAdapter adapter;
    private SqLiteHelper db;

    /**
     * Constructor Confirm Dialog
     * @param activity  get the current activity where the instance will be created
     * @param listView   get the listview of the current Activity
     * @param adapter get the listViewAdapter of the current Activity
     */
    public ConfirmDialog(Activity activity, ListView listView, listViewAdapter adapter) {
        this.activity = activity;
        this.listView = listView;
        this.adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you really want to apply ?")
                .setPositiveButton("Validate", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        validateChoice(adapter);
                          activity.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        initialiseList(listView, adapter);

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
     *
     * @param lv listview from the libraryActivity to initialise again
     * @param lva listviewAdapter from the LibraryActivity to initialise again
     */
    public void initialiseList(ListView lv, listViewAdapter lva) {
        lva.clear();
        lv.setAdapter(lva);
    }

    /**
     *
     * @param lva ListViewAdapter from the LibraryActivity which contains the informations of the changements
     *            made by the user on his list. Take the three ArrayList related to the checkbox.
     */
    public void validateChoice(listViewAdapter lva) {
        ArrayList<MangaClass> array = new ArrayList<>();
        array.addAll(lva.arrayManga); // contains all of the manga display on the listView
        if (!lva.getArrayFollow().isEmpty()) {  // Manga are now follow
            int position = 0;
            for (int i = 0; i < lva.getArrayFollow().size(); i++) {
                position = (int) lva.getArrayFollow().get(i);
                // update favorite status of a manga in the current position of the list provide by the ArrayList ArrayFollow
                db.getInstance(activity.getApplicationContext()).updateFavorite(array.get(position).getManga_id(), 1);
            }
        }

        if (!lva.getArrayNoFollow().isEmpty()) { // Manga are now not follow
            int position = 0;
            for (int i = 0; i < lva.getArrayNoFollow().size(); i++) {
                position = (int) lva.getArrayNoFollow().get(i);
                // update favorite status of a manga in the current position of the list provide by the ArrayList ArrayNoFollow
                db.getInstance(activity.getApplicationContext()).updateFavorite(array.get(position).getManga_id(), 0);
            }

        }
        if (!lva.getArrayTrashSelected().isEmpty()) { // Manga will be deleted
            int position = 0;
            for (int i = 0; i < lva.getArrayTrashSelected().size(); i++) {
                position = (int) lva.getArrayTrashSelected().get(i);
                // Delete the manga in the db and all of its tomes
                db.getInstance(activity.getApplicationContext()).deleteFavorite(array.get(position).getManga_id());
                db.getInstance(activity.getApplicationContext()).deleteManga(array.get(position).getManga_id());
                db.getInstance(activity.getApplicationContext()).deleteAllTomes(array.get(position).getManga_id());
            }
            // Apply all of the change on the ListView
            lva.arrayManga.clear();
            lva.arrayManga.addAll(db.getInstance(activity.getApplicationContext()).getAllMangas());
            lva.list.clear();
            lva.populateList2();
            lva.notifyDataSetChanged();
            listView.setAdapter(lva);
        }

    }
}