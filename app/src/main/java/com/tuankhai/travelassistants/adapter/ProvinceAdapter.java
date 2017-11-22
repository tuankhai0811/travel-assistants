package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;

import java.util.ArrayList;

/**
 * Created by Khai on 31/08/2017.
 */

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHolder> {

    private Activity context;
    private ArrayList<ProvinceDTO.Province> lists;
    private ArrayList<Integer> backgrounds;

    LayoutProvinceItemListener itemListener;

    public ProvinceAdapter(Activity context, ArrayList<ProvinceDTO.Province> lists, LayoutProvinceItemListener listener) {
        this.context = context;
        this.lists = lists;
        this.itemListener = listener;
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
        backgrounds.add(R.drawable.bg_random_11);
        backgrounds.add(R.drawable.bg_random_12);
        backgrounds.add(R.drawable.bg_random_13);
        backgrounds.add(R.drawable.bg_random_14);
        backgrounds.add(R.drawable.bg_random_15);
        backgrounds.add(R.drawable.bg_random_16);
        backgrounds.add(R.drawable.bg_random_17);
        backgrounds.add(R.drawable.bg_random_18);
        backgrounds.add(R.drawable.bg_random_19);
        backgrounds.add(R.drawable.bg_random_20);
    }

    @Override
    public ProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ProvinceViewHolder(inflater.inflate(R.layout.item_province, null));
//        return new ProvinceViewHolder(
//                MaterialRippleLayout.on(inflater.inflate(R.layout.item_province, parent, false))
//                        .rippleOverlay(true)
//                        .rippleAlpha(0.05f)
//                        .rippleColor(R.integer.rippleColor)
//                        .rippleHover(true)
//                        .create()
//        );
    }

    @Override
    public void onBindViewHolder(ProvinceViewHolder holder, int position) {
        final ProvinceDTO.Province item = lists.get(position);
        holder.txtName.setText(item.name);
        holder.txtIcon.setText(item.name.toString().substring(0, 1));
        holder.txtIcon.setBackgroundResource(backgrounds.get(item.color));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ProvinceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layout;
        TextView txtIcon, txtName;

        public ProvinceViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_item_province);
            txtIcon = itemView.findViewById(R.id.txt_item_icon_province);
            txtName = itemView.findViewById(R.id.txt_item_name_province);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemListener.onProvinceClick(txtName, lists.get(getAdapterPosition()));
        }
    }

    public interface LayoutProvinceItemListener {
        void onProvinceClick(View view, ProvinceDTO.Province item);
    }
}
