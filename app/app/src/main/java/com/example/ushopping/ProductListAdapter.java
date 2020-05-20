package com.example.ushopping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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

    @SuppressLint("ShowToast")
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).name;
        Integer count = getItem(position).count;

        // TODO: add UUID

        UUID id = getItem(position).Id;

        ProductData list = new ProductData(name, count);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName= (TextView) convertView.findViewById(com.example.ushopping.R.id.firstProductLine);
        TextView tvCount = (TextView) convertView.findViewById(R.id.secondProductLine);

        Button menu_btn = convertView.findViewById(R.id.menu_btn);
        menu_btn.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Product options");
            Context context = view.getContext();

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            builder.setView(layout);

            builder
                    .setPositiveButton("Change product name",(dialogInterface, i) -> {

                    })
                    .setNeutralButton("Change product quantity",(dialogInterface, i) -> {
                        //finish();

                    })
                    .setNegativeButton("Delete product",(dialogInterface, i) -> {
                        //finish();

                    })

                    .show();


        });

        tvName.setText(name);
        tvCount.setText(count.toString());

        return convertView;
    }
}
