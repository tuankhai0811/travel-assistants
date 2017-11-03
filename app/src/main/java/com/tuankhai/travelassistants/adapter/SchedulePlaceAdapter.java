package com.tuankhai.travelassistants.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.AddSchedulePlaceDTO;

import java.util.ArrayList;

/**
 * Created by Khai on 30/10/2017.
 */

public class SchedulePlaceAdapter extends RecyclerView.Adapter<SchedulePlaceAdapter.ViewHolder> {

    Context mContext;
    ArrayList<AddSchedulePlaceDTO.SchedulePlace> arrPlace;

    public SchedulePlaceAdapter(Context context, ArrayList<AddSchedulePlaceDTO.SchedulePlace> arrPlace) {
        this.mContext = context;
        this.arrPlace = arrPlace;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_schedule_place_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AddSchedulePlaceDTO.SchedulePlace item = arrPlace.get(i);
        viewHolder.txtTitle.setText(item.name);
        viewHolder.txtTime.setText(item.date_start);
        viewHolder.txtDescription.setText(item.description);
        String url = AppContansts.URL_IMAGE + item.id_place + AppContansts.IMAGE_RATIO_16_9;
        Log.e("status", url);
        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imgPlace);
    }

    @Override
    public int getItemCount() {
        return arrPlace.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtTime, txtDescription;
        ImageView imgPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtDescription = itemView.findViewById(R.id.txt_description);
            imgPlace = itemView.findViewById(R.id.img_place);
        }
    }
}
