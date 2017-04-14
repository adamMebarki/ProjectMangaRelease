package com.mangarelease.adam.projectmangarelease.LibrarySource;

/**
 * Created by Adam on 11/03/2017.
 * Contains the title of Favorite Mangas, Checkbox to delete or update the following.
 * Manage the interaction on the list
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;
import com.mangarelease.adam.projectmangarelease.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.FIRST_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.SECOND_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.THIRD_COLUMN;

public class listViewAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    private Activity activity; // hold the Library Activity
    private ViewHolder holder;
    private ArrayList arrayTrash; // contains the position of manga which will be delete in the list
    private ArrayList arrayNoFollow; // contains the position of manga which will be not follow after in the list
    private ArrayList arrayFollow;// contains the position of manga which will be follow after in the list
    private SqLiteHelper db;
    public ArrayList<MangaClass> arrayManga;

    /**
     * Get the current Activity where the listViewAdapter is created
     * @param activity
     */
    public listViewAdapter(Activity activity) {
        super();
        this.activity = activity;
        this.list = new ArrayList<>();
        holder = new ViewHolder();
        arrayTrash = new ArrayList();
        arrayFollow = new ArrayList();
        arrayNoFollow = new ArrayList();
        arrayManga = new ArrayList<>();
        arrayManga.addAll(db.getInstance(activity.getApplicationContext()).getAllMangas());
        populateList2();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    // For a better management of the columns of the list
    private class ViewHolder {
        TextView txtColName; // manga title
        CheckBox txtColFav;  // star Icon
        CheckBox txtColTrash; // trash icon
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub

        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_library, null);
            holder.txtColName = (TextView) convertView.findViewById(R.id.columnName);
            holder.txtColFav = (CheckBox) convertView.findViewById(R.id.columnFavorite);
            holder.txtColTrash = (CheckBox) convertView.findViewById(R.id.columnTrash);
            holder.txtColFav.setTag(position); // give the current position of the associate title manga
            holder.txtColTrash.setTag(position); // give the current position of the associate title manga

            // No other choices for the moment ...
            // If user click on the checkbox -> add or remove the position of the selected title in
            // the arrayTrash.
            holder.txtColTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    boolean isChecked = ((CheckBox) arg0).isChecked();
                    if (isChecked) {
                        // Not already in the array add in
                        if (!arrayTrash.contains(arg0.getTag()))
                            arrayTrash.add(arg0.getTag());
                        // If tag is in the array remove in  because user change his choice
                    } else {
                        if (arrayTrash.contains(arg0.getTag()))
                            arrayTrash.remove(arg0.getTag());
                    }
                }
            });

            // Same puporse as checkbox. Add or Remove from ArrayNoFollow or ArrayFollow
            // when user click on the checkbox star
            holder.txtColFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    boolean isChecked = ((CheckBox) arg0).isChecked();
                    if (isChecked) { //
                        // Add manga in follow list if not already && remove in no follow list also
                        if (!arrayFollow.contains(arg0.getTag())) {
                            arrayFollow.add(arg0.getTag());
                        }
                        if (arrayNoFollow.contains(arg0.getTag())) {
                            arrayNoFollow.remove(arg0.getTag());
                        }
                    } else {
                        // Add manga in no follow list if not already && remove in follow list also
                        if (!arrayNoFollow.contains(arg0.getTag()))
                            arrayNoFollow.add(arg0.getTag());
                        if (arrayFollow.contains(arg0.getTag()))
                            arrayFollow.remove(arg0.getTag());
                    }
                }

            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position); // revoir si hashmap pas mieux a la place d'une list
        holder.txtColName.setText(map.get(FIRST_COLUMN).toString()); // put the title of the manga
        holder.txtColName.setSelected(true);
        // Look in the db if the manga is not follow  and change the icon of the checkbox in grey star
        String title = map.get(FIRST_COLUMN).toString();
        int idmg = db.getInstance(activity.getApplicationContext()).getManga_id(title);
        if (db.getInstance(activity.getApplicationContext()).isFollow(idmg) == 0) {
            holder.txtColFav.setChecked(false);
        }
        return convertView;
    }

    // getter of ArrayList
    public ArrayList getArrayTrashSelected() {
        return arrayTrash;
    }

    public ArrayList getArrayFollow() {
        return arrayFollow;
    }

    public ArrayList getArrayNoFollow() {
        return arrayNoFollow;
    }

    // Clear all arrayList
    public void clear() {
        this.arrayNoFollow.clear();
        this.arrayFollow.clear();
        this.arrayTrash.clear();
    }


    // Populate the list with the title of manga retrieve from the database
    public void populateList2() {
        HashMap tmp;

        for (int i = 0; i < arrayManga.size(); i++) {
            tmp = new HashMap();
            tmp.put(FIRST_COLUMN, "" + arrayManga.get(i).getTitle());
            tmp.put(SECOND_COLUMN, "");
            tmp.put(THIRD_COLUMN, "");
            list.add(tmp);
        }
    }

    public ArrayList<HashMap> getList(){
        return list;
    }

}
