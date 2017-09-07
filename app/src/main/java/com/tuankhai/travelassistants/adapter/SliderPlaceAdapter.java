package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.DetailPlaceActivity;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.SliderPlaceDTO;

import java.util.ArrayList;

/**
 * Created by Khai on 01/09/2017.
 */

public class SliderPlaceAdapter extends PagerAdapter {
    private Activity context;
    private ArrayList<Bitmap> arrImage;
    private SliderPlaceDTO data;


    public SliderPlaceAdapter(FragmentActivity activity, SliderPlaceDTO data) {
        this.context = activity;
        this.data = data;
        arrImage = new ArrayList<>();
        for (int i = 0; i < data.places.length; i++) {
            arrImage.add(Utils.decodeToBase64(data.places[i].codeImage));
        }
    }

    @Override
    public int getCount() {
        return arrImage.size();
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
        if (arrImage.size() == 0) return null;
        final View item = LayoutInflater.from(context).inflate(R.layout.item_slider_place, null);
        ImageView imageView = item.findViewById(R.id.img_item_slider_place);
        TextView textView = item.findViewById(R.id.txt_item_name_slider_place);
        textView.setText(data.places[position].long_name);
        imageView.setImageBitmap(arrImage.get(position));
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailPlaceActivity.class);
                Gson gson = new Gson();
                String string = gson.toJson(data.places[position]);
                intent.putExtra(AppContansts.INTENT_DATA, gson.fromJson(string, PlaceDTO.Place.class));
                context.startActivity(intent);
            }
        });
        view.addView(item, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return item;
    }

}
