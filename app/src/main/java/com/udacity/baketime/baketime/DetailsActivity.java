package com.udacity.baketime.baketime;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.udacity.baketime.baketime.adapter.CustomViewPagerAdapter;
import com.udacity.baketime.baketime.adapter.TabLayoutAdapter;
import com.udacity.baketime.baketime.models.RecipeResult;

import timber.log.Timber;

import static com.udacity.baketime.baketime.misc.MiscFunctions.IS_TWO_PANE;
import static com.udacity.baketime.baketime.misc.MiscFunctions.RECIPE_DETAILS;
import static com.udacity.baketime.baketime.misc.MiscFunctions.URL_NOT_FOUND;
import static com.udacity.baketime.baketime.misc.MiscFunctions.VIDEO_URL;
import static com.udacity.baketime.baketime.misc.MiscFunctions.isEmpty;

public class DetailsActivity extends AppCompatActivity implements TabLayoutAdapter.TabClickListener {


    private static final String VIDOE_PLAYER_FRAGMENT_TAG = "video_player_frag_tag";
    private final String KEY_VIDEO_URL = "Video_URL";
    private RecipeResult rr;
    private String vidUrl;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ProgressBar progressBar;
    private boolean isTwoPane;
    private RecyclerView tabRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        Bundle b = getIntent().getExtras();


