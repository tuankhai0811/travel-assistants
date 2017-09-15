package com.tuankhai.travelassistants.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.tuankhai.likebutton.LikeButton;
import com.tuankhai.likebutton.OnAnimationEndListener;
import com.tuankhai.likebutton.OnLikeListener;
import com.tuankhai.loopingviewpager.CircleIndicator;
import com.tuankhai.loopingviewpager.LoopViewPager;
import com.tuankhai.ratingbar.MaterialRatingBar;
import com.tuankhai.slideractivity.Slider;
import com.tuankhai.slideractivity.model.SliderConfig;
import com.tuankhai.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.adapter.PlaceNearAdapter;
import com.tuankhai.travelassistants.adapter.ReviewsAdapter;
import com.tuankhai.travelassistants.adapter.SliderImageAdapter;
import com.tuankhai.travelassistants.adapter.decoration.SpacingItemDecorationHorizontal;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.CheckDTO;
import com.tuankhai.travelassistants.webservice.DTO.FavoriteDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.DTO.ReviewDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.AddFavoriteRequest;
import com.tuankhai.travelassistants.webservice.request.AddReviewRequest;
import com.tuankhai.travelassistants.webservice.request.CheckFavoriteRequest;
import com.tuankhai.travelassistants.webservice.request.EditPlaceRequest;
import com.tuankhai.travelassistants.webservice.request.GetReviewRequest;
import com.tuankhai.travelassistants.webservice.request.RemoveFavoriteRequest;
import com.tuankhai.viewpagertransformers.ZoomOutTranformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailPlaceActivity extends AppCompatActivity implements View.OnClickListener, OnLikeListener
        , OnAnimationEndListener, MaterialRatingBar.OnRatingChangeListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public static final int REQUEST_LOGIN = 234;

    PlaceDTO.Place data;
    ReviewDTO reviewDTO;
    PlaceGoogleDTO dataGoogle;
    PlaceNearDTO dataNearFood, dataNearHotel;
    Toolbar toolbar;
    SliderConfig mConfig;

    //Slider Image
    SliderImageAdapter adapterImage;
    LoopViewPager viewpager;
    CircleIndicator indicator;

    //Recycleview Hotel
    RecyclerView lvHotel;
    ArrayList<PlaceNearDTO.Result> arrHotel;
    RecyclerView.LayoutManager layoutManagerHotel;
    PlaceNearAdapter adapterHotel;
    LinearLayout layoutHotel;

    //Recycleview Food
    RecyclerView lvFood;
    ArrayList<PlaceNearDTO.Result> arrFood;
    RecyclerView.LayoutManager layoutManagerFood;
    PlaceNearAdapter adapterFood;
    LinearLayout layoutFood;

    //Reviews
    MaterialRatingBar ratingBarSelect;
    RecyclerView lvReview;
    ArrayList<PlaceGoogleDTO.Result.Review> arrReview;
    RecyclerView.LayoutManager layoutManagerReview;
    ReviewsAdapter adapterReviews;

    //Dialog Review
    Dialog dialogReview;
    MaterialRatingBar ratingBarSelectDialog;
    EditText txt_comment_dialog;
    TextView txtRatingDialog;
    Button btnCancel, btnSend;


    LikeButton likeButton;
    MaterialRatingBar ratingBar;
    TextView txtCountReview;
    MaterialRatingBar ratingBarView;

    int currentPage;
    int numPage;
    TimerTask task;
    Timer timer;
    final long DELAY_MS = 5000;      //delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;    //time in milliseconds between successive task executions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        setContentView(R.layout.activity_detail_place);
        getData();
        initCollapsingToolbar();
        initSlider();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        initSliderImage(data.arrImage);

        addControls();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void addControls() {
        likeButton = (LikeButton) findViewById(R.id.heart_button);
        likeButton.setOnLikeListener(this);
        likeButton.setOnAnimationEndListener(this);
    }

    private void refreshFavorite() {
        if (currentUser == null) {
            setLiked(false);
        } else {
            new RequestService().load(new CheckFavoriteRequest("", data.id, currentUser.getEmail()), false, new MyCallback() {
                @Override
                public void onSuccess(Object response) {
                    super.onSuccess(response);
                    CheckDTO result = (CheckDTO) response;
                    if (result.result) {
                        setLiked(true);
                    } else {
                        setLiked(false);
                    }
                }
            }, CheckDTO.class);
        }
    }

    public void setLiked(boolean flag) {
        if (flag) {
            likeButton.setOnLikeListener(null);
            likeButton.setLiked(true);
            likeButton.setOnLikeListener(this);
        } else {
            likeButton.setOnLikeListener(null);
            likeButton.setLiked(false);
            likeButton.setOnLikeListener(this);
        }
    }

    private void getData() {
        data = (PlaceDTO.Place) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        new RequestService().load(new GetReviewRequest("", data.id), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                reviewDTO = (ReviewDTO) response;
                if (arrReview == null) {
                    arrReview = new ArrayList<>();
                    arrReview.addAll(Arrays.asList(reviewDTO.result));
                } else {
                    arrReview.addAll(Arrays.asList(reviewDTO.result));
                    refreshReview();
                }

            }
        }, ReviewDTO.class);
        new RequestService().getPlace(data.place_id, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                dataGoogle = (PlaceGoogleDTO) response;
                addControlsPlaceGoogle();
                initSliderImageGoogle();
                initReviews();
                initDialogReviews();
            }
        });
        new RequestService().nearPlace(RequestService.TYPE_PLACE_FOOD, data.location_lat, data.location_lng, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                dataNearFood = (PlaceNearDTO) response;
                if (dataNearFood.results != null && dataNearFood.results.length > 0)
                    addControlsFood();
            }
        });

        new RequestService().nearPlace(RequestService.TYPE_PLACE_HOTEL, data.location_lat, data.location_lng, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                dataNearHotel = (PlaceNearDTO) response;
                if (dataNearHotel.results != null && dataNearHotel.results.length > 0)
                    addControlsHotel();
            }
        });
    }

    private void initDialogReviews() {
        dialogReview = new Dialog(this);
        dialogReview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogReview.setContentView(R.layout.content_dialog_rating);
        dialogReview.setCanceledOnTouchOutside(false);
        dialogReview.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ratingBarSelect.setRating(0f);
            }
        });
        dialogReview.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        txt_comment_dialog = dialogReview.findViewById(R.id.txt_comment_dialog_review);
        txtRatingDialog = dialogReview.findViewById(R.id.txt_rating_dialog);
        btnCancel = dialogReview.findViewById(R.id.btn_cancel);
        btnSend = dialogReview.findViewById(R.id.btn_send);
        btnCancel.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        ratingBarSelectDialog = dialogReview.findViewById(R.id.ratingBarSelectDialog);
        ratingBarSelectDialog.setMax(5);
        ratingBarSelectDialog.setNumStars(5);
        ratingBarSelectDialog.setStepSize(1f);
        ratingBarSelectDialog.setRating(0f);
        ratingBarSelectDialog.setOnRatingChangeListener(this);
    }

    private void initReviews() {

        if (arrReview == null) {
            arrReview = new ArrayList<>();
            arrReview.addAll(dataGoogle.getReviews());
        } else {
            arrReview.addAll(dataGoogle.getReviews());
            refreshReview();
        }
    }

    private void refreshRating() {
        int count = arrReview.size();
        float sum = 0;
        for (PlaceGoogleDTO.Result.Review item : arrReview) {
            sum += item.getRating();
        }
        float result = (float) ((int) ((float) (sum / count) * 10)) / 10;
        ((TextView) findViewById(R.id.txt_rating_view)).setText(result + "");
        ratingBar.setRating(result);
        ratingBarView.setRating(result);

        Log.e("status", ""+
                data.id+
                String.valueOf(result)+
                dataGoogle.result.formatted_address+
                dataGoogle.result.formatted_phone_number+
                dataGoogle.getLocationLat()+
                dataGoogle.getLocationLng()+
                new Gson().toJson(dataGoogle.result.opening_hours)+
                dataGoogle.result.website);
        new RequestService().load(
                new EditPlaceRequest(
                        "",
                        data.id,
                        String.valueOf(result),
                        dataGoogle.result.formatted_address,
                        dataGoogle.result.formatted_phone_number,
                        dataGoogle.getLocationLat(),
                        dataGoogle.getLocationLng(),
                        new Gson().toJson(dataGoogle.result.opening_hours),
                        dataGoogle.result.website),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        Log.e("status", new Gson().toJson(response));
                    }
                }, CheckDTO.class);
    }

    private void refreshReview() {
        lvReview = (RecyclerView) findViewById(R.id.lv_reviews);
        lvReview.setNestedScrollingEnabled(false);
        layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Collections.sort(arrReview);
        adapterReviews = new ReviewsAdapter(this, arrReview);
        lvReview.setLayoutManager(layoutManagerReview);
        lvReview.setAdapter(adapterReviews);

        ((TextView) findViewById(R.id.num_comment)).setText(arrReview.size() + "");
        txtCountReview.setText(dataGoogle.countReviews() + " " + getString(R.string.txt_review));

        ratingBarSelect = (MaterialRatingBar) findViewById(R.id.ratingBarSelect);
        ratingBarSelect.setMax(5);
        ratingBarSelect.setNumStars(5);
        ratingBarSelect.setStepSize(1f);
        ratingBarSelect.setRating(0f);
        ratingBarSelect.setOnRatingChangeListener(this);
        if (currentUser == null) {
            return;
        }
        for (int i = 0; i < arrReview.size(); i++) {
            if (currentUser.getEmail().equals(arrReview.get(i).email)) {
                ratingBarSelect.setOnRatingChangeListener(null);
                ratingBarSelect.setRating(arrReview.get(i).getRating());
                ratingBarSelect.setIsIndicator(true);
            }
        }

        refreshRating();
    }

    private void addControlsFood() {
        layoutFood = (LinearLayout) findViewById(R.id.layout_list_food);
        layoutFood.setVisibility(View.VISIBLE);
        lvFood = (RecyclerView) findViewById(R.id.lv_food);
        lvFood.setNestedScrollingEnabled(false);
        layoutManagerFood = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapterFood = new PlaceNearAdapter(this, Arrays.asList(dataNearFood.results), null);
        lvFood.setLayoutManager(layoutManagerFood);
        lvFood.addItemDecoration(new SpacingItemDecorationHorizontal(Utils.dpToPx(this, 10)));
        lvFood.setAdapter(adapterFood);
        findViewById(R.id.layout_more_food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPlaceActivity.this, ListPlaceNearActivity.class);
                intent.putExtra(AppContansts.INTENT_DATA, dataNearFood);
                intent.putExtra(AppContansts.INTENT_DATA1, dataGoogle.getLocationLat());
                intent.putExtra(AppContansts.INTENT_DATA2, dataGoogle.getLocationLng());
                intent.putExtra(AppContansts.INTENT_DATA3, RequestService.TYPE_PLACE_FOOD);
                startActivity(intent);
            }
        });
    }

    private void addControlsHotel() {
        layoutHotel = (LinearLayout) findViewById(R.id.layout_list_hotel);
        layoutHotel.setVisibility(View.VISIBLE);
        lvHotel = (RecyclerView) findViewById(R.id.lv_hotel);
        lvHotel.setNestedScrollingEnabled(false);
        layoutManagerHotel = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapterHotel = new PlaceNearAdapter(this, Arrays.asList(dataNearHotel.results), null);
        lvHotel.setLayoutManager(layoutManagerHotel);
        lvHotel.addItemDecoration(new SpacingItemDecorationHorizontal(Utils.dpToPx(this, 10)));
        lvHotel.setAdapter(adapterHotel);
        findViewById(R.id.layout_more_hotel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPlaceActivity.this, ListPlaceNearActivity.class);
                intent.putExtra(AppContansts.INTENT_DATA, dataNearHotel);
                intent.putExtra(AppContansts.INTENT_DATA1, dataGoogle.getLocationLat());
                intent.putExtra(AppContansts.INTENT_DATA2, dataGoogle.getLocationLng());
                intent.putExtra(AppContansts.INTENT_DATA3, RequestService.TYPE_PLACE_HOTEL);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_boto_regular))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        refreshFavorite();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void addControlsPlaceGoogle() {
        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);
        ratingBar.invalidate();
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.1f);
        ratingBar.setRating(dataGoogle.getRating());

        txtCountReview = (TextView) findViewById(R.id.txt_num_comment);

        findViewById(R.id.layout_content_reviews).setVisibility(View.VISIBLE);
