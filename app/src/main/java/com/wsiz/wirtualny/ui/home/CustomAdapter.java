package com.wsiz.wirtualny.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import com.wsiz.wirtualny.R;

public class CustomAdapter extends ArrayAdapter<String> {

    private ArrayList<String> dataSet;
    Context mContext;

    public CustomAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.list_layout, data);
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
            convertView = inflater.inflate(R.layout.list_layout, parent, false);
            viewHolder.tiltedOnList = (TextView) convertView.findViewById(R.id.tiltedOnList);
            viewHolder.dataOnList = (TextView) convertView.findViewById(R.id.dataOnList);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }



        try{
            String[] split = dataModel.split("\n");
            viewHolder.tiltedOnList.setText(split[0]);
            viewHolder.dataOnList.setText(split[1]);
        }catch (ArrayIndexOutOfBoundsException e){
            e.fillInStackTrace();
        }

        return convertView;
    }



}