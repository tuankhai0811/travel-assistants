package com.tuankhai.travelassistants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.library.stickyadapter.StickyListHeadersAdapter;
import com.tuankhai.travelassistants.webservice.DTO.AddScheduleDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tuank on 22/10/2017.
 */

public class ScheduleAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    Context mContext;
    ArrayList<AddScheduleDTO.Schedule> arrSchedule;

    int[] mSectionIndices;
    String[] mSectionHeaders;
    private LayoutInflater mInflater;

    public ScheduleAdapter(Context context, ArrayList<AddScheduleDTO.Schedule> list) {
        this.mContext = context;
        this.arrSchedule = list;
        mInflater = LayoutInflater.from(mContext);
        mSectionIndices = getSectionIndices();
        mSectionHeaders = getSectionHeaders();
    }

    public void notifyDataChange() {
        mSectionIndices = getSectionIndices();
        mSectionHeaders = getSectionHeaders();
        notifyDataSetChanged();
    }

    private String[] getSectionHeaders() {
        SimpleDateFormat format = new SimpleDateFormat("MM - yyyy");
        String[] dates = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            Date date = new Date(Long.valueOf(arrSchedule.get(mSectionIndices[i]).date_start));
            dates[i] = format.format(date);
        }
        return dates;
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();

        if (arrSchedule.size() == 0) {
            return new int[0];
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date dateStart = new Date(Long.valueOf(arrSchedule.get(0).date_start));
        Date dateEnd = new Date(Long.valueOf(arrSchedule.get(0).date_end));
        String lastFirstDate = format.format(dateStart) + " - " + format.format(dateEnd);
        sectionIndices.add(0);

        for (int i = 0; i < arrSchedule.size(); i++) {
            Date timeStart = new Date(Long.valueOf(arrSchedule.get(i).date_start));
            Date timeEnd = new Date(Long.valueOf(arrSchedule.get(i).date_end));
            String lastDate = format.format(timeStart) + " - " + format.format(timeEnd);
            if (!lastDate.equals(lastFirstDate)) {
                lastFirstDate = lastDate;
                sectionIndices.add(i);
            }
        }

        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header_sticky_adapter, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        SimpleDateFormat format = new SimpleDateFormat("MM - yyyy");
        Date dateStart = new Date(Long.valueOf(arrSchedule.get(position).date_start));
        Date dateEnd = new Date(Long.valueOf(arrSchedule.get(position).date_end));
        String start = format.format(dateStart);
        String end = format.format(dateEnd);
        if (start.equals(end)) {
            holder.text.setText(start);
        } else {
            holder.text.setText(start + "  đến  " + end);
        }
        return convertView;
    }

    class HeaderViewHolder {
        TextView text;
    }

    @Override
    public long getHeaderId(int position) {
        SimpleDateFormat format = new SimpleDateFormat("MMyyyy");
        Date dateStart = new Date(Long.valueOf(arrSchedule.get(position).date_start));
        Date dateEnd = new Date(Long.valueOf(arrSchedule.get(position).date_end));
        long start = Long.valueOf(format.format(dateStart));
        long end = Long.valueOf(format.format(dateEnd));
        return start * 100 - end;
    }

    @Override
    public int getCount() {
        return arrSchedule.size();
    }

    @Override
    public Object getItem(int i) {
        return arrSchedule.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_schedule_layout, viewGroup, false);
            holder.txtName = (TextView) view.findViewById(R.id.txtName);
            holder.txtDate = (TextView) view.findViewById(R.id.txtDate);
            holder.txtPlace = (TextView) view.findViewById(R.id.txtPlace);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AddScheduleDTO.Schedule item = arrSchedule.get(i);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dateStart = new Date(Long.valueOf(item.date_start));
        Date dateEnd = new Date(Long.valueOf(item.date_end));

        holder.txtName.setText(item.name);
        holder.txtDate.setText(item.length + " ngày (" + format.format(dateStart) + " - " + format.format(dateEnd) + ")");
        holder.txtPlace.setText(item.place + " địa điểm");

        return view;
    }

    class ViewHolder {
        TextView txtName, txtDate, txtPlace;
    }

    @Override
    public Object[] getSections() {
        return mSectionHeaders;
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }
}
