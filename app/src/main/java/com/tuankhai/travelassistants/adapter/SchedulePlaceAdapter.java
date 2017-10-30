package com.tuankhai.travelassistants.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
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
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule_place_layout, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {



    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtTime, txtDescription;
        ImageView imgPlace;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
