package com.mangarelease.adam.projectmangarelease.NewReleaseSource.ContentReleaseSource;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mangarelease.adam.projectmangarelease.R;

/**
 * Created by Adam on 20/04/2017.
 */

public class EmptyFragment extends Fragment implements View.OnClickListener {
    private Button returnRelButton;
    private String sentence = "";
    private TextView textEmpty;

    public EmptyFragment(String sentence) {
        this.sentence = sentence;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_release_empty, null);
        returnRelButton = (Button) view.findViewById(R.id.returnButRel);
        returnRelButton.setOnClickListener(this); // FavoriteBut instanciation
        textEmpty = (TextView) view.findViewById(R.id.emptytv);
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