package com.tuankhai.travelassistants.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.tuankhai.travelassistants.utils.MyCache;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDayDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.util.List;
import java.util.WeakHashMap;

import static com.tuankhai.travelassistants.utils.MyCache.bg_place_global_4_3;

/**
 * Created by Khai on 16/11/2017.
 */

public class PlaceScheduleDayAdapter extends RecyclerView.Adapter<PlaceScheduleDayAdapter.PlaceNearViewHolder> {
    private Context context;
    private List<AddScheduleDayDTO.ScheduleDay> arrPlace;
    private WeakHashMap<String, PlaceGoogleDTO> arrData;

    private PlaceScheduleDayAdapter.LayoutListPlaceNearItemListener itemListener;

    public PlaceScheduleDayAdapter(
            Context context,
            List<AddScheduleDayDTO.ScheduleDay> arrPlace,
            PlaceScheduleDayAdapter.LayoutListPlaceNearItemListener listener) {
        this.context = context.getApplicationContext();
        this.arrPlace = arrPlace;
        this.itemListener = listener;
        this.arrData = new WeakHashMap<>();
    }

    @Override
    public PlaceScheduleDayAdapter.PlaceNearViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new PlaceScheduleDayAdapter.PlaceNearViewHolder(inflater.inflate(R.layout.item_place_schedule_day_liner, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final PlaceScheduleDayAdapter.PlaceNearViewHolder placeViewHolder, final int i) {
        String id = arrPlace.get(i).place_id;
        PlaceGoogleDTO item = arrData.get(id);
        if (item == null){
            new RequestService().getPlace(
                    arrPlace.get(i).place_id,
                    new MyCallback() {
                        @Override
                        public void onSuccess(Object response) {
                            super.onSuccess(response);
                            PlaceGoogleDTO detail = (PlaceGoogleDTO) response;
                            arrData.put(arrPlace.get(i).place_id, detail);
                            binding(placeViewHolder, detail.result);
                        }
                    });
        } else {
            binding(placeViewHolder, item.result);
        }
    }

    private void binding(PlaceScheduleDayAdapter.PlaceNearViewHolder holder, PlaceGoogleDTO.Result item) {
        holder.txtName.setText(item.name);
        holder.ratingBar.invalidate();
        holder.ratingBar.setMax(5);
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setStepSize(0.1f);
        holder.ratingBar.setRating(item.getRating() + 0.1f);
//        if (item.distance == 0) {
//            Location mLocation = new Location("Place");
//            mLocation.setLongitude(Double.parseDouble(item.geometry.location.lng));
//            mLocation.setLatitude(Double.parseDouble(item.geometry.location.lat));
//            arrPlace.get(position).distance = mLocation.distanceTo(location);
//            double distance = ((double) Math.round(mLocation.distanceTo(location) / 100)) / 10;
//            if (distance == 0) {
//                holder.txtDistance.setText(Math.round(mLocation.distanceTo(location)) + " m");
//            } else {
//                holder.txtDistance.setText(distance + " km");
//            }
//        } else {
//            double distance = ((double) Math.round(item.distance / 100)) / 10;
//            if (distance == 0) {
//                holder.txtDistance.setText(Math.round(item.distance) + " m");
//            } else {
//                holder.txtDistance.setText(distance + " km");
//            }
//        }
        if (item.photos != null && item.photos.length > 0) {
            Glide.with(context)
                    .load(RequestService.getImageAdapter(item.photos[0].photo_reference))
                    .override(context.getResources().getInteger(R.integer.width_16_9),
                            context.getResources().getInteger(R.integer.height_16_9))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        } else {
            if (MyCache.getInstance().getBitmapFromMemCache(bg_place_global_4_3) == null) {
                Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_place_global_4_3);
                MyCache.getInstance().addBitmapToMemoryCache(bg_place_global_4_3, image);
                holder.imageView.setImageBitmap(image);
            } else {
                holder.imageView.setImageBitmap(MyCache.getInstance().getBitmapFromMemCache(bg_place_global_4_3));
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrPlace.size();
    }

    public class PlaceNearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName, txtDistance;
        ImageView imageView;
        MaterialRatingBar ratingBar;

        public PlaceNearViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDistance = itemView.findViewById(R.id.txt_distance);
            imageView = itemView.findViewById(R.id.img_place);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            itemView.findViewById(R.id.layout).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                itemListener.onItemPlaceNearClick(view, arrPlace.get(getAdapterPosition()));
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    public interface LayoutListPlaceNearItemListener {
        void onItemPlaceNearClick(View view, AddScheduleDayDTO.ScheduleDay item);
    }
}