package com.mangarelease.adam.projectmangarelease.NewReleaseSource.FilterSearchSource;


import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mangarelease.adam.projectmangarelease.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Adam on 04/03/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context ctxt;
    public int[] stateManga; // [0] -> Favorites [1] -> Others
    public int[] stateCategory; // [0] -> Shonen [1] -> Shojo [2] -> Seinen [3] -> Humour
    private List<String> listDataHeader; // header titles
    //child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {

        this.ctxt = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        stateManga = new int[2];
        for (int i = 0; i < stateManga.length; i++)
            stateManga[i] = 1;
        stateCategory = new int[4];
        for (int i = 0; i < stateCategory.length; i++)
            stateCategory[i] = 1;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader
                .get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView,
                             ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater;
            infalInflater = (LayoutInflater) this.ctxt
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_checkbox_filter_release, null);
        }
        CheckBox txtListChild = (CheckBox) convertView
                .findViewById(R.id.checkboxChoice);
        txtListChild.setText(childText);
        Log.d("Group",groupPosition+"");
        txtListChild.setTag(groupPosition);
        if (groupPosition == 0) {
            if (stateManga[childPosition] == 1)
                txtListChild.setChecked(true);
            else
                txtListChild.setChecked(false);
        }else if(groupPosition == 1){
            if (stateCategory[childPosition] == 1)
                txtListChild.setChecked(true);
            else
                txtListChild.setChecked(false);
        }


        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.ctxt
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_filter_release, null);
        }

        TextView headTitleFilter = (TextView) convertView
                .findViewById(R.id.headTitleFilter);
        headTitleFilter.setTypeface(null, Typeface.BOLD);
        headTitleFilter.setText(headerTitle);
        headTitleFilter.setTag(groupPosition);
        return convertView;
    }


    public CheckBox getCheckBox(int groupPosition, int childPosition, View convertView) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater;
            infalInflater = (LayoutInflater) this.ctxt
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_checkbox_filter_release, null);
        }
        CheckBox txtListChild = (CheckBox) convertView
                .findViewById(R.id.checkboxChoice);
        txtListChild.setText(childText);
        txtListChild.setTag(childPosition);
        return txtListChild;

    }

}
