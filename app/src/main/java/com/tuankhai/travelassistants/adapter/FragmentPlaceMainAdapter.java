package com.tuankhai.travelassistants.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Khai on 07/09/2017.
 */

public class FragmentPlaceMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class FragmentPlaceProvinViewHolder extends RecyclerView.ViewHolder {
        public FragmentPlaceProvinViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FragmentPlaceSliderViewHolder extends RecyclerView.ViewHolder {
        public FragmentPlaceSliderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FragmentPlaceGridViewHolder extends RecyclerView.ViewHolder {
        public FragmentPlaceGridViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FragmentPlaceLinerViewHolder extends RecyclerView.ViewHolder {
        public FragmentPlaceLinerViewHolder(View itemView) {
            super(itemView);
        }
    }

}
