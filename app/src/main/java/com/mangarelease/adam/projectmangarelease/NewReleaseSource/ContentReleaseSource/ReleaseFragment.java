package com.mangarelease.adam.projectmangarelease.NewReleaseSource.ContentReleaseSource;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.TomeClass;
import com.mangarelease.adam.projectmangarelease.R;

import java.util.ArrayList;

/**
 * Created by Adam on 06/03/2017.
 */
// Contains  content of new release


public class ReleaseFragment extends Fragment implements View.OnClickListener {

    private TextView tv;
    private ViewPager mp;
    private PagerAdapter mpa;
    private ArrayList<TomeClass> array;
    private Button retBut;
    private ImageButton favBut;

    public ReleaseFragment(ArrayList<TomeClass> ar) {
        array = ar;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_release, null);

        retBut = (Button) view.findViewById(R.id.returnButRel);
        retBut.setOnClickListener(this); // FavoriteBut instanciation
        favBut = (ImageButton) view.findViewById(R.id.favButRel);
        favBut.setOnClickListener(this);
        favBut.setTag(R.drawable.greystar);
        mp = (ViewPager) view.findViewById(R.id.pager);
        mp.setPageTransformer(true, new DepthPageTransformer());
        mpa = new ScreenSlidePagerAdapter(this.getChildFragmentManager(), array);
        mp.setAdapter(mpa);
        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(mpa);
        mp.setAdapter(wrappedAdapter);
        PageListener pageListener = new PageListener(mp, array, favBut, getContext());
        pageListener.onPageSelected(0);
        mp.addOnPageChangeListener(pageListener);
        return view;

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        if (v.getId() == retBut.getId()) {
            getActivity().finish();
        }
        if (v.getId() == favBut.getId()) {
            if ((Integer) v.getTag() == R.drawable.greystar) {
                favBut.setBackgroundResource(R.drawable.yellowstar);
                favBut.setTag(R.drawable.yellowstar);
            } else {
                favBut.setBackgroundResource(R.drawable.greystar);
                favBut.setTag(R.drawable.greystar);
            }
        }
    }


    public ViewPager getViewPager() {
        return mp;
    }


    /**
     * Inner class Detect if the manga of the new release are favorite or note
     */
    private static class PageListener extends ViewPager.SimpleOnPageChangeListener {
        private static int currentPage;
        private static ViewPager mypager;
        private static ArrayList<TomeClass> arrayView;
        private static ImageButton favb;
        private static boolean isP = false;
        private SqLiteHelper db;
        private Context context;

        public PageListener(ViewPager mp, ArrayList<TomeClass> array, ImageButton favbutton, Context context) {
            this.mypager = mp;
            this.arrayView = array;
            favb = favbutton;
            this.context = context;

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void onPageSelected(int position) {

            Log.d("current item", "" + mypager.getCurrentItem());
            currentPage = mypager.getCurrentItem();
            Log.d("current item", arrayView.get(currentPage).getTitleManga());

            // compare titlemanga of newRelease with array of favorite manga (title)
            // create class exclusive for favoriteManga for the whole project and use getInstance. instead
            if (isP) {
                favb.setBackgroundResource(R.drawable.yellowstar);
                favb.setTag(R.drawable.yellowstar);
            } else {
                favb.setBackgroundResource(R.drawable.greystar);
                favb.setTag(R.drawable.greystar);
            }
            isP = !isP;

        }
    }
}