        if (findViewById(R.id.twoPaneShow) != null) {
            isTwoPane = true;
            initTwoPane(b);
        } else {
            isTwoPane = false;
            initSinglePane(b);
        }
    }

    private void initSinglePane(Bundle b) {
        if (b != null && b.containsKey(RECIPE_DETAILS)) {
            String details = b.getString(RECIPE_DETAILS);

            vidUrl = b.getString(KEY_VIDEO_URL);

            Gson gson = new Gson();
            rr = gson.fromJson(details, RecipeResult.class);


            CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(this, rr);
//
//            To Use it in all fragment way use the below adapter.
//            No performance change was noticed so went with earlier adpater.
//
//            CustomViewPagerSetpsAdapter adapter = new CustomViewPagerSetpsAdapter(getSupportFragmentManager(), this, rr);

            viewPager = findViewById(R.id.viewpager);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);


            tabLayout = findViewById(R.id.tabs);
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.ingredients)));
            tabLayout.setupWithViewPager(viewPager);

            progressBar = findViewById(R.id.progressBar);
            progressBar.setScaleY(3f);
            progressBar.setMax(adapter.getCount());
            progressBar.setProgress(1);


            NestedScrollView scrollview = findViewById(R.id.scrollView);
            scrollview.fullScroll(View.FOCUS_DOWN);
            scrollview.fullScroll(ScrollView.FOCUS_UP);


            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
            viewPager.setLayoutParams(lp);

            scrollview.scrollTo(0, 0);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int state) {
                }

                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                    if (position > 0) {
                        if (!rr.getSteps().get(position - 1).isVideoURLpresent()) {
                            Timber.d(rr.getSteps().get(position - 1).getVideoURL());
                            vidUrl = rr.getSteps().get(position - 1).getVideoURL();
                            startFragment(vidUrl);

                        } else if (!rr.getSteps().get(position - 1).isThumbnailURLpresent()) {
                            Timber.d(rr.getSteps().get(position - 1).getThumbnailURL());
                            vidUrl = rr.getSteps().get(position - 1).getThumbnailURL();
                            startFragment(vidUrl);

                        } else {
                            Timber.d(getResources().getString(R.string.not_found));
                            vidUrl = URL_NOT_FOUND;
                            startFragment(vidUrl);

                        }
                    } else {
                        Timber.d(getResources().getString(R.string.position_0));
                        vidUrl = URL_NOT_FOUND;
                        endFragment();
                    }
                    progressBar.setProgress(position + 1);
                }
            });


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

    }

    private void initTwoPane(Bundle b) {
        if (b != null) {
            String details = b.getString(RECIPE_DETAILS);

            vidUrl = b.getString(KEY_VIDEO_URL);

            Gson gson = new Gson();
            rr = gson.fromJson(details, RecipeResult.class);

            CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(this, rr);

            tabRecyclerView = findViewById(R.id.tabsList);
            RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);

            tabRecyclerView.setLayoutManager(recyclerViewLayoutManager);

            TabLayoutAdapter tla = new TabLayoutAdapter(adapter, this);
            tabRecyclerView.setAdapter(tla);

            viewPager = findViewById(R.id.viewpager);
            viewPager.setAdapter(adapter);
            viewPager.postDelayed(new Runnable() {

                @Override
                public void run() {
                    viewPager.setCurrentItem(0);
                    TextView tv = findViewWithTag(0);
                    if (tv != null) {
                        setTabTextViewColor(tv);
                    }
                }
            }, 10);


            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int state) {
                }

                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                public void onPageSelected(int position) {
                    if (position > 0) {
                        if (!rr.getSteps().get(position - 1).isVideoURLpresent()) {
                            Timber.d(rr.getSteps().get(position - 1).getVideoURL());
                            vidUrl = rr.getSteps().get(position - 1).getVideoURL();
                            startFragment(vidUrl);


                        } else if (!rr.getSteps().get(position - 1).isThumbnailURLpresent()) {
                            Timber.d(rr.getSteps().get(position - 1).getThumbnailURL());
                            vidUrl = rr.getSteps().get(position - 1).getThumbnailURL();
                            startFragment(vidUrl);

                        } else {
                            Timber.d(getResources().getString(R.string.not_found));
                            vidUrl = URL_NOT_FOUND;
                            startFragment(vidUrl);

                        }
                    } else {
                        Timber.d(getResources().getString(R.string.position_0));
                        vidUrl = URL_NOT_FOUND;
                        endFragment();
                    }

                    TextView tv = findViewWithTag(position);
                    if (tv != null) {
                        setTabTextViewColor(tv);
                    }

                }
            });
        }


    }

    private void endFragment() {

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(VIDOE_PLAYER_FRAGMENT_TAG) != null) {
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            fragmentTransaction.remove(fragmentManager.findFragmentByTag(VIDOE_PLAYER_FRAGMENT_TAG)).commit();
        }

    }

    private void startFragment(String vidUrl) {

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString(VIDEO_URL, vidUrl);
        args.putBoolean(IS_TWO_PANE, isTwoPane);

        BlankFragment fragment = new BlankFragment();
        fragment.setArguments(args);

        if (fragmentManager.findFragmentByTag(VIDOE_PLAYER_FRAGMENT_TAG) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.videoPlayer, fragment, VIDOE_PLAYER_FRAGMENT_TAG).commit();
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(R.id.videoPlayer, fragment, VIDOE_PLAYER_FRAGMENT_TAG).commit();

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        viewPager.setLayoutParams(params);
        int currentOrientation = getResources().getConfiguration().orientation;
        boolean canVideoBePlayed = BlankFragment.canVideoBePlayed();
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {

            if (!isEmpty(vidUrl) && !vidUrl.equals(URL_NOT_FOUND) && canVideoBePlayed) {
                if (!isTwoPane) {
                    hideOrShowViewsSinglePane(View.GONE);
                }
            }
            Timber.d(getResources().getString(R.string.landscape));


        } else {
            Timber.d(getResources().getString(R.string.portrait));
            if (!isEmpty(vidUrl) && !vidUrl.equals(URL_NOT_FOUND) && canVideoBePlayed) {
                if (!isTwoPane) {
                    hideOrShowViewsSinglePane(View.VISIBLE);
                }
            }
        }
    }

    private void hideOrShowViewsSinglePane(int gone) {
        viewPager.setVisibility(gone);
        progressBar.setVisibility(gone);
        tabLayout.setVisibility(gone);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_VIDEO_URL, vidUrl);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onTabClickListener(int itemId, TextView tag) {
        viewPager.setCurrentItem(itemId);
        recolorTextViews();
        setTabTextViewColor(tag);
    }


    private void setTabTextViewColor(TextView tag) {
        recolorTextViews();
        tag.animate().translationX(40);
        tag.setTextColor(getResources().getColor(R.color.colorAccent));
    }


    private void recolorTextViews() {
        for (int childCount = tabRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final TabLayoutAdapter.SimpleViewHolder holder = (TabLayoutAdapter.SimpleViewHolder) tabRecyclerView.getChildViewHolder(tabRecyclerView.getChildAt(i));
            holder.textView.animate().translationX(0);
            holder.textView.setTextColor(getResources().getColor(R.color.colorText));
        }
    }

    private TextView findViewWithTag(int position) {
        TextView tv = null;
        for (int childCount = tabRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final TabLayoutAdapter.SimpleViewHolder holder = (TabLayoutAdapter.SimpleViewHolder) tabRecyclerView.getChildViewHolder(tabRecyclerView.getChildAt(i));

            if (holder.textView.getTag().equals(getResources().getString(R.string.view) + position)) {
                tv = holder.textView;
                break;
            }
        }
        return tv;
    }


}
