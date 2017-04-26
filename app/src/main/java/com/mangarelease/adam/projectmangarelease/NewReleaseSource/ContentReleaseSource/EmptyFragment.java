package com.mangarelease.adam.projectmangarelease.NewReleaseSource.ContentReleaseSource;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mangarelease.adam.projectmangarelease.R;

/**
 * Class use when where is no result of the filter make by the user or an error with the internet connection
 * An empty page will show to replace the
 * releasefragment and avoid any error.
 */


public class EmptyFragment extends Fragment implements View.OnClickListener {
    private Button returnRelButton;
    private String sentence = "";

    public EmptyFragment(String sentence) {
        this.sentence = sentence;
    }

    public EmptyFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        @SuppressLint("InflateParams") ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_release_empty, null);
        returnRelButton = (Button) view.findViewById(R.id.returnButRel);
        returnRelButton.setOnClickListener(this); // FavoriteBut instanciation
        TextView textEmpty = (TextView) view.findViewById(R.id.emptytv);
        textEmpty.setText(sentence);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == returnRelButton.getId()) {
            getActivity().finish();
        }
    }
}