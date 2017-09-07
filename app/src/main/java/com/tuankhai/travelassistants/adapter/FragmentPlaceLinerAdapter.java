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

public class FragmentPlaceLinerAdapter extends RecyclerView.Adapter<FragmentPlaceLinerAdapter.ViewHolder> {

    Activity context;

    public FragmentPlaceLinerAdapter(Activity context) {
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = context.getLayoutInflater();
        return new ViewHolder(inflater.inflate(R.layout.item_fragment_place_liner, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        switch (position) {
            case 0:
                viewHolder.txtName.setText(context.getResources().getString(R.string.type_spring));
                viewHolder.imgView.setBackground(context.getResources().getDrawable(R.drawable.bg_spring));
                break;
            case 1:
                viewHolder.txtName.setText(context.getResources().getString(R.string.type_summer));
                viewHolder.imgView.setBackground(context.getResources().getDrawable(R.drawable.bg_summer));
                break;
            case 2:
                viewHolder.txtName.setText(context.getResources().getString(R.string.type_autumn));
                viewHolder.imgView.setBackground(context.getResources().getDrawable(R.drawable.bg_autumn));
                break;
            case 3:
                viewHolder.txtName.setText(context.getResources().getString(R.string.type_winter));
                viewHolder.imgView.setBackground(context.getResources().getDrawable(R.drawable.bg_winter));
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
            imgView = itemView.findViewById(R.id.img_item_fragment_place_liner);
            txtName = itemView.findViewById(R.id.txt_item_fragment_place_liner);
        }
    }
}
