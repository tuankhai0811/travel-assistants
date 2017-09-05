package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuankhai.ripple.MaterialRippleLayout;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;

import java.util.ArrayList;

/**
 * Created by Khai on 05/09/2017.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    Activity context;
    ArrayList<PlaceDTO.Place> arrPlace;

    private static int type;

    public PlaceAdapter(Activity context, ArrayList<PlaceDTO.Place> arrPlace) {
        this.context = context;
        this.arrPlace = arrPlace;
        type = R.layout.item_place_line;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new PlaceViewHolder(
                MaterialRippleLayout.on(inflater.inflate(type, viewGroup, false))
                        .rippleOverlay(true)
                        .rippleAlpha(0.2f)
                        .rippleColor(R.integer.rippleColor)
                        .rippleHover(true)
                        .create()
        );
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder placeViewHolder, int i) {
        PlaceDTO.Place item = arrPlace.get(i);
        String url = AppContansts.URL_IMAGE + item.id + AppContansts.IMAGE_RATIO_16_9;
        placeViewHolder.txtName.setText(item.long_name);
        Glide.with(context).load(url).into(placeViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrPlace.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView txtName;
        ImageView imageView;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_item_place);
            txtName = itemView.findViewById(R.id.txt_item_name_place);
            imageView = itemView.findViewById(R.id.img_item_place);
        }
    }
}
