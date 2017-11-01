package com.tuankhai.travelassistants.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;

import java.util.List;

/**
 * Created by Khai on 01/11/2017.
 */

public class ScheduleListAdapter extends ArrayAdapter<AddScheduleDTO.Schedule> {

    Context mContext;
    List<AddScheduleDTO.Schedule> arrSchedule;

    public ScheduleListAdapter(@NonNull Context context, int resource, @NonNull List<AddScheduleDTO.Schedule> arrSchedule) {
        super(context, resource, arrSchedule);
        this.mContext = context;
        this.arrSchedule = arrSchedule;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, false);
    }

    public View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent, boolean hasDivider) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_spinner_schedule, parent, false);
        TextView textView = row.findViewById(R.id.txt_name);
        textView.setText(arrSchedule.get(position).name);
        if (hasDivider) {
            row.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, true);
    }
}
