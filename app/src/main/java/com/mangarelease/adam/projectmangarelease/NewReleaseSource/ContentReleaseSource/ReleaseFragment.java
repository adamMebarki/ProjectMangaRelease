package com.mangarelease.adam.projectmangarelease.NewReleaseSource.ContentReleaseSource;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.MangaClass;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.SqLiteHelper;
import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.TomeClass;
import com.mangarelease.adam.projectmangarelease.R;

import java.util.ArrayList;

/**
 * Created by Adam on 06/03/2017.
 * Class ReleaseFragment : Contains the main content of the ReleaseActivity : The New Releases of manga retrieve from
 * the parsage and manage to show in a appropriate form on the phone.
 *
 */


public class ReleaseFragment extends Fragment implements View.OnClickListener {

    private ViewPager pagerView;
    private PagerAdapter pagerAdapter;
    private ArrayList<TomeClass> tomes;
    private Button returnRelButton;
    private SqLiteHelper db;
    private ImageButton favoriteRelButton;
    private PageListener pageListener;

    /**
     *
     * @param ar : ArrayList which contains the list of the new tomes retrieve from the parsage
     */
    public ReleaseFragment(ArrayList<TomeClass> ar) {
        tomes = ar;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_release, null);

        // Return and Favorite Buttons instanciation
        returnRelButton = (Button) view.findViewById(R.id.returnButRel);
        returnRelButton.setOnClickListener(this);
        favoriteRelButton = (ImageButton) view.findViewById(R.id.favButRel);
        favoriteRelButton.setOnClickListener(this);
        favoriteRelButton.setTag(R.drawable.greystar);

        // Instanciation of the ViewPage which show one tome, one by one when user will slide the screen.
        pagerView = (ViewPager) view.findViewById(R.id.pager);
        pagerView.setPageTransformer(true, new DepthPageTransformer());
        // Use of the modify Adapter which use the ScreenSlidePageFragment :
        // The Goal is to use our own Slide and Manage the position of elements like we want
        // More Information : https://developer.android.com/training/animation/screen-slide.html
        pagerAdapter = new ScreenSlidePagerAdapter(this.getChildFragmentManager(), tomes);
        pagerView.setAdapter(pagerAdapter);
        // Use of an InfinitePagerAdapter : purpose : loop on the tomes when the user slide. No End and No Begin
        // More Information : https://android-arsenal.com/details/1/1307#!package
        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(pagerAdapter);
        pagerView.setAdapter(wrappedAdapter);
        // Use of a PageListener to know which page is the current page for the Favorite Management
        pageListener = new PageListener(pagerView, tomes, favoriteRelButton, getContext());
        pageListener.onPageSelected(0);
        pagerView.addOnPageChangeListener(pageListener);
        return view;

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        // Return to the Menu Screen
        if (v.getId() == returnRelButton.getId()) {
            getActivity().finish();
        }

        // Click on the favorite Button :
        // if manga already exist in the db, it changes the status of followed : true or false
        // if manga do not exist in the create new manga and add in the favorite list and followed it automatically
        if (v.getId() == favoriteRelButton.getId()) {
            MangaClass mg = new MangaClass();
            mg.setTitle(tomes.get(pageListener.getCurrentPage()).getTitleManga());
            mg.setCategory(tomes.get(pageListener.getCurrentPage()).getCategory());
            mg.setEditor_name("Kurokawa"); // Because there is only one Publisher for the moment
            int id = (int) db.getInstance(getContext()).createManga(mg);
            // New manga created -> add to the favorite list
            if (id != 0) {
                db.getInstance(getContext()).createFavorite(id, 1);
                this.AddTomeOfFavoriteManga(mg, id); // Add others tomes too, if there are also retrieve from the parsage.

            } else { // manga already exist -> stop following or restart the following
                int tmp = db.getInstance(getContext()).getManga_id(mg.getTitle());
                if (db.getInstance(getContext()).isFollow(tmp) == 0) { // manga was not followed
                    // followed now and add the others tomes if there are.
                    db.getInstance(getContext()).updateFavorite(tmp, 1);
                    this.AddTomeOfFavoriteManga(mg, id);
                } else { // manga was followed
                    // stop the following of the manga
                    db.getInstance(getContext()).updateFavorite(tmp, 0);
                }
            }
            // Change the drawable of the star Grey or Yellow
            if ((Integer) v.getTag() == R.drawable.greystar) {
                favoriteRelButton.setBackgroundResource(R.drawable.yellowstar);
                favoriteRelButton.setTag(R.drawable.yellowstar);
            } else {
                favoriteRelButton.setBackgroundResource(R.drawable.greystar);
                favoriteRelButton.setTag(R.drawable.greystar);
            }
        }
    }


    // Create new Favorite add the current tome chosen and others if in the parseList
    // Same principle as the method AddTomeFavorite() of the ReleaseActivity
    private void AddTomeOfFavoriteManga(MangaClass mg, int id) {
        for (int i = 0; i < tomes.size(); i++) {
            if (tomes.get(i).getTitleManga().compareTo(mg.getTitle()) == 0) {
                TomeClass tome = new TomeClass();
                tome.setNum_vol(tomes.get(i).getNum_vol());
                tome.setDesc(tomes.get(i).getDesc());
                tome.setImage(tomes.get(i).getImage());
                tome.setManga_id(mg.getManga_id());
                db.getInstance(getContext()).createTome(tome, id);
            }
        }
    }


    /**
     * Inner class Detect if the manga of the new release list are  or note
     */
    private static class PageListener extends ViewPager.SimpleOnPageChangeListener {
        private static int currentPage;
        private static ViewPager viewPager;
        private static ArrayList<TomeClass> arrayView;
        private static ImageButton favoriteRelButton;
        private SqLiteHelper db;
        private Context context;

        public PageListener(ViewPager mp, ArrayList<TomeClass> array, ImageButton favbutton, Context context) {
            this.viewPager = mp;
            this.arrayView = array;
            favoriteRelButton = favbutton;
            this.context = context;

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void onPageSelected(int position) {

            currentPage = viewPager.getCurrentItem();
            int id = db.getInstance(context).getManga_id(arrayView.get(currentPage).getTitleManga());
            int isFollow = db.getInstance(context).isFollow(id);
            if (isFollow == 1) {
                favoriteRelButton.setBackgroundResource(R.drawable.yellowstar);
                favoriteRelButton.setTag(R.drawable.yellowstar);
            } else {
                favoriteRelButton.setBackgroundResource(R.drawable.greystar);
                favoriteRelButton.setTag(R.drawable.greystar);
            }
        }


        public int getCurrentPage() {
            return viewPager.getCurrentItem();
        }


    }


}
