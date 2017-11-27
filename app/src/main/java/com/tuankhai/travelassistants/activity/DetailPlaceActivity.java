package com.tuankhai.travelassistants.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.base.BaseActivity;
import com.tuankhai.travelassistants.activity.controller.DetailPlaceController;
import com.tuankhai.travelassistants.adapter.PlaceNearAdapter;
import com.tuankhai.travelassistants.adapter.ReviewsAdapter;
import com.tuankhai.travelassistants.adapter.ScheduleListAdapter;
import com.tuankhai.travelassistants.adapter.SliderImageAdapter;
import com.tuankhai.travelassistants.adapter.decoration.SpacingItemDecorationHorizontal;
import com.tuankhai.travelassistants.customTab.CustomTabActivityHelper;
import com.tuankhai.travelassistants.library.likebutton.LikeButton;
import com.tuankhai.travelassistants.library.likebutton.OnAnimationEndListener;
import com.tuankhai.travelassistants.library.likebutton.OnLikeListener;
import com.tuankhai.travelassistants.library.loopingviewpager.CircleIndicator;
import com.tuankhai.travelassistants.library.loopingviewpager.LoopViewPager;
import com.tuankhai.travelassistants.library.ratingbar.MaterialRatingBar;
import com.tuankhai.travelassistants.library.readmore.ReadMoreTextView;
import com.tuankhai.travelassistants.library.slideractivity.Slider;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.library.viewpagertransformers.DefaultTransformer;
import com.tuankhai.travelassistants.library.viewpagertransformers.ZoomOutTranformer;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;
import com.tuankhai.travelassistants.webservice.DTO.AddSchedulePlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.CheckerDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.DTO.ReviewDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.EditPlaceRequest;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DetailPlaceActivity extends BaseActivity
        implements View.OnClickListener,
        OnLikeListener,
        OnAnimationEndListener,
        MaterialRatingBar.OnRatingChangeListener,
        OnMapReadyCallback, PlaceNearAdapter.LayoutListPlaceNearItemListener,
        ReviewsAdapter.OnItemReviewActionListener {

    private DetailPlaceController mController;

    private PlaceDTO.Place data;
    private ReviewDTO reviewDTO;
    private PlaceGoogleDTO dataGoogle;
    private PlaceNearDTO dataNearFood, dataNearHotel;
    private LatLng location;
    private Toolbar toolbar;
    private ReadMoreTextView mDescription;

    //Slider Image
    private SliderImageAdapter adapterImage;
    private LoopViewPager viewpager;
    private CircleIndicator indicator;

    //Recycleview Hotel
    private RecyclerView lvHotel;
    private ArrayList<PlaceNearDTO.Result> arrHotel;
    private RecyclerView.LayoutManager layoutManagerHotel;
    private PlaceNearAdapter adapterHotel;
    private LinearLayout layoutHotel;

    //Recycleview Food
    private RecyclerView lvFood;
    private ArrayList<PlaceNearDTO.Result> arrFood;
    private RecyclerView.LayoutManager layoutManagerFood;
    private PlaceNearAdapter adapterFood;
    private LinearLayout layoutFood;

    //Reviews
    private MaterialRatingBar ratingBarSelect;
    private RecyclerView lvReview;
    private ArrayList<PlaceGoogleDTO.Result.Review> arrReview;
    private RecyclerView.LayoutManager layoutManagerReview;
    private ReviewsAdapter adapterReviews;

    //Dialog Review
    private Dialog dialogReview;
    private MaterialRatingBar ratingBarSelectDialog;
    private EditText txt_comment_dialog;
    private TextView txtRatingDialog;
    private Button btnCancel, btnSend;
    private boolean isAddNew = true;

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private ProgressBar progressBar4;
    private ProgressBar progressBar5;

    private LikeButton likeButton;
    private MaterialRatingBar ratingBar;
    private MaterialRatingBar ratingBarView;

    //Maps
    private MapView mapView;
    private GoogleMap mMap;

    private int currentPage;
    private int numPage;
    private TimerTask task;
    private Timer timer;
    private final long DELAY_MS = 5000;      //delay in milliseconds before task is to be executed
    private final long PERIOD_MS = 5000;    //time in milliseconds between successive task executions.

    //Dialog add schedule
    Dialog dialogSchedule;
    Button btnCancelDialogSchedule, btnCreateDialogSchedule;
    TextView txtFromDate, txtToDate, txtNote, txtSchedule;
    ImageView btnNewSchedule;
    Spinner lvSchedule;
    Calendar current = Calendar.getInstance();
    Calendar fromDate = Calendar.getInstance();
    Calendar toDate = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ScheduleListAdapter adapterSchedule;
    ArrayList<AddScheduleDTO.Schedule> arrSchedule;
    AddScheduleDTO.Schedule mSchedule;

    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        setContentView(R.layout.activity_detail_place);
        addControls();
        getData();
    }

    private void addControls() {
        mController = new DetailPlaceController(this);
        data = (PlaceDTO.Place) getIntent().getSerializableExtra(AppContansts.INTENT_DATA);
        location = new LatLng(Double.parseDouble(data.getLocationLat()),
                Double.parseDouble(data.getLocationLng()));

        likeButton = (LikeButton) findViewById(R.id.heart_button);
        likeButton.setOnLikeListener(this);
        likeButton.setOnAnimationEndListener(this);

        mDescription = (ReadMoreTextView) findViewById(R.id.txt_description);
        mDescription.setText(data.description);

        initProgress();
        initInformation();
        initStaticMaps();
        initCollapsingToolbar();
        initSlider();
        initDialogSchedule();
    }

    private void initDialogSchedule() {
        btnAdd = (FloatingActionButton) findViewById(R.id.btn_add_schedule);
        btnAdd.setOnClickListener(this);

        //Dialog new
        dialogSchedule = new Dialog(this, Utils.getAnimDialog(this));
        dialogSchedule.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSchedule.setContentView(R.layout.content_dialog_create_schedule_place);
        dialogSchedule.setCanceledOnTouchOutside(false);
        dialogSchedule.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnCancelDialogSchedule = dialogSchedule.findViewById(R.id.btn_cancel_schedule);
        btnCreateDialogSchedule = dialogSchedule.findViewById(R.id.btn_create_schedule);
        txtNote = dialogSchedule.findViewById(R.id.txt_note);
        txtFromDate = dialogSchedule.findViewById(R.id.txt_from_date);
        txtToDate = dialogSchedule.findViewById(R.id.txt_to_date);
        lvSchedule = dialogSchedule.findViewById(R.id.lv_schedule);
        txtSchedule = dialogSchedule.findViewById(R.id.txt_schedule);
        btnNewSchedule = dialogSchedule.findViewById(R.id.btn_new_schedule);
        txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
        txtFromDate.setText(simpleDateFormat.format(fromDate.getTime()));
        arrSchedule = new ArrayList<>();
        adapterSchedule = new ScheduleListAdapter(this, 0, arrSchedule);
//        lvSchedule.setPrompt("Chọn lịch trình...");
        lvSchedule.setAdapter(adapterSchedule);
        btnCancelDialogSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSchedule.dismiss();
            }
        });

        btnNewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPlaceActivity.this, ScheduleActivity.class);
                intent.putExtra(AppContansts.INTENT_DATA, AppContansts.REQUEST_ADD_SCHEDULE);
                startActivityForResult(intent, AppContansts.REQUEST_ADD_SCHEDULE);
                dialogSchedule.dismiss();
            }
        });

        lvSchedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrSchedule.size() != 0) {
                    mSchedule = arrSchedule.get(i);
                    fromDate.setTime(mSchedule.getStart());
                    toDate.setTime(mSchedule.getEnd());
                    txtFromDate.setText(simpleDateFormat.format(fromDate.getTime()));
                    txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtNote.getWindowToken(), 0);
                showDialogPickerFromDate();
            }
        });

        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtNote.getWindowToken(), 0);
                showDialogPickerToDate();
            }
        });

        btnCreateDialogSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSchedule == null) {
                    Utils.showFaildToast(DetailPlaceActivity.this, "Bạn chưa có lịch trình, vui lòng tạo một lịch trình mới trước");
                } else {
                    mController.createSchedule(
                            mUser.getEmail(),
                            mSchedule.id,
                            mSchedule.name,
                            data.id,
                            fromDate.getTimeInMillis(),
                            toDate.getTimeInMillis(),
                            txtNote.getText().toString());
                }
            }
        });
    }

    private void showDialogPickerToDate() {
        int mYear = toDate.get(Calendar.YEAR);
        int mMonth = toDate.get(Calendar.MONTH);
        int mDay = toDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                toDate.set(Calendar.YEAR, year);
                toDate.set(Calendar.MONTH, month);
                toDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                toDate.set(Calendar.HOUR_OF_DAY, 23);
                toDate.set(Calendar.MINUTE, 59);
                toDate.set(Calendar.SECOND, 59);
                txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
                if (toDate.getTimeInMillis() < mSchedule.getStart().getTime() || toDate.getTimeInMillis() > mSchedule.getEnd().getTime()) {
                    txtToDate.setError("Thời gian không đúng!");
                    Utils.showFaildToast(getApplicationContext(), "Không thể chọn ngày ngoài lịch trình đã chọn!");
                } else if (fromDate.getTimeInMillis() >= toDate.getTimeInMillis()) {
                    txtToDate.setError("Thời gian không đúng!");
                    Utils.showFaildToast(getApplicationContext(), "Ngày kết thúc phải sau ngày bắt đầu!");
                } else {
                    txtToDate.setError(null);
                }
            }
        }, mYear, mMonth, mDay);
        pickerDialog.setTitle("Đến ngày");
        pickerDialog.show();
    }

    private void showDialogPickerFromDate() {
        int mYear = fromDate.get(Calendar.YEAR);
        int mMonth = fromDate.get(Calendar.MONTH);
        int mDay = fromDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fromDate.set(Calendar.YEAR, year);
                fromDate.set(Calendar.MONTH, month);
                fromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fromDate.set(Calendar.HOUR_OF_DAY, 0);
                fromDate.set(Calendar.MINUTE, 0);
                fromDate.set(Calendar.SECOND, 0);
                txtFromDate.setText(simpleDateFormat.format(fromDate.getTime()));
                if (fromDate.getTimeInMillis() < mSchedule.getStart().getTime() || fromDate.getTimeInMillis() > mSchedule.getEnd().getTime()) {
                    txtFromDate.setError("Thời gian không đúng!");
                    Utils.showFaildToast(getApplicationContext(), "Không thể chọn ngày ngoài lịch trình đã chọn!");
                } else {
                    txtFromDate.setError(null);
                    if (fromDate.getTimeInMillis() > toDate.getTimeInMillis()) {
                        toDate.set(Calendar.YEAR, year);
                        toDate.set(Calendar.MONTH, month);
                        toDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        toDate.set(Calendar.HOUR_OF_DAY, 23);
                        toDate.set(Calendar.MINUTE, 59);
                        toDate.set(Calendar.SECOND, 59);
                        txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
                    }
                }
            }
        }, mYear, mMonth, mDay);
        pickerDialog.setTitle("Từ ngày");
        pickerDialog.show();
    }

    private void refreshFavorite() {
        if (mUser == null) {
            setLiked(false);
        } else {
            mController.checkIsFavorite(data.id, mUser.getEmail());
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
        mController.getLocalReviews(data.id);
        mController.getGooglePlaceDetail(data.place_id);
        mController.getNearFoodPlace(data.getLocationLat(), data.getLocationLng());
        mController.getNearHotelPlace(data.getLocationLat(), data.getLocationLng());
    }

    private void initProgress() {
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar_1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar_2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar_3);
        progressBar4 = (ProgressBar) findViewById(R.id.progressBar_4);
        progressBar5 = (ProgressBar) findViewById(R.id.progressBar_5);

        progressBar1.setMax(100);
        progressBar2.setMax(100);
        progressBar3.setMax(100);
        progressBar4.setMax(100);
        progressBar5.setMax(100);
    }

    private void initInformation() {
        ((TextView) findViewById(R.id.txt_address)).setText(data.address);
        if (!Utils.isEmptyString(data.phone)) {
            findViewById(R.id.layout_tel).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txt_tel)).setText(data.phone);
            findViewById(R.id.txt_tel).setOnClickListener(this);
        }
        if (!Utils.isEmptyString(data.website)) {
            findViewById(R.id.layout_website).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txt_website)).setText(data.website);
            findViewById(R.id.txt_website).setOnClickListener(this);
        }
        findViewById(R.id.layout_address).setOnClickListener(this);
    }

    private void initStaticMaps() {
        findViewById(R.id.layout_static_maps).setVisibility(View.VISIBLE);
        mapView = (MapView) findViewById(R.id.lite_listrow_map);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setCompassEnabled(true);
    }

    private void initDialogReviews() {
        dialogReview = new Dialog(this, Utils.getAnimDialog(this));
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
        }
        refreshReview();
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
        updatePlace(result);
    }

    public void updatePlace(Float rating) {
        new RequestService().load(
                new EditPlaceRequest(
                        "",
                        data.id,
                        String.valueOf(rating),
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
                    }
                }, CheckerDTO.class);
    }

    public void refreshProgressBar() {
        int size = arrReview.size();
        for (int k = 1; k < 6; k++) {
            int count = 0;
            for (int i = 0; i < size; i++) {
                if ((int) arrReview.get(i).getRating() == k) {
                    count++;
                }
            }
            switch (k) {
                case 1:
                    Double progress1 = (double) count / (double) size;
                    progressBar1.setProgress((int) (progress1 * 100));
                    break;
                case 2:
                    Double progress2 = (double) count / (double) size;
                    progressBar2.setProgress((int) (progress2 * 100));
                    break;
                case 3:
                    Double progress3 = (double) count / (double) size;
                    progressBar3.setProgress((int) (progress3 * 100));
                    break;
                case 4:
                    Double progress4 = (double) count / (double) size;
                    progressBar4.setProgress((int) (progress4 * 100));
                    break;
                case 5:
                    Double progress5 = (double) count / (double) size;
                    progressBar5.setProgress((int) (progress5 * 100));
                    break;
            }
        }
    }

    private void refreshReview() {
        lvReview = (RecyclerView) findViewById(R.id.lv_reviews);
        lvReview.setNestedScrollingEnabled(false);
        layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Collections.sort(arrReview);
        adapterReviews = new ReviewsAdapter(this, arrReview, this);
        adapterReviews.setAuth(mUser);
        lvReview.setLayoutManager(layoutManagerReview);
        lvReview.setAdapter(adapterReviews);

        ((TextView) findViewById(R.id.num_comment)).setText(arrReview.size() + "");
        ((TextView) findViewById(R.id.txt_num_comment)).setText(arrReview.size() + " " + getString(R.string.txt_review));

        ratingBarSelect = (MaterialRatingBar) findViewById(R.id.ratingBarSelect);
        ratingBarSelect.setMax(5);
        ratingBarSelect.setNumStars(5);
        ratingBarSelect.setStepSize(1f);
        ratingBarSelect.setRating(0f);
        ratingBarSelect.setOnRatingChangeListener(this);
        refreshProgressBar();
        if (mUser == null) {
            return;
        }
        boolean flag = true;
        for (int i = 0; i < arrReview.size(); i++) {
            if (mUser.getEmail().equals(arrReview.get(i).email)) {
                flag = false;
                ratingBarSelect.setOnRatingChangeListener(null);
                ratingBarSelect.setRating(arrReview.get(i).getRating());
                ratingBarSelect.setIsIndicator(true);
                break;
            }
        }
        if (flag) {
            ratingBarSelect.setOnRatingChangeListener(this);
            ratingBarSelect.setRating(0);
            ratingBarSelect.setIsIndicator(false);
        }
        refreshRating();
    }

    private void addControlsFood() {
        layoutFood = (LinearLayout) findViewById(R.id.layout_list_food);
        layoutFood.setVisibility(View.VISIBLE);
        lvFood = (RecyclerView) findViewById(R.id.lv_food);
        lvFood.setNestedScrollingEnabled(false);
        layoutManagerFood = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        adapterFood = new PlaceNearAdapter(this, Arrays.asList(dataNearFood.results), this);
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
                intent.putExtra(AppContansts.INTENT_DATA3, AppContansts.INTENT_TYPE_FOOD);
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
        adapterHotel = new PlaceNearAdapter(this, Arrays.asList(dataNearHotel.results), this);
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
                intent.putExtra(AppContansts.INTENT_DATA3, AppContansts.INTENT_TYPE_HOTEL);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.layout_static_maps).setOnClickListener(this);
        findViewById(R.id.layout_address).setOnClickListener(this);
        initSliderImage(data.arrImage);
        refreshFavorite();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            timer.cancel();
            timer.purge();
            timer = null;
            task.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (viewpager == null) return;
            viewpager.setOnPageChangeListener(null);
        }
    }

    private void addControlsPlaceGoogle() {
        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);
        ratingBar.invalidate();
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(0.1f);
        ratingBar.setRating(dataGoogle.getRating());

        findViewById(R.id.layout_content_reviews).setVisibility(View.VISIBLE);
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
        viewpager.setScrollDurationFactor(1000);
        adapterImage = new SliderImageAdapter(this, array);
        viewpager.setPageTransformer(true, new DefaultTransformer());
        viewpager.setAdapter(adapterImage);

        currentPage = 0;
        numPage = array.size();
        final Handler handler = new Handler();
        final Runnable update = new StaticRunnable(this);
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
        viewpager.setScrollDurationFactor(1000);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

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
        SliderConfig mConfig = new SliderConfig.Builder()
                .primaryColor(getResources().getColor(R.color.colorPrimary))
                .secondaryColor(getResources().getColor(R.color.colorPrimary))
                .position(SliderPosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.2f)
                .edge(true)
                .edgeSize(0.2f)
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
        if (mUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(AppContansts.INTENT_DATA, AppContansts.REQUEST_LOGIN);
            startActivityForResult(intent, AppContansts.REQUEST_LOGIN);
        } else {
            likeButton.setEnabled(false);
            mController.addFavorite(data.id, mUser.getEmail());
        }
    }

    @Override
    public void unLiked(final LikeButton likeButton) {
        if (mUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(AppContansts.INTENT_DATA, AppContansts.REQUEST_LOGIN);
            startActivityForResult(intent, AppContansts.REQUEST_LOGIN);
        } else {
            likeButton.setEnabled(false);
            mController.removeFavorite(data.id, mUser.getEmail());
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
                if (isAddNew) {
                    ratingBarSelect.setIsIndicator(true);
                    ratingBarSelect.setOnRatingChangeListener(null);
                    mController.sendDataReview(
                            mUser.getDisplayName().toString(),
                            mUser.getEmail().toString(),
                            mUser.getPhotoUrl().toString(),
                            data.id,
                            String.valueOf(ratingBarSelectDialog.getRating()),
                            txt_comment_dialog.getText().toString(),
                            String.valueOf(new Date().getTime()));
                } else {
                    mController.editReviews(
                            mUser,
                            data.id,
                            String.valueOf(ratingBarSelectDialog.getRating()),
                            txt_comment_dialog.getText().toString(),
                            String.valueOf(new Date().getTime()));
                }
                break;
            case R.id.txt_website:
                CustomTabActivityHelper.openCustomTab(this, Uri.parse(data.website));
                break;
            case R.id.txt_tel:
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.phone));
                startActivity(i);
                break;
            case R.id.btn_add_schedule:
                if (mUser == null) {
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    loginIntent.putExtra(AppContansts.INTENT_DATA, AppContansts.REQUEST_LOGIN);
                    startActivity(loginIntent);
                } else {
                    mController.getSchedule(mUser);
                }
                break;
        }
    }

    private void userReview() {

    }

    @Override
    public void onBackPressed() {
        if (dialogReview == null) {
            super.onBackPressed();
            return;
        }
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
                if (mUser == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(AppContansts.INTENT_DATA, AppContansts.REQUEST_LOGIN);
                    startActivityForResult(intent, AppContansts.REQUEST_LOGIN);
                    ratingBarSelect.setRating(0f);
                } else {
                    ratingBarSelectDialog.setRating(rating);
                    setStatusDialog(rating);
                    txt_comment_dialog.setText("");
                    isAddNew = true;
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
                case AppContansts.REQUEST_LOGIN:
                    mUser = mAuth.getCurrentUser();
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

    @Override
    public void onItemPlaceNearClick(View view, PlaceNearDTO.Result item) {
        Intent intent = new Intent(this, DetailPlaceNearActivity.class);
        intent.putExtra(AppContansts.INTENT_DATA, item);
        startActivity(intent);
    }

    @Override
    public void reviewsEdit(PlaceGoogleDTO.Result.Review review) {
        if (mUser == null) return;
        isAddNew = false;
        ratingBarSelectDialog.setRating(review.getRating());
        txt_comment_dialog.setText(review.text);
        dialogReview.show();
    }

    @Override
    public void reviewsRemove(PlaceGoogleDTO.Result.Review review) {
        if (mUser == null) return;
        mController.removeReviews(mUser, review);
    }

    public void removeReviewSuccess() {
        for (PlaceGoogleDTO.Result.Review item : arrReview) {
            if (item.email.equals(mUser.getEmail())) {
                arrReview.remove(item);
                break;
            }
        }
        refreshReview();
    }

    public void editReviewSuccess() {
        dialogReview.dismiss();
        for (int i = 0; i < arrReview.size(); i++) {
            PlaceGoogleDTO.Result.Review item = arrReview.get(i);
            if (item.email != null && !item.email.isEmpty()) {
                arrReview.remove(i);
                i--;
            }
        }
        adapterReviews.notifyDataSetChanged();
        mController.getLocalReviews(data.id);
    }

    public void getLocalReviewSuccess(ReviewDTO review) {
        reviewDTO = review;
        if (arrReview == null) {
            arrReview = new ArrayList<>();
            arrReview.addAll(Arrays.asList(reviewDTO.result));
        } else {
            arrReview.addAll(Arrays.asList(reviewDTO.result));
            refreshReview();
        }
    }

    public void getGooglePlaceDetailSuccess(PlaceGoogleDTO placeGoogleDTO) {
        dataGoogle = placeGoogleDTO;
        addControlsPlaceGoogle();
        initSliderImageGoogle();
        initReviews();
        initDialogReviews();
        updatePlace(dataGoogle.getRating());
    }

    public void getNearFoodPlaceSuccess(PlaceNearDTO placeNearDTO) {
        dataNearFood = placeNearDTO;
        if (dataNearFood.results != null && dataNearFood.results.length > 0)
            addControlsFood();
    }

    public void getNearHotelPlaceSuccess(PlaceNearDTO placeNearDTO) {
        dataNearHotel = placeNearDTO;
        if (dataNearHotel.results != null && dataNearHotel.results.length > 0)
            addControlsHotel();
    }

    public void addFavoriteSuccess() {
        likeButton.setEnabled(true);
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.main_content),
                getString(R.string.add_favorite),
                BaseTransientBottomBar.LENGTH_LONG);
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

    public void removeFavoriteSuccess() {
        likeButton.setEnabled(true);
        Snackbar.make(
                findViewById(R.id.main_content),
                getString(R.string.remove_favorite),
                BaseTransientBottomBar.LENGTH_LONG).show();
    }

    public void addReviewSuccess() {
        dialogReview.dismiss();
        mController.getLocalReviews(data.id);
    }

    public void getScheduleSuccess(List<AddScheduleDTO.Schedule> schedules) {
        Collections.sort(schedules);
        arrSchedule.clear();
        arrSchedule.addAll(schedules);
        adapterSchedule.notifyDataSetChanged();
        txtFromDate.setError(null);
        txtToDate.setError(null);
        txtNote.setText("");
        txtToDate.setText(simpleDateFormat.format(toDate.getTime()));
        txtFromDate.setText(simpleDateFormat.format(fromDate.getTime()));
        if (arrSchedule.size() == 0) {
            lvSchedule.setVisibility(View.GONE);
            txtSchedule.setVisibility(View.VISIBLE);
        } else {
            lvSchedule.setVisibility(View.VISIBLE);
            txtSchedule.setVisibility(View.GONE);
        }
        dialogSchedule.show();
    }

    public void addSchedulePlaceSuccess(final AddSchedulePlaceDTO addSchedulePlaceDTO) {
        dialogSchedule.dismiss();
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.main_content),
                "Thêm thành công!",
                BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setAction("View", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPlaceActivity.this, SchedulePlaceActivity.class);
                intent.putExtra(AppContansts.INTENT_DATA, addSchedulePlaceDTO.result.id_schedule);
                startActivity(intent);
            }
        });
        snackbar.show();
//        Utils.showSuccessToast(this, "Thêm thành công!");
    }

    public void addSchedulePlaceFailure() {
        Utils.showFaildToast(this, "Trùng thời gian với địa điểm khác");
    }

    private static class StaticRunnable implements Runnable {
        private WeakReference weakReference;

        public StaticRunnable(BaseActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            DetailPlaceActivity activity = (DetailPlaceActivity) weakReference.get();
            if (activity != null) {
                activity.currentPage = activity.viewpager.getCurrentItem() + 1;
                if (activity.currentPage == activity.numPage) {
                    activity.currentPage = 0;
                }
                activity.viewpager.setCurrentItem(activity.currentPage--, true);
            }
        }
    }
}
