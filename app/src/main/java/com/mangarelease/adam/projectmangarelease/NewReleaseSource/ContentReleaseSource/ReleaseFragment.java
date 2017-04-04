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
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
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
    private SqLiteHelper db;
    private ImageButton favBut;
    private PageListener pageListener;

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
        pageListener = new PageListener(mp, array, favBut, getContext());
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
        // if manga already exist change the status of followed.
        // if manga do not exist create new manga and add in the favorite liste
        if (v.getId() == favBut.getId()) {

            MangaClass mg = new MangaClass();
            mg.setTitle(array.get(pageListener.getCurrentPage()).getTitleManga());
            mg.setCategory(array.get(pageListener.getCurrentPage()).getCategory());
            mg.setEditor_name("Kurokawa");
            int id = (int) db.getInstance(getContext()).createManga(mg);
            // New manga created -> add to the favorite list
            if (id != 0) {
                db.getInstance(getContext()).createFavorite(id, 1);
                this.AddTomeOfFavoriteManga(mg,id);

            } else { // manga already exist -> stop following or restart the following
                int tmp = db.getInstance(getContext()).getManga_id(mg.getTitle());
                if (db.getInstance(getContext()).isFollow(tmp) == 0) { // manga is not followed
                    db.getInstance(getContext()).updateFavorite(tmp, 1);
                    this.AddTomeOfFavoriteManga(mg,id);
                } else { // manga is followed
                    db.getInstance(getContext()).updateFavorite(tmp, 0);
                }
            }

            if ((Integer) v.getTag() == R.drawable.greystar) {
                favBut.setBackgroundResource(R.drawable.yellowstar);
                favBut.setTag(R.drawable.yellowstar);
            } else {
                favBut.setBackgroundResource(R.drawable.greystar);
                favBut.setTag(R.drawable.greystar);
            }
        }
    }


    // Create new Favorite add the current tome chosen and others if in the parseList
    private void AddTomeOfFavoriteManga(MangaClass mg,int id) {
        for (int i = 0; i < array.size(); i++) {
            if(array.get(i).getTitleManga().compareTo(mg.getTitle())==0){
                Log.d("MANGA RELEASE : ",mg.getTitle() + " : " + array.get(i).getNum_vol());
                TomeClass tome = new TomeClass();
                tome.setNum_vol(array.get(i).getNum_vol());
                tome.setDesc(array.get(i).getDesc());
                tome.setImage(array.get(i).getImage());
                tome.setManga_id(mg.getManga_id());
                db.getInstance(getContext()).createTome(tome,id);
            }
        }
    }


    /**
     * Inner class Detect if the manga of the new release are favorite or note
     */
    private static class PageListener extends ViewPager.SimpleOnPageChangeListener {
        private static int currentPage;
        private static ViewPager mypager;
        private static ArrayList<TomeClass> arrayView;
        private static ImageButton favb;
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

            currentPage = mypager.getCurrentItem();
            int id = db.getInstance(context).getManga_id(arrayView.get(currentPage).getTitleManga());
            int isFollow = db.getInstance(context).isFollow(id);
            if (isFollow == 1) {
                favb.setBackgroundResource(R.drawable.yellowstar);
                favb.setTag(R.drawable.yellowstar);
            } else {
                favb.setBackgroundResource(R.drawable.greystar);
                favb.setTag(R.drawable.greystar);
            }
        }


        public int getCurrentPage() {
            return mypager.getCurrentItem();
        }


    }
}