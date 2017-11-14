package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.library.ratingbar.MaterialRatingBar;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;

import java.util.ArrayList;

/**
 * Created by Khai on 04/10/2017.
 */

public class PlaceQueryAdapter extends RecyclerView.Adapter<PlaceQueryAdapter.PlaceViewHolder> {
    private Activity context;
    private ArrayList<PlaceDTO.Place> arrPlace;

    private LayoutListPlaceQueryItemListener itemListener;

    public PlaceQueryAdapter(Activity context, ArrayList<PlaceDTO.Place> arrPlace, LayoutListPlaceQueryItemListener listener) {
        this.context = context;
        this.arrPlace = arrPlace;
        this.itemListener = listener;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new PlaceViewHolder(inflater.inflate(R.layout.item_query_place_type, null));
//        return new PlaceViewHolder(
//                MaterialRippleLayout.on(inflater.inflate(R.layout.item_query_place_type, viewGroup, false))
//                        .rippleOverlay(true)
//                        .rippleAlpha(0.2f)
//                        .rippleColor(R.integer.rippleColor)
//                        .rippleHover(true)
//                        .create()
//        );
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder placeViewHolder, int i) {
        PlaceDTO.Place item = arrPlace.get(i);
        String url = AppContansts.URL_IMAGE + item.id + AppContansts.IMAGE_THUMBNAIL;
        placeViewHolder.txtName.setText(item.long_name);
        placeViewHolder.txtProvince.setText(item.province_name);
        placeViewHolder.ratingBar.setRating(item.getRating());
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.bg_place_global_16_10)
                .into(placeViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrPlace.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName, txtProvince;
        ImageView imageView;
        MaterialRatingBar ratingBar;
        View layout;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name_place);
            txtProvince = itemView.findViewById(R.id.txt_name_province);
            imageView = itemView.findViewById(R.id.img_place);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            layout = itemView.findViewById(R.id.layout_item_place_result);
            ratingBar.invalidate();
            ratingBar.setMax(5);
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(0.1f);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemListener == null) return;
            itemListener.onItemPlaceClick(view, arrPlace.get(getAdapterPosition()));
        }
    }

    public interface LayoutListPlaceQueryItemListener {
        void onItemPlaceClick(View view, PlaceDTO.Place item);
    }
}
