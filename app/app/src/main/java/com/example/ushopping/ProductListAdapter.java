package com.example.ushopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ushopping.R;
import com.example.ushopping.api.APIContext;
import com.example.ushopping.data.ProductData;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ProductListAdapter extends ArrayAdapter<ProductData> {

    private Context mContext;
    int mResource;

    public ProductListAdapter(Context context, int resource, ArrayList<ProductData> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).name;
        Integer count = getItem(position).count;

        // TODO: add UUID

        //UUID id = getItem(position).getId();

        ProductData list = new ProductData(name, count);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName= (TextView) convertView.findViewById(com.example.ushopping.R.id.firstProductLine);
        TextView tvCount = (TextView) convertView.findViewById(R.id.secondProductLine);

        tvName.setText(name);
        tvCount.setText(count.toString());

        return convertView;
    }
}
