package com.wsiz.wirtualny.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import com.wsiz.wirtualny.R;

public class CustomAdapter2 extends ArrayAdapter<String> {

    private ArrayList<String> dataSet;
    Context mContext;

    public CustomAdapter2(ArrayList<String> data, Context context) {
        super(context, R.layout.list_oceny, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    private class ViewHolder {
        TextView przedmiot;
        TextView t1;
        TextView t2;
        TextView t3;
        TextView aktywnosc;

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
            convertView = inflater.inflate(R.layout.list_oceny, parent, false);

            viewHolder.przedmiot = (TextView) convertView.findViewById(R.id.tf_przedmiot);
            viewHolder.t1 = (TextView) convertView.findViewById(R.id.tf_t1);
            viewHolder.t2 = (TextView) convertView.findViewById(R.id.tf_t2);
            viewHolder.t3 = (TextView) convertView.findViewById(R.id.tf_t3);
            viewHolder.aktywnosc = (TextView) convertView.findViewById(R.id.tf_aktywnosc);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }



        try{
            String[] split = dataModel.split("~~");
            viewHolder.przedmiot.setText(split[0]);
            if(!split[1].contains("0.0")){
                viewHolder.t1.setText(split[1]);
            }else{
                viewHolder.t1.setText("");
            }
            if(!split[2].contains("0.0")){
                viewHolder.t2.setText(split[2]);
            }else{
                viewHolder.t2.setText("");
            }
            if(!split[3].contains("0.0")){
                viewHolder.t3.setText(split[3]);
            }else{
                viewHolder.t3.setText("");
            }
            if(!split[4].contains("0.0")){
                viewHolder.aktywnosc.setText(split[4]);
            }else{
                viewHolder.aktywnosc.setText("");
            }





        }catch (ArrayIndexOutOfBoundsException e){
            e.fillInStackTrace();
        }

        return convertView;
    }



}