//        ((TextView) findViewById(R.id.num_comment)).setText(dataGoogle.getSizeReview() + "");
        ((TextView) findViewById(R.id.txt_rating_view)).setText(dataGoogle.getRating() + "");
        ratingBarView = (MaterialRatingBar) findViewById(R.id.ratingBarView);
        ratingBarView.invalidate();
        ratingBarView.setMax(5);
        ratingBarView.setNumStars(5);
        ratingBarView.setStepSize(0.1f);
        ratingBarView.setRating(dataGoogle.getRating());
    }

    private void initSliderImage(ArrayList<String> array) {
        if (array.size() == 0) return;
        viewpager = (LoopViewPager) findViewById(R.id.viewpagerImage);
        viewpager.setScrollDurationFactor(1500);
        adapterImage = new SliderImageAdapter(this, array);
        viewpager.setPageTransformer(true, new ZoomOutTranformer());
        viewpager.setAdapter(adapterImage);

        currentPage = 0;
        numPage = array.size();
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                currentPage = viewpager.getCurrentItem() + 1;
                if (currentPage == numPage) {
                    currentPage = 0;
                }
                viewpager.setCurrentItem(currentPage++, true);
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

    private void initSliderImageGoogle() {

        if (data.arrImage.size() > 0) return;
        ArrayList<String> arrayImage = dataGoogle.getImage();
        viewpager = (LoopViewPager) findViewById(R.id.viewpagerImage);
        viewpager.setScrollDurationFactor(1500);
        adapterImage = new SliderImageAdapter(this, arrayImage);
        viewpager.setPageTransformer(true, new ZoomOutTranformer());
        viewpager.setAdapter(adapterImage);

        currentPage = 0;
        numPage = arrayImage.size();
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                currentPage = viewpager.getCurrentItem() + 1;
                if (currentPage == numPage) {
                    currentPage = 0;
                }
                viewpager.setCurrentItem(currentPage++, true);
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

    private void initCollapsingToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.txt_title)).setText("");
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(data.getName());
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(data.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(data.getName());
                    isShow = false;
                }
            }
        });
    }

    private void initSlider() {
        mConfig = new SliderConfig.Builder()
                .primaryColor(getResources().getColor(R.color.colorPrimary))
                .secondaryColor(getResources().getColor(R.color.colorPrimary))
                .position(SliderPosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.4f)
                .edge(true)
                .edgeSize(0.4f)
                .build();
        Slider.attach(this, mConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void liked(final LikeButton likeButton) {
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(AppContansts.INTENT_DATA, REQUEST_LOGIN);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else {
            likeButton.setEnabled(false);
            new RequestService().load(new AddFavoriteRequest("", data.id, currentUser.getEmail()), false, new MyCallback() {
                @Override
                public void onSuccess(Object response) {
                    super.onSuccess(response);
                    likeButton.setEnabled(true);
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.main_content), getString(R.string.add_favorite), BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setAction("View", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(DetailPlaceActivity.this, ListPlaceActivity.class);
                            intent.putExtra(AppContansts.INTENT_TYPE, AppContansts.INTENT_TYPE_FAVORITE);
                            startActivity(intent);
                        }
                    });
                    snackbar.show();
                }
            }, FavoriteDTO.class);
        }
    }

    @Override
    public void unLiked(final LikeButton likeButton) {
        likeButton.setEnabled(false);
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(AppContansts.INTENT_DATA, REQUEST_LOGIN);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else {
            new RequestService().load(new RemoveFavoriteRequest("", data.id, currentUser.getEmail()), false, new MyCallback() {
                @Override
                public void onSuccess(Object response) {
                    super.onSuccess(response);
                    likeButton.setEnabled(true);
                    Snackbar.make(findViewById(R.id.main_content), getString(R.string.remove_favorite), BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }, FavoriteDTO.class);
        }
    }

    @Override
    public void onAnimationEnd(LikeButton likeButton) {
//        Log.e("status", "Animation End for %s" + likeButton);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                ratingBarSelect.setRating(0f);
                dialogReview.dismiss();
                break;
            case R.id.btn_send:
                ratingBarSelect.setIsIndicator(true);
                ratingBarSelect.setOnRatingChangeListener(null);
                userReview();
                break;
        }
    }

    private void userReview() {
        new RequestService().load(
                new AddReviewRequest("",
                        currentUser.getDisplayName().toString(),
                        currentUser.getEmail().toString(),
                        currentUser.getPhotoUrl().toString(),
                        data.id.toString(),
                        String.valueOf(ratingBarSelectDialog.getRating()),
                        txt_comment_dialog.getText().toString(),
                        String.valueOf(new Date().getTime())),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        dialogReview.dismiss();
                        new RequestService().load(new GetReviewRequest("", data.id), false, new MyCallback() {
                            @Override
                            public void onSuccess(Object response) {
                                super.onSuccess(response);
                                reviewDTO = (ReviewDTO) response;
                                if (arrReview == null) {
                                    arrReview = new ArrayList<>();
                                    arrReview.addAll(Arrays.asList(reviewDTO.result));
                                } else {
                                    arrReview.addAll(Arrays.asList(reviewDTO.result));
                                    refreshReview();
                                }

                            }
                        }, ReviewDTO.class);
                    }
                }, PlaceGoogleDTO.Result.Review.class);
    }

    @Override
    public void onBackPressed() {
        if (dialogReview != null & dialogReview.isShowing()) {
            ratingBarSelect.setRating(0f);
            dialogReview.dismiss();
        }
        super.onBackPressed();
    }

    @Override
    public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
        if (rating == 0f) return;
        switch (ratingBar.getId()) {
            case R.id.ratingBarSelect:
                if (currentUser == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(AppContansts.INTENT_DATA, REQUEST_LOGIN);
                    startActivityForResult(intent, REQUEST_LOGIN);
                    ratingBarSelect.setRating(0f);
                } else {
                    ratingBarSelectDialog.setRating(rating);
                    setStatusDialog(rating);
                    dialogReview.show();
                }
                break;
            case R.id.ratingBarSelectDialog:
                ratingBarSelect.setRating(rating);
                setStatusDialog(rating);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_LOGIN:
                    currentUser = mAuth.getCurrentUser();
                    refreshReview();
                    refreshFavorite();
                    break;
            }
        } else {

        }
    }

    private void setStatusDialog(float rating) {
        switch ((int) rating) {
            case 0:
                txtRatingDialog.setText("Ghét!");
                break;
            case 1:
                txtRatingDialog.setText("Ghét!");
                break;
            case 2:
                txtRatingDialog.setText("Không thích!");
                break;
            case 3:
                txtRatingDialog.setText("Tạm được!");
                break;
            case 4:
                txtRatingDialog.setText("Thích!");
                break;
            case 5:
                txtRatingDialog.setText("Rất thích!");
                break;
        }
    }

//    class GetImageSlider extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
//
//        ArrayList<String> lists;
//
//        public GetImageSlider(ArrayList<String> lists) {
//            this.lists = lists;
//        }
//
//        @Override
//        protected ArrayList<Bitmap> doInBackground(Void... voids) {
//            ArrayList<Bitmap> arrImg = new ArrayList<Bitmap>();
//            for (int i = 0; i < lists.size(); i++) {
//                try {
//                    URL url = new URL(lists.get(i));
//                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    arrImg.add(image);
//                } catch (IOException e) {
//                    System.out.println(e);
//                }
//            }
//            return arrImg;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
//            super.onPostExecute(bitmaps);
//            if (bitmaps.size() == 0) return;
//            initSliderImage(bitmaps);
//        }
//    }

}
