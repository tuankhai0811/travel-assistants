package com.tuankhai.travelassistants.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tuankhai.floatingsearchview.main.FloatingSearchView;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.BaseActivity;
import com.tuankhai.travelassistants.adapter.FragmentPlaceMainAdapter;
import com.tuankhai.travelassistants.adapter.decoration.GridSpacingItemDecoration;
import com.tuankhai.travelassistants.fragment.controller.PlacesController;
import com.tuankhai.travelassistants.utils.Utils;

/**
 * Created by Khai on 31/08/2017.
 */

public class PlacesFragment extends BaseFragment
        implements AppBarLayout.OnOffsetChangedListener {
    protected String TAG = "";
    protected BaseActivity mActivity;
    public View mRootView;
    protected PlacesController placesController;
    BaseFragmentCallbacks callbacks;

    private String mLastQuery = "";

    RecyclerView lvMain;
    FragmentPlaceMainAdapter adapterMain;
    RecyclerView.LayoutManager layoutManagerMain;

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
            placesController = new PlacesController(this);

            adapterMain = new FragmentPlaceMainAdapter(mActivity);
            layoutManagerMain = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            lvMain.addItemDecoration(new GridSpacingItemDecoration(1, Utils.dpToPx(mActivity, getResources().getInteger(R.integer.spacingItemDecoration)), false));
            lvMain.setLayoutManager(layoutManagerMain);
            lvMain.setAdapter(adapterMain);
        }
        //getChildFragmentManager().beginTransaction().replace(R.id.checkout_right_view, mRightFragment).commit();
        return mRootView;
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

        lvMain = mRootView.findViewById(R.id.lv_fragment_place);
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

}
