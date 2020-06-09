package com.example.ushopping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.ushopping.api.APIContext;
import com.example.ushopping.api.ProductsAPI;
import com.example.ushopping.data.IdData;
import com.example.ushopping.data.ProductData;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;

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

        ProductData product = getItem(position);
        UUID id = product.id;
        UUID listId = product.listId;
        String name = product.name;
        Integer count = product.count;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName= (TextView) convertView.findViewById(R.id.firstProductLine);
        TextView tvCount = (TextView) convertView.findViewById(R.id.secondProductLine);
        CheckBox check_box = convertView.findViewById(R.id.checkBox1);

        tvName.setText(name);
        tvCount.setText(count.toString());
        check_box.setChecked(product.isPurchased);

        check_box.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ProductData productData = new ProductData(listId, id, isChecked);
            ProductsAPI api = APIContext.createAPI(ProductsAPI.class);
            Call<ProductData> call = api.patch(listId, id, productData, APIContext.getSession(getContext()));
            APIContext.makeCall(parent.getRootView(), call, data -> {
                getItem(position).isPurchased = data.isPurchased;
            }).call();
        });

        //ViewAnimationFab.rotateFab(convertView, true);

        View listView = parent.findViewById(R.id.lv_addedproduct);

        Button menu_btn = convertView.findViewById(R.id.menu_btn);
        View finalConvertView = convertView;
        menu_btn.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Product options");
            Context context = view.getContext();

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            builder.setView(layout);

            builder.setPositiveButton("Change product parameters",(dialogInterface, i) -> {

                changeProduct(listView, product, finalConvertView, position);

            }).setNegativeButton("Delete product", (dialogInterface, i) -> {
                ProductsAPI api = APIContext.createAPI(ProductsAPI.class);
                Call<IdData> call = api.delete(listId, id, APIContext.getSession(context));

                APIContext.makeCall(listView, call, data -> {
                   remove(getItem(position));
                   notifyDataSetChanged();
                }).call();

            }).show();
        });

        return convertView;
    }

    void changeProduct(View listView, ProductData product, View convertView, int position) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
        Context context = listView.getContext();

        TextView tvName= (TextView) convertView.findViewById(com.example.ushopping.R.id.firstProductLine);
        TextView tvCount = (TextView) convertView.findViewById(R.id.secondProductLine);
        CheckBox check_box = convertView.findViewById(R.id.checkBox1);

        builder2.setTitle("Change parameters");

        EditText change_name = new EditText(context);
        EditText change_count = new EditText(context);
        LinearLayout layout1 = new LinearLayout(context);
        layout1.setOrientation(LinearLayout.VERTICAL);

        change_name.setInputType(InputType.TYPE_CLASS_TEXT);
        change_name.setText(tvName.getText());
        layout1.addView(change_name);

        change_count.setInputType(InputType.TYPE_CLASS_NUMBER);
        change_count.setText(tvCount.getText());
        layout1.addView(change_count);

        builder2.setPositiveButton("Change", (dialogInterface1, i1) -> {

            ProductData parameters = new ProductData(change_name.getText().toString(), Integer.parseInt(change_count.getText().toString()));
            ProductsAPI api = APIContext.createAPI(ProductsAPI.class);
            Call<ProductData> call = api.patch(product.listId, product.id,  parameters, APIContext.getSession(getContext()));

            APIContext.makeCall(listView, call, data -> {
                getItem(position).name = data.name;
                getItem(position).count = data.count;
                notifyDataSetChanged();
            }).call();
        }).setNegativeButton("Cancel", null);
        builder2.setView(layout1).show();
    }
}
