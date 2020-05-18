package com.example.ushopping;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ushopping.data.ProductData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ProductListActivity extends AppCompatActivity {

    ListView productsListView;
    com.example.ushopping.ProductListAdapter productListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        UUID id = UUID.fromString(extras.getString("list_id"));
        String title = extras.getString("list_title");
        Date created_at = new Date(extras.getLong("list_date"));
        getSupportActionBar().setTitle(title);



        productsListView = findViewById(R.id.lv_addedproduct);
        FloatingActionButton add_product = findViewById(R.id.btn_addproduct);
        EditText input_product = new EditText(this);
        EditText input_quantity = new EditText(this);


        ImageButton btn_menu = findViewById(R.id.btn_menu);
        productListAdapter = new com.example.ushopping.ProductListAdapter(this, R.layout.adapter_product, new ArrayList<ProductData>());
        productsListView.setAdapter(productListAdapter);
        productListAdapter.add(new ProductData("abc",1));
        productListAdapter.add(new ProductData("def",2));
        productListAdapter.add(new ProductData("ghj",3));
        // TODO: load products

        add_product.setOnClickListener(view ->  {
            productDialogBuilder(view).show();
        });
    }

    AlertDialog.Builder productDialogBuilder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new item");


        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText name = new EditText(this);
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setHint("Name");
        layout.addView(name);

        EditText count = new EditText(this);
        count.setInputType(InputType.TYPE_CLASS_NUMBER);
        count.setHint("Count");
        layout.addView(count);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            // TODO: add product to list
            productListAdapter.add(new ProductData(name.getText().toString(), Integer.parseInt(count.getText().toString())));

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        return builder;
    }

    AlertDialog.Builder menuButtonBuilder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product options");
        Context context = view.getContext();

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        builder
                .setPositiveButton("Change product name",(dialogInterface, i) -> {


                })
                .setNeutralButton("Change product quantity",(dialogInterface, i) -> {


                })
                .setNegativeButton("Delete product",(dialogInterface, i) -> {
                    

                })
                .show();
        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) finish();

        if (id == R.id.action_list_rename) {
            // TODO: rename list
            return true;
        }

        if (id == R.id.action_list_delete) {
            // TODO: remove list
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
