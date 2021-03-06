package com.wsiz.wirtualny.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.wsiz.wirtualny.R;

public class CustomAdapter extends ArrayAdapter<String> {

    private ArrayList<String> dataSet;
    Context mContext;

    public CustomAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.list_aktualnosci, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    private class ViewHolder {
        TextView tiltedOnList;
        TextView dataOnList;
        TextView standardOnList;
        ImageView imageonList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_aktualnosci, parent, false);
            viewHolder.tiltedOnList = (TextView) convertView.findViewById(R.id.tiltedOnList);
            viewHolder.dataOnList = (TextView) convertView.findViewById(R.id.dataOnList);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }



        try{
            String[] split = dataModel.split("~~");
            viewHolder.tiltedOnList.setText(split[0]);
            Date date;
            date = new Date();
            date.setTime(Long.valueOf(split[1]));
            SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
            String date1 = format1.format(date);
            viewHolder.dataOnList.setText(date1);
        }catch (ArrayIndexOutOfBoundsException e){
            e.fillInStackTrace();
        }

        return convertView;
    }



}