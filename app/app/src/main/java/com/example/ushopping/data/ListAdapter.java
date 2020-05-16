package com.example.ushopping.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ushopping.R;


import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ListData> {

    private Context mContext;
    int mResource;

    public ListAdapter(Context context, int resource, ArrayList<ListData> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String title = getItem(position).getTitle();
        String date = getItem(position).getDate();
        int id = getItem(position).getId();

        ListData list = new ListData(title, date, id);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvtitle = (TextView) convertView.findViewById(com.example.ushopping.R.id.firstLine);
        TextView tvdate = (TextView) convertView.findViewById(R.id.secondLine);

        tvtitle.setText(title);
        tvdate.setText(date);

        return convertView;
    }
}
