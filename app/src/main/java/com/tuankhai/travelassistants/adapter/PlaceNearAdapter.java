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
import com.tuankhai.ratingbar.MaterialRatingBar;
import com.tuankhai.ripple.MaterialRippleLayout;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.util.List;

/**
 * Created by Khai on 11/09/2017.
 */

public class PlaceNearAdapter extends RecyclerView.Adapter<PlaceNearAdapter.PlaceNearViewHolder> {
    Activity context;
    List<PlaceNearDTO.Result> arrPlace;

    LayoutListPlaceNearItemListener itemListener;

    public PlaceNearAdapter(Activity context, List<PlaceNearDTO.Result> arrPlace, LayoutListPlaceNearItemListener listener) {
        this.context = context;
        this.arrPlace = arrPlace;
        this.itemListener = listener;
    }

    @Override
    public PlaceNearViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new PlaceNearViewHolder(
                MaterialRippleLayout.on(inflater.inflate(R.layout.item_place_near, viewGroup, false))
                        .rippleOverlay(true)
                        .rippleAlpha(0.2f)
                        .rippleColor(R.integer.rippleColor)
                        .rippleHover(true)
                        .create()
        );
    }

    @Override
    public void onBindViewHolder(PlaceNearViewHolder placeViewHolder, int i) {
        PlaceNearDTO.Result item = arrPlace.get(i);
        placeViewHolder.txtName.setText(item.name);
        placeViewHolder.ratingBar.invalidate();
        placeViewHolder.ratingBar.setMax(5);
        placeViewHolder.ratingBar.setNumStars(5);
        placeViewHolder.ratingBar.setStepSize(0.1f);
        placeViewHolder.ratingBar.setRating(item.getRaring() + 0.1f);
        if (item.photos != null && item.photos.length > 0) {
            Glide.with(context)
                    .load(RequestService.getImage(item.photos[0].photo_reference))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(placeViewHolder.imageView);
        } else {
            placeViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_place_global_4_3));
        }
    }

    @Override
    public int getItemCount() {
        return arrPlace.size();
    }

    public class PlaceNearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        ImageView imageView;
        MaterialRatingBar ratingBar;

        public PlaceNearViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            imageView = itemView.findViewById(R.id.img_place);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            itemListener.onItemPlaceClick(view, arrPlace.get(getAdapterPosition()));
        }
    }

    public interface LayoutListPlaceNearItemListener {
        void onItemPlaceNearClick(View view, PlaceNearDTO.Result item);
    }
}