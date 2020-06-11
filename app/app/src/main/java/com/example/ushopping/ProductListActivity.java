package com.example.ushopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ushopping.api.APIContext;
import com.example.ushopping.api.ProductListsAPI;
import com.example.ushopping.api.ProductsAPI;
import com.example.ushopping.data.IdData;
import com.example.ushopping.data.ProductData;
import com.example.ushopping.data.ProductListData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;

public class ProductListActivity extends AppCompatActivity {

    ListView productsListView;
    ProductListAdapter productListAdapter;
    UUID listId;
    String title;
    Date created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Bundle extras = getIntent().getExtras();
        listId = UUID.fromString(extras.getString("list_id"));
        title = extras.getString("list_title");
        created_at = new Date(extras.getLong("list_date"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        productsListView = findViewById(R.id.lv_addedproduct);
        FloatingActionButton add_product = findViewById(R.id.btn_addproduct);
        EditText input_product = new EditText(this);
        EditText input_quantity = new EditText(this);

        productListAdapter = new ProductListAdapter(this, R.layout.adapter_product, new ArrayList<ProductData>());
        productsListView.setAdapter(productListAdapter);

        add_product.setOnClickListener(view -> {
            productDialogBuilder(view).show();
        });

        setSpinner(true);
        loadProducts();
    }

    void setSpinner(boolean active) {
        ProgressBar waiting = findViewById(R.id.waiting);
        FloatingActionButton btn_addList = findViewById(R.id.btn_addproduct);

        if (active) {
            waiting.setVisibility(View.VISIBLE);
            btn_addList.hide();
            productListAdapter.clear();
        } else {
            waiting.setVisibility(View.GONE);
            btn_addList.show();
        }
    }

    public void loadProducts(){
        View view = findViewById(R.id.lv_addedproduct);
        ProductsAPI productList = APIContext.createAPI(ProductsAPI.class);
        Call<List<ProductData>> listCall = productList.get(listId, APIContext.getSession(this));

        APIContext.makeCall(view, listCall, data -> {
            productListAdapter.clear();
            productListAdapter.addAll(data);
            setSpinner(false);
        }).call();

    }

    public AlertDialog.Builder productDialogBuilder(View view) {
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
            ProductData productData = new ProductData(name.getText().toString(), Integer.parseInt(count.getText().toString()));
            ProductsAPI api = APIContext.createAPI(ProductsAPI.class);
            Call<ProductData> call = api.post(listId, productData, APIContext.getSession(this));

            APIContext.makeCall(view, call, data -> {
                productListAdapter.add(data);
            }).call();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            finish();
        });

        return builder;
    }

    public AlertDialog.Builder rename_builder(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change list title");

        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText rename = new EditText(this);
        rename.setInputType(InputType.TYPE_CLASS_TEXT);
        rename.setHint("Enter new title");
        rename.setText(title);
        layout.addView(rename);

        builder.setView(layout);

        builder.setPositiveButton("Change", (dialogInterface, i) -> {
            ProductListData productListData = new ProductListData(listId, rename.getText().toString(), created_at);
            ProductListsAPI api = APIContext.createAPI(ProductListsAPI.class);
            Call<ProductListData> call = api.patch(listId, productListData, APIContext.getSession(this));

            APIContext.makeCall(view, call, data -> {
                getSupportActionBar().setTitle(data.title);
            }).call();

        }).setNegativeButton("Cancel", null);

        return builder;
    }

    public AlertDialog.Builder delete_builder(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete?");

        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            ProductListsAPI api = APIContext.createAPI(ProductListsAPI.class);
            Call<IdData> call = api.delete(listId, APIContext.getSession(this));

            APIContext.makeCall(view, call, data -> {
                finish();
            }).call();
        }).setNegativeButton("Cancel", null);
        return builder;
    }

    public void list_members() {
        Intent intent = new Intent(this, ListMembersActivity.class);
        intent.putExtra("list_id", listId.toString());
        intent.putExtra("list_title", title);
        startActivity(intent);
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

        if (id == R.id.action_list_refresh) {
            setSpinner(true);
            loadProducts();
            return true;
        }

        if (id == R.id.action_list_rename) {
            rename_builder(productsListView).show();
            return true;
        }

        if (id == R.id.action_list_share) {
            list_members();
            return  true;
        }

        if (id == R.id.action_list_delete) {
            delete_builder(productsListView).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
