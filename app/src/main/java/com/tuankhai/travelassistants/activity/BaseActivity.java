package com.tuankhai.travelassistants.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tuankhai.floatingsearchview.main.FloatingSearchView;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.controller.BaseController;
import com.tuankhai.travelassistants.fragment.BaseFragment;
import com.tuankhai.travelassistants.fragment.interfaces.BaseFragmentCallbacks;
import com.tuankhai.travelassistants.utils.AppContansts;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BaseFragmentCallbacks {
    protected String TAG = "";

    public BaseActivityCallback callback;
    public BaseController mBaseController;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;

    public DrawerLayout drawerLayout;
    Toolbar toolbar;

    public AppBarLayout appBarLayout;
    public FrameLayout searchviewLayout;
    public FloatingSearchView searchView;
    private String mLastQuery = "";

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    NavigationView navigationView;
    View header;
    TextView txtLogin, txtName;
    ImageView imgPhoto;
    LinearLayout layoutLogin;
    FrameLayout layoutLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        initNavigationDrawer();
        addControls();
        addEvents();

        mBaseController.addPlaceFragment();
    }

    private void addEvents() {
        setupSearchBar();
    }

    private void addControls() {
        TAG = this.getClass().getName().toString();

        header = navigationView.getHeaderView(0);

        mBaseController = new BaseController(this);
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
                Log.e(TAG, "onSearchTextChanged()");
            }
        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {

            @Override
            public void onSearchAction(String query) {
                searchView.hideProgress();
                mLastQuery = query;
                Log.e(TAG, "onSearchAction()");
            }
        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mBaseController.addSearchFragment();
                Log.e(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {
                searchView.setSearchBarTitle(mLastQuery);
                searchView.hideProgress();
                Log.e(TAG, "onFocusCleared()");
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
                    mBaseController.addPlaceFragment();
                }
            }
        });

        searchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                Log.e(TAG, "onHomeClicked()");
            }
        });
    }

    private void initNavigationDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

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
                mBaseController.addPlaceFragment();
                drawerLayout.closeDrawers();
                break;
            case R.id.settings:
                mBaseController.addSearchFragment();
                break;
            case R.id.trash:
                Toast.makeText(getApplicationContext(), "Trash", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
            case R.id.logout:
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
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
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_boto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        currentUser = mAuth.getCurrentUser();
        updateNavUI(currentUser);
    }

    private void updateNavUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            layoutLogin.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
        } else {
            layoutLogout.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
            txtName.setText(currentUser.getDisplayName());
            Glide.with(this).load(Uri.parse(currentUser.getPhotoUrl().toString())).into(imgPhoto);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
//        List fragments = getSupportFragmentManager().getFragments();
//        BaseFragment currentFragment = (BaseFragment) fragments.get(fragments.size() - 1);
        if (searchView.isSearchBarFocused()) {
            searchView.clearSearchFocus();
        }
        BaseFragment currentFragment = (BaseFragment) fragmentManager.findFragmentByTag(AppContansts.KEY_SEARCH_FRAGMENT);
        if (currentFragment != null) {
            mBaseController.addPlaceFragment();
            return;
        }
        super.onBackPressed();
//        else {
//            List fragments = getSupportFragmentManager().getFragments();
//            currentFragment = (BaseFragment) fragments.get(fragments.size() - 1);
//        }
//        if (!currentFragment.onActivityBackPress()) {
//            super.onBackPressed();
//        }
    }

    public void setCallback(BaseActivityCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
        searchView.attachNavigationDrawerToMenuButton(drawerLayout);
    }

    public interface BaseActivityCallback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
