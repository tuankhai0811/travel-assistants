package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Khai on 01/09/2017.
 */

public class SliderPlaceAdapter extends PagerAdapter {
    private Activity context;
    private ArrayList<Bitmap> arrayList;

    public SliderPlaceAdapter(FragmentActivity context, ArrayList<Bitmap> arrImgSliderPlace) {
        this.context = context;
        this.arrayList = arrImgSliderPlace;
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
        if (arrayList.size() == 0) return null;
        ImageView imageView = new ImageView(view.getContext());
        imageView.setImageBitmap(arrayList.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return imageView;
    }

//    public void addItem() {
//        mSize++;
//        notifyDataSetChanged();
//    }
//
//    public void removeItem() {
//        mSize--;
//        mSize = mSize < 0 ? 0 : mSize;
//
//        notifyDataSetChanged();
//    }
}
