package com.tuankhai.travelassistants.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tuankhai.floatingsearchview.main.FloatingSearchView;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.BaseActivity;
import com.tuankhai.travelassistants.fragment.interfaces.BaseFragmentCallbacks;

public class SearchPlaceFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener,
        View.OnClickListener {
    BaseActivity mActivity;

    BaseFragmentCallbacks callbacks;

    LinearLayout layoutAtm, layoutRestaurant, layoutHotel;

    ItemTypeSearchOnClickListener itemListener;

    public void setItemListener(ItemTypeSearchOnClickListener listener) {
        this.itemListener = listener;
    }

    public SearchPlaceFragment() {
    }

    public static SearchPlaceFragment newInstance(BaseActivity mActivity) {
        SearchPlaceFragment fragment = new SearchPlaceFragment();
        fragment.setItemListener(mActivity);
        fragment.mActivity = mActivity;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.TAG = "SearchPlaceFragment";
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
            mRootView = inflater.inflate(R.layout.fragment_search_place, container, false);
            addControls();
            addEvents();
        }
        mActivity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        return mRootView;
    }

    private void addEvents() {
        layoutAtm.setOnClickListener(this);
        layoutRestaurant.setOnClickListener(this);
        layoutHotel.setOnClickListener(this);
    }

    private void addControls() {
        attachSearchViewActivityDrawer(mActivity.searchView);
        layoutAtm = mRootView.findViewById(R.id.layout_atm);
        layoutRestaurant = mRootView.findViewById(R.id.layout_restaurant);
        layoutHotel = mRootView.findViewById(R.id.layout_hotel);
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
    public void onClick(View view) {
        if (itemListener == null) return;
        switch (view.getId()) {
            case R.id.layout_atm:
                itemListener.TypeAtmClick();
                break;
            case R.id.layout_restaurant:
                itemListener.TypeRestaurantClick();
                break;
            case R.id.layout_hotel:
                itemListener.TypeHotelClick();
                break;
        }
    }

    public interface ItemTypeSearchOnClickListener {
        void TypeAtmClick();

        void TypeRestaurantClick();

        void TypeHotelClick();
    }
}
