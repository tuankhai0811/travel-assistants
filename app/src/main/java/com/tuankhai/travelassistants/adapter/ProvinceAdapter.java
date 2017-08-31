package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.webservice.DTO.Province;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Khai on 31/08/2017.
 */

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {

    private Activity context;
    private ArrayList<Province> lists;
    private ArrayList<Integer> backgrounds;

    public ProvinceAdapter(Activity context, ArrayList<Province> lists) {
        this.context = context;
        this.lists = lists;
        backgrounds = new ArrayList<>();
        backgrounds.add(R.drawable.bg_random_1);
        backgrounds.add(R.drawable.bg_random_2);
        backgrounds.add(R.drawable.bg_random_3);
        backgrounds.add(R.drawable.bg_random_4);
        backgrounds.add(R.drawable.bg_random_5);
        backgrounds.add(R.drawable.bg_random_6);
        backgrounds.add(R.drawable.bg_random_7);
        backgrounds.add(R.drawable.bg_random_8);
        backgrounds.add(R.drawable.bg_random_9);
        backgrounds.add(R.drawable.bg_random_10);
    }

    @Override
    public ProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProvinceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province, null));
    }

    @Override
    public void onBindViewHolder(ProvinceViewHolder holder, int position) {
        Province item = lists.get(position);
        holder.txtName.setText(item.name);
        holder.txtIcon.setText(item.name.charAt(0));
        holder.txtIcon.setBackground(context.getResources().getDrawable(new Random().nextInt(9)));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ProvinceViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView txtIcon, txtName;

        public ProvinceViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_item_province);
            txtIcon = itemView.findViewById(R.id.txt_item_icon_province);
            txtName = itemView.findViewById(R.id.txt_item_name_province);
        }
    }
}
