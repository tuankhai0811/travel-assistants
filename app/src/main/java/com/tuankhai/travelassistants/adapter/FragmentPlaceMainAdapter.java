package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuankhai.loopingviewpager.CircleIndicator;
import com.tuankhai.loopingviewpager.LoopViewPager;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.ListPlaceActivity;
import com.tuankhai.travelassistants.adapter.decoration.GridSpacingItemDecoration;
import com.tuankhai.travelassistants.model.AllSliderPlace;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;
import com.tuankhai.viewpagertransformers.ZoomOutTranformer;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Khai on 07/09/2017.
 */

public class FragmentPlaceMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ProvinceAdapter.LayoutProvinceItemListener {
    private final int VIEW_PROVINCE = 0;
    private final int VIEW_SLIDER = 1;
    private final int VIEW_GRID = 2;
    private final int VIEW_LINER = 3;

    Activity context;

    //Province
    List<ProvinceDTO.Province> arrProvinces;
    ProvinceAdapter adapterProvinces;

    //Slider
    SliderPlaceAdapter adapterSliderPlace;
    AllSliderPlace allSliderPlace;
    int currentPage;
    int numPage;
    Timer timer;
    TimerTask task;
    final long DELAY_MS = 5000;
    final long PERIOD_MS = 5000;

    //Grid
    FragmentPlaceGridAdapter gridAdapter;

    //Liner
    FragmentPlaceLinerAdapter linerAdapter;

    public FragmentPlaceMainAdapter(Activity context) {
        this.context = context;

        //Get data
        allSliderPlace = Utils.getSliderPlace(context);
        arrProvinces = Arrays.asList(Utils.getAllProvince(context).provinces);

        //Province
        adapterProvinces = new ProvinceAdapter(context, arrProvinces, this);

        //Slider
        adapterSliderPlace = new SliderPlaceAdapter(context, allSliderPlace);

        //Grid
        gridAdapter = new FragmentPlaceGridAdapter(context);

        //Liner
        linerAdapter = new FragmentPlaceLinerAdapter(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case VIEW_PROVINCE:
                return new FragmentPlaceProvinViewHolder(inflater.inflate(R.layout.fragment_place_province, viewGroup, false));
            case VIEW_SLIDER:
                return new FragmentPlaceSliderViewHolder(inflater.inflate(R.layout.fragment_place_slider, viewGroup, false));
            case VIEW_GRID:
                return new FragmentPlaceGridViewHolder(inflater.inflate(R.layout.fragment_place_grid, viewGroup, false));
            case VIEW_LINER:
                return new FragmentPlaceLinerViewHolder(inflater.inflate(R.layout.fragment_place_linear, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (getItemViewType(i)) {
            case VIEW_PROVINCE:
                progressTypeProvince((FragmentPlaceProvinViewHolder) viewHolder);
                break;
            case VIEW_SLIDER:
                progressTypeSlider((FragmentPlaceSliderViewHolder) viewHolder);
                break;
            case VIEW_GRID:
                progressTypeGrid((FragmentPlaceGridViewHolder) viewHolder);
                break;
            case VIEW_LINER:
                progressTypeLine((FragmentPlaceLinerViewHolder) viewHolder);
                break;
        }
    }

    private void progressTypeLine(FragmentPlaceLinerViewHolder viewHolder) {
        viewHolder.lvLiner.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewHolder.lvLiner.addItemDecoration(new GridSpacingItemDecoration(1, Utils.dpToPx(context, context.getResources().getInteger(R.integer.spacingItemDecoration)), false));
        viewHolder.lvLiner.setAdapter(linerAdapter);
    }

    private void progressTypeGrid(FragmentPlaceGridViewHolder viewHolder) {
        viewHolder.lvGrid.setLayoutManager(new GridLayoutManager(context, 2));
        viewHolder.lvGrid.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dpToPx(context, context.getResources().getInteger(R.integer.spacingItemDecoration)), false));
        viewHolder.lvGrid.setAdapter(gridAdapter);
    }

    private void progressTypeSlider(final FragmentPlaceSliderViewHolder viewHolder) {
        viewHolder.loopViewPager.setScrollDurationFactor(1500);
        viewHolder.loopViewPager.setAdapter(adapterSliderPlace);
        viewHolder.loopViewPager.setPageTransformer(true, new ZoomOutTranformer());
        viewHolder.circleIndicator.setViewPager(viewHolder.loopViewPager);
        currentPage = 0;
        numPage = allSliderPlace.listSliderPlace.length;
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                currentPage = viewHolder.loopViewPager.getCurrentItem() + 1;
                if (currentPage == numPage) {
                    currentPage = 0;
                }
                viewHolder.loopViewPager.setCurrentItem(currentPage--, true);
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
        viewHolder.loopViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    private void progressTypeProvince(FragmentPlaceProvinViewHolder viewHolder) {
        viewHolder.lvProvince.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewHolder.lvProvince.setAdapter(adapterProvinces);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_PROVINCE;
            case 1:
                return VIEW_SLIDER;
            case 2:
                return VIEW_GRID;
            case 3:
                return VIEW_LINER;
            default:
                return VIEW_PROVINCE;
        }
    }

    @Override
    public void onProvinceClick(View view, ProvinceDTO.Province item) {
        Intent intent = new Intent(context, ListPlaceActivity.class);
        intent.putExtra(AppContansts.INTENT_TYPE, AppContansts.INTENT_TYPE_PROVINCE);
        intent.putExtra(AppContansts.INTENT_DATA, item);
        context.startActivity(intent);
    }

    public class FragmentPlaceProvinViewHolder extends RecyclerView.ViewHolder {
        RecyclerView lvProvince;
        View progressBar;

        public FragmentPlaceProvinViewHolder(View itemView) {
            super(itemView);
            lvProvince = itemView.findViewById(R.id.lv_province);
            progressBar = itemView.findViewById(R.id.progressBar_loading_list_province);
        }
    }

    public class FragmentPlaceSliderViewHolder extends RecyclerView.ViewHolder {
        LoopViewPager loopViewPager;
        CircleIndicator circleIndicator;
        View progressBar;

        public FragmentPlaceSliderViewHolder(View itemView) {
            super(itemView);
            loopViewPager = itemView.findViewById(R.id.viewpagerPlace);
            circleIndicator = itemView.findViewById(R.id.indicatorPlace);
            progressBar = itemView.findViewById(R.id.progressBar_loading_slider_place);
        }
    }

    public class FragmentPlaceGridViewHolder extends RecyclerView.ViewHolder {

        RecyclerView lvGrid;

        public FragmentPlaceGridViewHolder(View itemView) {
            super(itemView);
            lvGrid = itemView.findViewById(R.id.lv_fragment_place_grid);
        }
    }

    public class FragmentPlaceLinerViewHolder extends RecyclerView.ViewHolder {

        RecyclerView lvLiner;

        public FragmentPlaceLinerViewHolder(View itemView) {
            super(itemView);
            lvLiner = itemView.findViewById(R.id.lv_fragment_place_liner);
        }
    }

}
