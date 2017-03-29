package com.mangarelease.adam.projectmangarelease.LibrarySource;

/**
 * Created by Adam on 11/03/2017.
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.mangarelease.adam.projectmangarelease.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.FIRST_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.SECOND_COLUMN;
import static com.mangarelease.adam.projectmangarelease.LibrarySource.Constant.THIRD_COLUMN;

public class listViewAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    Activity activity;
    ViewHolder holder;

    public listViewAdapter(Activity activity, ArrayList<HashMap> list) {
        super();
        this.activity = activity;
        this.list = list;
        holder = new ViewHolder();


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





    private class ViewHolder {
        TextView txtColName;
        CheckBox txtColFav;
        CheckBox txtColTrash;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub

        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_library, null);
//            holder = new ViewHolder();
            holder.txtColName = (TextView) convertView.findViewById(R.id.columnName);
            holder.txtColFav = (CheckBox) convertView.findViewById(R.id.columnFavorite);
            holder.txtColTrash = (CheckBox) convertView.findViewById(R.id.columnTrash);
            holder.txtColTrash.setTag(position);

            holder.txtColTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    boolean isChecked = ((CheckBox)arg0).isChecked();
                    String tag=arg0.getTag().toString();
                    if (isChecked){

                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(arg0.getContext(),tag, duration);
                        toast.show();
                    }else{

                    }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        HashMap map = list.get(position);
        holder.txtColName.setText(map.get(FIRST_COLUMN).toString());
        holder.txtColFav.setText(map.get(SECOND_COLUMN).toString());
        holder.txtColTrash.setText(map.get(THIRD_COLUMN).toString());

        return convertView;
    }
}
