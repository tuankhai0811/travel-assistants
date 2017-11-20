package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.DetailPlaceActivity;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;

/**
 * Created by Khai on 01/09/2017.
 */

public class SliderPlaceAdapter extends PagerAdapter {
    private Activity context;
    private PlaceDTO data;

    public SliderPlaceAdapter(FragmentActivity activity, PlaceDTO data) {
        this.context = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.places.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        if (getCount() == 0) return null;
        final View item = LayoutInflater.from(context).inflate(R.layout.item_slider_place, null);
        ImageView imageView = item.findViewById(R.id.img_item_slider_place);
        TextView textView = item.findViewById(R.id.txt_item_name_slider_place);
        textView.setText(data.places[position].long_name);
        String address = AppContansts.URL_IMAGE + data.places[position].id + AppContansts.IMAGE_RATIO_4_3;
        Glide.with(context)
                .load(Uri.parse(address))
                .override(context.getResources().getInteger(R.integer.width_4_3),
                        context.getResources().getInteger(R.integer.height_4_3))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailPlaceActivity.class);
                intent.putExtra(AppContansts.INTENT_DATA, data.places[position]);
                context.startActivity(intent);
            }
        });
        view.addView(item, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return item;
    }

}
