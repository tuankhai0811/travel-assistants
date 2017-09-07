package com.tuankhai.travelassistants.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tuankhai.floatingsearchview.main.FloatingSearchView;
import com.tuankhai.loopingviewpager.CircleIndicator;
import com.tuankhai.loopingviewpager.LoopViewPager;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.BaseActivity;
import com.tuankhai.travelassistants.activity.ListPlaceActivity;
import com.tuankhai.travelassistants.adapter.ProvinceAdapter;
import com.tuankhai.travelassistants.adapter.SliderPlaceAdapter;
import com.tuankhai.travelassistants.fragment.controller.PlacesController;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.travelassistants.webservice.DTO.SliderPlaceDTO;
import com.tuankhai.viewpagertransformers.ZoomOutTranformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Khai on 31/08/2017.
 */

public class PlacesFragment extends BaseFragment
        implements AppBarLayout.OnOffsetChangedListener,
        ProvinceAdapter.LayoutProvinceItemListener{
    protected String TAG = "";
    protected BaseActivity mActivity;
    public View mRootView;
    protected PlacesController placesController;
    BaseFragmentCallbacks callbacks;

    RecyclerView lvProvince;
    RecyclerView.LayoutManager layoutManagerProvince;
    ArrayList<ProvinceDTO.Province> arrProvinces;
    ProvinceAdapter adapterProvinces;

    SliderPlaceAdapter adapterSliderPlace;
    LoopViewPager viewpager;
    CircleIndicator indicator;

    FrameLayout layoutSpring, layoutSummer, layoutAutumn, layoutWinter;
    LinearLayout layoutSea, layoutAttractions, layoutCultural, layoutEntertainment;

    private String mLastQuery = "";

    //auto swipe
    int currentPage;
    int numPage;
    Timer timer;
    TimerTask task;
    final long DELAY_MS = 5000;
    final long PERIOD_MS = 5000;

    public static PlacesFragment newInstance(BaseActivity activity) {
        PlacesFragment fragment = new PlacesFragment();
        fragment.mActivity = activity;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.TAG = "PlacesFragment";
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.places_activity, container, false);
            addControls();
            addEvents();
            placesController = new PlacesController(this);
            placesController.getAllProvince();
            placesController.getSliderPlace();
        }
        //getChildFragmentManager().beginTransaction().replace(R.id.checkout_right_view, mRightFragment).commit();
        return mRootView;
    }

    private void addEvents() {
        layoutSpring = mRootView.findViewById(R.id.layout_type_spring);
        layoutSummer = mRootView.findViewById(R.id.layout_type_summer);
        layoutAutumn = mRootView.findViewById(R.id.layout_type_autumn);
        layoutWinter = mRootView.findViewById(R.id.layout_type_winter);

        layoutSea = mRootView.findViewById(R.id.layout_type_sea);
        layoutAttractions = mRootView.findViewById(R.id.layout_type_attractions);
        layoutCultural = mRootView.findViewById(R.id.layout_type_cultural);
        layoutEntertainment = mRootView.findViewById(R.id.layout_type_entertainment);

        layoutSpring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClick(view, AppContansts.INTENT_TYPE_SPRING);
            }
        });

        layoutSummer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClick(view, AppContansts.INTENT_TYPE_SUMMER);
            }
        });

        layoutAutumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClick(view, AppContansts.INTENT_TYPE_AUTUMN);
            }
        });

        layoutWinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClick(view, AppContansts.INTENT_TYPE_WINNER);
            }
        });

        layoutSea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClick(view, AppContansts.INTENT_TYPE_SEA);
            }
        });


        layoutAttractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClick(view, AppContansts.INTENT_TYPE_ATTRACTIONS);
            }
        });


        layoutEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClick(view, AppContansts.INTENT_TYPE_ENTERTAINMENT);
            }
        });

        layoutCultural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClick(view, AppContansts.INTENT_TYPE_CULTURAL);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSearchBar();
    }

    private void setupSearchBar() {
        mActivity.searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mActivity.searchView.hideProgress();
                } else {
                    mActivity.searchView.showProgress();
                }
                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mActivity.searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {

            @Override
            public void onSearchAction(String query) {
                mActivity.searchView.hideProgress();
                mLastQuery = query;
                Log.d(TAG, "onSearchAction()");
            }
        });

        mActivity.searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {
                mActivity.searchView.setSearchBarTitle(mLastQuery);
                mActivity.searchView.hideProgress();
                Log.d(TAG, "onFocusCleared()");
            }
        });

        mActivity.searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.action_change_colors) {

//                    mIsDarkSearchTheme = true;
//
//                    //demonstrate setting colors for items
//                    mActivity.searchView.setBackgroundColor(Color.parseColor("#787878"));
//                    mActivity.searchView.setHintTextColor(Color.parseColor("#e9e9e9"));
//                    mActivity.searchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
//                    mActivity.searchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
//                    mActivity.searchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
//                    mActivity.searchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
//                    mActivity.searchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        mActivity.searchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                Log.d(TAG, "onHomeClicked()");
            }
        });
    }

    private void addControls() {
        attachSearchViewActivityDrawer(mActivity.searchView);
        mActivity.appBarLayout.addOnOffsetChangedListener(this);
        lvProvince = mRootView.findViewById(R.id.lv_province);
        arrProvinces = new ArrayList<>();
        layoutManagerProvince = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapterProvinces = new ProvinceAdapter(mActivity, arrProvinces, this);
        lvProvince.setLayoutManager(layoutManagerProvince);
        lvProvince.setAdapter(adapterProvinces);
    }

    protected void attachSearchViewActivityDrawer(FloatingSearchView searchView) {
        if (callbacks != null) {
            callbacks.onAttachSearchViewToDrawer(searchView);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseFragmentCallbacks) {
            callbacks = (BaseFragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BaseFragmentCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public void setAllProvince(ProvinceDTO allProvince) {
        arrProvinces.clear();
        arrProvinces.addAll(Arrays.asList(allProvince.provinces));
        adapterProvinces.notifyDataSetChanged();
    }

    @Override
    public boolean onActivityBackPress() {
        if (!mActivity.searchView.setSearchFocused(false)) {
            return false;
        }
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mActivity.searchView.setTranslationY(verticalOffset);
    }

    public void setSliderPlace(SliderPlaceDTO data) {
        viewpager = mRootView.findViewById(R.id.viewpagerPlace);
        indicator = mRootView.findViewById(R.id.indicatorPlace);
        viewpager.setScrollDurationFactor(1500);
        adapterSliderPlace = new SliderPlaceAdapter(getActivity(), data);
        viewpager.setAdapter(adapterSliderPlace);
        viewpager.setPageTransformer(true, new ZoomOutTranformer());
        indicator.setViewPager(viewpager);
        currentPage = 0;
        numPage = data.places.length;
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                currentPage = viewpager.getCurrentItem() + 1;
                if (currentPage == numPage) {
                    currentPage = 0;
                }
                Log.e("status", currentPage+"");
                viewpager.setCurrentItem(currentPage--, true);
            }
        };
        task = new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        };
        timer = new Timer();
        timer.schedule(task, DELAY_MS);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                    timer = new Timer();
                    if (task != null) {
                        task.cancel();
                        task = null;
                        task = new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(update);
                            }
                        };
                    }
                    timer.schedule(task, DELAY_MS);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onProvinceClick(View view, ProvinceDTO.Province item) {
        Intent intent = new Intent(getContext(), ListPlaceActivity.class);
        intent.putExtra(AppContansts.INTENT_TYPE, AppContansts.INTENT_TYPE_PROVINCE);
        intent.putExtra(AppContansts.INTENT_DATA, item);
        startActivity(intent);
    }

    public void onTypeClick(View view, int type){
        Intent intent = new Intent(getContext(), ListPlaceActivity.class);
        intent.putExtra(AppContansts.INTENT_TYPE, AppContansts.INTENT_TYPE_NORMAL);
        intent.putExtra(AppContansts.INTENT_DATA, type);
        startActivity(intent);
    }

}
