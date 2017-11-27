package com.tuankhai.travelassistants.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.DetailPlaceActivity;
import com.tuankhai.travelassistants.activity.MainActivity;
import com.tuankhai.travelassistants.adapter.PlaceQueryAdapter;
import com.tuankhai.travelassistants.adapter.decoration.PlaceQueryDecoration;
import com.tuankhai.travelassistants.fragment.base.BaseFragment;
import com.tuankhai.travelassistants.fragment.controller.ResultController;
import com.tuankhai.travelassistants.fragment.interfaces.BaseFragmentCallbacks;
import com.tuankhai.travelassistants.library.floatingsearchview.main.FloatingSearchView;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.SearchResultDTO;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchResultFragment extends BaseFragment
        implements AppBarLayout.OnOffsetChangedListener,
        PlaceQueryAdapter.LayoutListPlaceQueryItemListener {

    private ResultController mController;

    private MainActivity mActivity;
    private BaseFragmentCallbacks callbacks;

    private View notify;
    private ProgressBar progressBar;
    private RecyclerView lvResult;
    private RecyclerView.LayoutManager layoutManager;
    private PlaceQueryAdapter adapter;
    private ArrayList<PlaceDTO.Place> arrPlace;

    public String mLastQuery = "";

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(MainActivity mActivity, String query) {
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.mActivity = mActivity;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.TAG = "SearchResultFragment";
        fragment.mLastQuery = query;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("status", "onCreateView:" + mLastQuery);
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_search_result, container, false);
            addControls();
            addEvents();
        }
        if (!Utils.isEmptyString(mLastQuery)) {
            querySearch(mLastQuery);
        }
        mActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        return mRootView;
    }

    private void addEvents() {

    }

    private void addControls() {
        mController = new ResultController(this);
        attachSearchViewActivityDrawer(mActivity.searchView);

        progressBar = mRootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        notify = mRootView.findViewById(R.id.layout_notify);
        notify.setVisibility(View.GONE);

        lvResult = mRootView.findViewById(R.id.lv_result);
        arrPlace = new ArrayList<>();
        layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        adapter = new PlaceQueryAdapter(mActivity, arrPlace, this);
        lvResult.setLayoutManager(layoutManager);
        lvResult.addItemDecoration(new PlaceQueryDecoration(Utils.dpToPx(mActivity, 10)));
        lvResult.setAdapter(adapter);
    }

    public void querySearch(String query) {
        if (query.length() < 2) return;
        clearData();
        mLastQuery = query;
        mController.getResult(query);
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

    protected void attachSearchViewActivityDrawer(FloatingSearchView searchView) {
        if (callbacks != null) {
            callbacks.onAttachSearchViewToDrawer(searchView);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public boolean onActivityBackPress() {
        if (mActivity.searchView.isSearchBarFocused()) {
            mActivity.searchView.clearSearchFocus();
            return true;
        }
        return false;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mActivity.searchView.setTranslationY(verticalOffset);
    }

    @Override
    public void onItemPlaceClick(View view, PlaceDTO.Place item) {
        Intent intent = new Intent(getActivity(), DetailPlaceActivity.class);
        intent.putExtra(AppContansts.INTENT_DATA, item);
        startActivity(intent);
    }

    public void refressData(SearchResultDTO resultDTO) {
        progressBar.setVisibility(View.GONE);
        arrPlace.clear();
        arrPlace.addAll(Arrays.asList(resultDTO.result.places));
        adapter.notifyDataSetChanged();

        if (arrPlace.size() == 0) notify.setVisibility(View.VISIBLE);
    }

    public void clearData() {
        notify.setVisibility(View.GONE);
        arrPlace.clear();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
    }

}
