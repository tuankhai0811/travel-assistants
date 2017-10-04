package com.tuankhai.travelassistants.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.MainController;
import com.tuankhai.travelassistants.fragment.BaseFragment;
import com.tuankhai.travelassistants.fragment.SearchPlaceFragment;
import com.tuankhai.travelassistants.fragment.SearchResultFragment;
import com.tuankhai.travelassistants.fragment.interfaces.BaseFragmentCallbacks;
import com.tuankhai.travelassistants.location.LocationHelper;
import com.tuankhai.travelassistants.module.floatingsearchview.main.FloatingSearchView;
import com.tuankhai.travelassistants.utils.AppContansts;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchPlaceFragment.ItemTypeSearchOnClickListener,
        BaseFragmentCallbacks, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private BaseActivityCallback callback;
    private MainController mMainController;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;

    public DrawerLayout drawerLayout;
    private Toolbar toolbar;

    public AppBarLayout appBarLayout;
    protected FrameLayout searchviewLayout;
    public FloatingSearchView searchView;
    private String mLastQuery = "";

    private NavigationView navigationView;
    private View header;
    protected TextView txtLogin, txtName;
    private ImageView imgPhoto;
    private LinearLayout layoutLogin;
    private FrameLayout layoutLogout;

    //Location
    private LocationHelper locationHelper;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationDrawer();
        initLocationHelper();
        addControls();
        setupSearchBar();

        mMainController.addPlaceFragment();
    }

    private boolean initLocation() {
        if (locationHelper.mGoogleApiClient == null || !locationHelper.mGoogleApiClient.isConnecting()) {
            locationHelper.buildGoogleApiClient();
        }
        if (locationHelper.checkpermission()) {
            if (locationHelper.checkPlayServices()) {
                return true;
            }
        }
        return false;
    }

    private void initLocationHelper() {
        locationHelper = new LocationHelper(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.message_enable_location));
        builder.setTitle(getString(R.string.app_name));
        builder.setPositiveButton(getString(R.string.action_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
    }

    private void addControls() {
        mMainController = new MainController(this);

        searchviewLayout = (FrameLayout) findViewById(R.id.layout_search_view);
        searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);

        txtLogin = header.findViewById(R.id.txt_login_nav);
        imgPhoto = header.findViewById(R.id.img_photo_nav);
        txtName = header.findViewById(R.id.txt_name_nav);
        layoutLogin = header.findViewById(R.id.nav_header_login);
        layoutLogout = header.findViewById(R.id.nav_header_logout);
        layoutLogin.setVisibility(View.GONE);
        layoutLogout.setVisibility(View.VISIBLE);
    }

    private void setupSearchBar() {
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.hideProgress();
                } else {
                    searchView.showProgress();
                }
                logError("SearchView", "onSearchTextChanged");
            }
        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {

            @Override
            public void onSearchAction(String query) {
                searchView.hideProgress();
                mLastQuery = query;
                logError("SearchView", "onSearchAction");
                if (mMainController.curFragment.getClass().getName().equals(SearchResultFragment.class.getName())) {
                    ((SearchResultFragment) mMainController.curFragment).querySearch(mLastQuery);
                } else {
                    mMainController.addResultFragment(mLastQuery);
                    //((SearchResultFragment) mMainController.curFragment).querySearch(mLastQuery);
                }
            }
        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                initLocation();
                mMainController.addSearchFragment();
                logError("SearchView", "onFocus");
            }

            @Override
            public void onFocusCleared() {
                searchView.setSearchBarTitle(mLastQuery);
                searchView.hideProgress();
                logError("SearchView", "onFocusCleared");
            }
        });

        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
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
                    Toast.makeText(getApplicationContext(), item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        searchView.setOnBackSearchButtonListener(new FloatingSearchView.onBackSearchButtonListener() {
            @Override
            public void onBackClick() {
                BaseFragment currentFragment = (BaseFragment) fragmentManager.findFragmentByTag(AppContansts.KEY_SEARCH_FRAGMENT);
                if (currentFragment != null) {
                    mMainController.addPlaceFragment();
                }
            }
        });

        searchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                logError("SearchView", "onHomeClicked");
            }
        });

        searchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                logError("SearchView", "onClearSearchClicked");
            }
        });
    }

    private void initNavigationDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        header = navigationView.getHeaderView(0);

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.drawer_open,
                        R.string.drawer_close) {

                    @Override
                    public void onDrawerClosed(View v) {
                        super.onDrawerClosed(v);
                    }

                    @Override
                    public void onDrawerOpened(View v) {
                        super.onDrawerOpened(v);
                    }
                };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.home:
                mMainController.addPlaceFragment();
                break;
            case R.id.nav_menu_favorite:
                if (mUser == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(AppContansts.INTENT_DATA, AppContansts.REQUEST_LOGIN);
                    startActivityForResult(intent, AppContansts.REQUEST_LOGIN);
                    drawerLayout.closeDrawers();
                    return false;
                }
                Intent intentFavorite = new Intent(this, ListPlaceActivity.class);
                intentFavorite.putExtra(AppContansts.INTENT_TYPE, AppContansts.INTENT_TYPE_FAVORITE);
                startActivity(intentFavorite);
                break;
            case R.id.trash:
                Toast.makeText(getApplicationContext(), "Trash", Toast.LENGTH_SHORT).show();
                mMainController.addSearchFragment();
                break;
            case R.id.logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        unCheckItemMenu(navigationView.getMenu());
        updateNavUI(mUser);
    }

    private void unCheckItemMenu(Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                // Un check sub menu items
                unCheckItemMenu(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }

    private void updateNavUI(FirebaseUser mUser) {
        if (mUser == null) {
            layoutLogin.setVisibility(View.GONE);
            layoutLogout.setVisibility(View.VISIBLE);
        } else {
            layoutLogout.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
            txtName.setText(mUser.getDisplayName());
            Glide.with(this).load(Uri.parse(mUser.getPhotoUrl().toString())).into(imgPhoto);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchView.mSearchInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.mSearchInput.getWindowToken(), 0);
            }
        }, 10);
        mLastQuery = "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
        locationHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchBarFocused()) {
            searchView.clearSearchFocus();
        }
        BaseFragment currentFragment = (BaseFragment) fragmentManager.findFragmentByTag(AppContansts.KEY_SEARCH_FRAGMENT);
        if (currentFragment != null) {
            mMainController.addPlaceFragment();
            return;
        }
        super.onBackPressed();
    }

    public void setCallback(BaseActivityCallback callback) {
        this.callback = callback;
    }

    public void gotoListNearPlace(int type) {
        if (locationHelper.checkpermission()) {
            Location location = locationHelper.getLocation();
            if (location == null) {
                alertDialog.show();
            } else {
                Intent intent = new Intent(this, ListPlaceNearActivity.class);
                intent.putExtra(AppContansts.INTENT_DATA1, locationHelper.getLatitude());
                intent.putExtra(AppContansts.INTENT_DATA2, locationHelper.getLongitude());
                intent.putExtra(AppContansts.INTENT_DATA3, type);
                startActivity(intent);
                Log.e("status", locationHelper.mLastLocation.getLatitude() +
                        "," +
                        locationHelper.mLastLocation.getLongitude());
            }
        }
    }

    @Override
    public void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
        searchView.attachNavigationDrawerToMenuButton(drawerLayout);
    }

    @Override
    public void TypeAtmClick() {
        gotoListNearPlace(AppContansts.INTENT_TYPE_ATM);
    }

    @Override
    public void TypeRestaurantClick() {
        gotoListNearPlace(AppContansts.INTENT_TYPE_FOOD);
    }

    @Override
    public void TypeHotelClick() {
        gotoListNearPlace(AppContansts.INTENT_TYPE_HOTEL);
    }

    @Override
    public void TypeGasStationClick() {
        gotoListNearPlace(AppContansts.INTENT_TYPE_GAS_STATION);
    }

    @Override
    public void TypeDrinksClick() {
        gotoListNearPlace(AppContansts.INTENT_TYPE_DRINK);
    }

    @Override
    public void TypeHospitalClick() {
        gotoListNearPlace(AppContansts.INTENT_TYPE_HOSPITAL);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        logError(mTAG,
                "Connection failed: ConnectionResult.getErrorCode() = "
                        + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        // Once connected with google api, get the location
        locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        locationHelper.connectApiClient();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public interface BaseActivityCallback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
