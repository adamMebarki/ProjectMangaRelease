package com.mangarelease.adam.projectmangarelease.NewReleaseSource.ContentReleaseSource;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.TomeClass;
import com.mangarelease.adam.projectmangarelease.R;

/**
 * Created by Adam on 12/03/2017.
 * Class which contains the picture and the resume of the current page/tome of the New Release List
 */
public class ScreenSlidePageFragment extends Fragment {

    private String image;
    private String desc;
    private String title;
    private String num;
    private WebView pictView;

    public ScreenSlidePageFragment(TomeClass currentTome) {
        this.image = currentTome.getImage();
        this.desc = currentTome.getDesc();
        this.title = currentTome.getTitleManga();
        this.title = title.replaceAll("''","'");
        this.num = currentTome.getNum_vol();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_release_slide_page, container, false);
        // Use of a WebView to show the picture
        pictView = (WebView) rootView.findViewById(R.id.picture_content);
        String html = "<style>img{height: 80%;max-width: 80%;}</style> <html><head></head><body><center><img src=\"" + image + "\"></center></body></html>";
        pictView.loadUrl("about:blank");
        pictView.loadData(html, "text/html", null);
        TextView tv_title = (TextView) rootView.findViewById(R.id.title_manga);
        TextView tv_desc = (TextView) rootView.findViewById(R.id.desc_content);
        tv_desc.setMovementMethod(new ScrollingMovementMethod());
        tv_desc.setText(desc);
        tv_title.setText(title + " - " + num);
        tv_title.setSelected(true);
        return rootView;
    }
}