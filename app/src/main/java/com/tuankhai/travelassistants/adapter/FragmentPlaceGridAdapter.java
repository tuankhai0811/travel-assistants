package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;

/**
 * Created by Khai on 07/09/2017.
 */

public class FragmentPlaceGridAdapter extends RecyclerView.Adapter<FragmentPlaceGridAdapter.ViewHolder> {

    Activity context;

    public FragmentPlaceGridAdapter(Activity context) {
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = context.getLayoutInflater();
        return new ViewHolder(inflater.inflate(R.layout.item_fragment_place_grid, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        switch (position) {
            case 0:
                viewHolder.txtName.setText(context.getResources().getString(R.string.type_sea));
                viewHolder.imgView.setBackground(context.getResources().getDrawable(R.drawable.bg_sea_travel));
                break;
            case 1:
                viewHolder.txtName.setText(context.getResources().getString(R.string.type_attractions));
                viewHolder.imgView.setBackground(context.getResources().getDrawable(R.drawable.bg_attractions_travel));
                break;
            case 2:
                viewHolder.txtName.setText(context.getResources().getString(R.string.type_cultural));
                viewHolder.imgView.setBackground(context.getResources().getDrawable(R.drawable.bg_cultural_travel));
                break;
            case 3:
                viewHolder.txtName.setText(context.getResources().getString(R.string.type_entertaiment));
                viewHolder.imgView.setBackground(context.getResources().getDrawable(R.drawable.bg_entertainment_travel));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;
        TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.img_item_fragment_place_grid);
            txtName = itemView.findViewById(R.id.txt_item_fragment_place_grid);
        }
    }
}
