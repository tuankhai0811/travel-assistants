package com.tuankhai.travelassistants.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.module.readmore.ReadMoreTextView;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.AddSchedulePlaceDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Khai on 30/10/2017.
 */

public class SchedulePlaceAdapter extends RecyclerView.Adapter<SchedulePlaceAdapter.ViewHolder> {

    Context mContext;
    OnItemSchedulePlaceActionListener mListener;
    ArrayList<AddSchedulePlaceDTO.SchedulePlace> arrPlace;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public SchedulePlaceAdapter(Context context, ArrayList<AddSchedulePlaceDTO.SchedulePlace> arrPlace, OnItemSchedulePlaceActionListener listener) {
        this.mContext = context;
        this.arrPlace = arrPlace;
        this.mListener = listener;
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
        viewHolder.txtTime.setText(item.length + " ngày từ " + sdf.format(item.getStart()));
        viewHolder.txtDescription.setText(item.description);
        if (item.description.trim().isEmpty()) {
            viewHolder.txtDescription.setVisibility(View.GONE);
        }
        String url = AppContansts.URL_IMAGE + item.id_place + AppContansts.IMAGE_RATIO_16_9;
        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imgPlace);
    }

    @Override
    public int getItemCount() {
        return arrPlace.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle, txtTime;
        ReadMoreTextView txtDescription;
        ImageView imgPlace;
        View menu;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtDescription = itemView.findViewById(R.id.txt_description);
            imgPlace = itemView.findViewById(R.id.img_place);
            menu = itemView.findViewById(R.id.btn_menu);
            menu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_menu: {
//                    Context wrapper = new ContextThemeWrapper(mContext, R.style.PopupMenu);
                    PopupMenu popup = new PopupMenu(mContext, view);
                    popup.getMenuInflater().inflate(R.menu.menu_reviews, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (mListener == null) return false;
                            switch (item.getItemId()) {
                                case R.id.menu_edit:
                                    mListener.scheduleEdit(arrPlace.get(getAdapterPosition()));
                                    break;
                                case R.id.menu_remove:
                                    mListener.scheduleRemove(arrPlace.get(getAdapterPosition()));
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
                break;
            }
        }
    }

    public interface OnItemSchedulePlaceActionListener {
        void scheduleEdit(AddSchedulePlaceDTO.SchedulePlace schedulePlace);

        void scheduleRemove(AddSchedulePlaceDTO.SchedulePlace schedulePlace);
    }
}
