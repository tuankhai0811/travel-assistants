package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tuankhai.travelassistants.R;

import java.util.ArrayList;

/**
 * Created by tuank on 06/09/2017.
 */

public class SliderImageAdapter extends PagerAdapter {
    private Activity context;
    private ArrayList<Bitmap> arrImage;

    public SliderImageAdapter(FragmentActivity activity, ArrayList<Bitmap> arrImage) {
        this.context = activity;
        this.arrImage = arrImage;
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
    public Object instantiateItem(ViewGroup view, int position) {
        if (arrImage.size() == 0) return null;
        View item = LayoutInflater.from(context).inflate(R.layout.item_slider_image, null);
        ImageView imageView = item.findViewById(R.id.img_item_slider_place);
        imageView.setImageBitmap(arrImage.get(position));
        view.addView(item, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return item;
    }
}