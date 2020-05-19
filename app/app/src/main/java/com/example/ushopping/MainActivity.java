package com.example.ushopping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.ushopping.api.APICall;
import com.example.ushopping.api.APIContext;
import com.example.ushopping.api.ProductListsAPI;
import com.example.ushopping.data.ErrorData;
import com.example.ushopping.data.ProductListData;
import com.example.ushopping.data.TitleData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = findViewById(R.id.listView);
        FloatingActionButton btn_addList = findViewById(R.id.btn_addList);

        listAdapter = new ListAdapter(this, R.layout.adapter_main_list, new ArrayList<ProductListData>());
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ProductListData list = listAdapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
            intent.putExtra("list_id", list.id.toString());
            intent.putExtra("list_title", list.title);
            intent.putExtra("list_date", list.createdAt.getTime());
            startActivity(intent);
        });

        btn_addList.setOnClickListener(view -> {
            productDialogBuilder(view).show();
        });

        setSpinner(true);
        APIContext.updateSession(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (APIContext.isLogin(this)) refreshList();
    }

    AlertDialog.Builder productDialogBuilder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        builder.setTitle("Add new list");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = input.getText().toString();
            TitleData lsd = new TitleData(title);

            ProductListsAPI productList = APIContext.createAPI(ProductListsAPI.class);
            Call<ProductListData> listCall = productList.post(lsd, APIContext.getSession(this));

            APIContext.makeCall(view, listCall, data -> {
                Snackbar.make(view, "Created list: " + data.title, Snackbar.LENGTH_LONG).show();

                ProductListData create = new ProductListData(data.id, data.title, data.createdAt);
                listAdapter.insert(create, 0);

            }).call();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        return builder;
    }

    void setSpinner(boolean active) {
        ProgressBar waiting = findViewById(R.id.waiting);
        FloatingActionButton btn_addList = findViewById(R.id.btn_addList);

        if (active) {
            waiting.setVisibility(View.VISIBLE);
            btn_addList.hide();
            listAdapter.clear();
        } else {
            waiting.setVisibility(View.GONE);
            btn_addList.show();
        }
    }

    void refreshList() {
        View view = findViewById(R.id.listView);
        ProgressBar waiting = findViewById(R.id.waiting);
        FloatingActionButton btn_addList = findViewById(R.id.btn_addList);

        ProductListsAPI productList = APIContext.createAPI(ProductListsAPI.class);
        Call<List<ProductListData>> listCall = productList.getAll(APIContext.getSession(this));

        APIContext.makeCall(view, listCall, data -> {
            listAdapter.clear();

            for (ProductListData list : data) {
                listAdapter.add(list);
                listAdapter.sort((a, b) -> b.createdAt.compareTo(a.createdAt));
            }

            setSpinner(false);

        }).exception(t -> {
            Log.wtf("RETROFIT", t);
            Snackbar.make(view, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
            waiting.setVisibility(view.GONE);

        }).call();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            setSpinner(true);
            refreshList();
            return true;
        }

        if (id == R.id.action_logout) {
            APIContext.logout(this);
            APIContext.spawnActivity(this, LoginActivity.class);
            refreshList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
