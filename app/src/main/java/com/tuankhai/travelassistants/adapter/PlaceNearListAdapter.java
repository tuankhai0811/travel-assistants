package com.tuankhai.travelassistants.adapter;

import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tuankhai.ratingbar.MaterialRatingBar;
import com.tuankhai.ripple.MaterialRippleLayout;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.ListPlaceNearActivity;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.main.RequestService;

import java.util.List;

/**
 * Created by Khai on 11/09/2017.
 */

public class PlaceNearListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    ListPlaceNearActivity context;
    List<PlaceNearDTO.Result> arrPlace;
    RecyclerView mRecyclerView;
    Location location;

    LayoutListPlaceNearItemListener itemListener;
    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setOnItemClickListener(LayoutListPlaceNearItemListener onItemClickListener) {
        this.itemListener = onItemClickListener;
    }

    public PlaceNearListAdapter(ListPlaceNearActivity context, RecyclerView recyclerView, List<PlaceNearDTO.Result> arrPlace) {
        this.context = context;
        this.arrPlace = arrPlace;
        this.mRecyclerView = recyclerView;
        this.location = context.location;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            final LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new PlaceNearViewHolder(
                    MaterialRippleLayout.on(inflater.inflate(R.layout.item_place_near_liner, viewGroup, false))
                            .rippleOverlay(true)
                            .rippleAlpha(0.2f)
                            .rippleColor(R.integer.rippleColor)
                            .rippleHover(true)
                            .create()
            );
//            View view = LayoutInflater.from(context).inflate(R.layout.item_place_near_liner, viewGroup, false);
//            return new PlaceNearViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_progressbar, viewGroup, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return arrPlace.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            PlaceNearViewHolder holder = (PlaceNearViewHolder) viewHolder;
//            Glide.clear(holder.imageView);
//            holder.setIsRecyclable(false);
            PlaceNearDTO.Result item = arrPlace.get(position);
            Location mLocation = new Location("Place");
            mLocation.setLongitude(Double.parseDouble(item.geometry.location.lng));
            mLocation.setLatitude(Double.parseDouble(item.geometry.location.lat));
            holder.txtName.setText(item.name);
            holder.ratingBar.invalidate();
            holder.ratingBar.setMax(5);
            holder.ratingBar.setNumStars(5);
            holder.ratingBar.setStepSize(0.1f);
            holder.ratingBar.setRating(item.getRaring() + 0.1f);
            double distance = ((double) Math.round(mLocation.distanceTo(location) / 100)) / 10;
            if (distance == 0) {
                holder.txtDistance.setText(mLocation.distanceTo(location) + " m");
            } else {
                holder.txtDistance.setText(distance + " km");
            }
            if (item.photos != null && item.photos.length > 0) {
                Glide.with(context)
                        .load(RequestService.getImage(item.photos[0].photo_reference))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_place_global_4_3));
            }

        } else {
            ((LoadingViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return arrPlace == null ? 0 : arrPlace.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
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

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}