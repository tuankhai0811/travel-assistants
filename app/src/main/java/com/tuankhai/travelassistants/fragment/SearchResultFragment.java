package com.tuankhai.travelassistants.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuankhai.travelassistants.activity.MainActivity;
import com.tuankhai.travelassistants.module.floatingsearchview.main.FloatingSearchView;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.fragment.interfaces.BaseFragmentCallbacks;

public class SearchResultFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener{
    MainActivity mActivity;

    BaseFragmentCallbacks callbacks;

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(MainActivity mActivity) {
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.mActivity = mActivity;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.TAG = "SearchResultFragment";
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_search_result, container, false);
            addControls();
            addEvents();
        }
        mActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        return mRootView;
    }

    private void addEvents() {

    }

    private void addControls() {
        attachSearchViewActivityDrawer(mActivity.searchView);
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
}
