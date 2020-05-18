package com.example.ushopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ushopping.api.APIContext;
import com.example.ushopping.data.ProductListData;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ListAdapter extends ArrayAdapter<ProductListData> {

    private Context mContext;
    int mResource;

    public ListAdapter(Context context, int resource, ArrayList<ProductListData> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String title = getItem(position).title;
        Date date = getItem(position).createdAt;
        UUID id = getItem(position).id;

        ProductListData list = new ProductListData(id, title, date);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvtitle = (TextView) convertView.findViewById(com.example.ushopping.R.id.firstLine);
        TextView tvdate = (TextView) convertView.findViewById(R.id.secondLine);

        tvtitle.setText(title);
        tvdate.setText(APIContext.formatDate(date));

        return convertView;
    }
}
