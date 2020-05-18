package com.example.ushopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.ushopping.api.APIContext;
import com.example.ushopping.api.ProductListsApi;
import com.example.ushopping.data.ErrorData;
import com.example.ushopping.data.ListData;
import com.example.ushopping.data.ListSendData;
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
import android.widget.AdapterView;
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

    Retrofit api;
    ListAdapter listAdapter;
    UUID authorisation = UUID.fromString("f7eb77b0-009e-4a4a-8c5e-788df73f3153");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = findViewById(R.id.listView);
        FloatingActionButton btn_addList = findViewById(R.id.btn_addList);
        btn_addList.hide();
        api = APIContext.getContext();

        listAdapter = new ListAdapter(this, R.layout.adapter_main_list, new ArrayList<ListData>());
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ListData list = listAdapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
            intent.putExtra("list_id", list.id.toString());
            intent.putExtra("list_title", list.title);
            intent.putExtra("list_date", list.created_at.getTime());
            startActivity(intent);
        });

        refreshList();

        btn_addList.setOnClickListener(view -> {
            productDialogBuilder(view).show();
        });
    }

    AlertDialog.Builder productDialogBuilder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        builder.setTitle("Add new list");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = input.getText().toString();
            ListSendData lsd = new ListSendData(title);
            ProductListsApi productList = api.create(ProductListsApi.class);
            Call<ListData> listCall = productList.post(authorisation, lsd);

            listCall.enqueue(new Callback<ListData>() {
                @Override
                public void onResponse(Call<ListData> call, Response<ListData> response) {
                    if (ErrorData.show(response, view)) return;

                    ListData list = response.body();
                    Snackbar.make(view, "Created list: " + list.title, Snackbar.LENGTH_LONG).show();

                    ListData create = new ListData(list.title, list.created_at, list.id);
                    listAdapter.insert(create, 0);
                }

                @Override
                public void onFailure(Call<ListData> call, Throwable t) {
                    Log.wtf("RETROFIT", t);
                    Snackbar.make(view, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        return builder;
    }

    void refreshList() {
        View view = findViewById(R.id.listView);
        listAdapter.clear();

        ProgressBar waiting = findViewById(R.id.waiting);
        waiting.setVisibility(View.VISIBLE);

        FloatingActionButton btn_addList = findViewById(R.id.btn_addList);
        btn_addList.hide();

        ProductListsApi productList = api.create(ProductListsApi.class);
        Call<List<ListData>> listCall = productList.getAll(authorisation);

        listCall.enqueue(new Callback<List<ListData>>() {
            @Override
            public void onResponse(Call<List<ListData>> call, Response<List<ListData>> response) {
                if (ErrorData.show(response, view)) return;

                List<ListData> lists = response.body();

                for (ListData list : lists) {
                    ListData item = new ListData(list.title, list.created_at, list.id);
                    listAdapter.add(item);
                    listAdapter.sort((a, b) -> b.created_at.compareTo(a.created_at));
                }

                waiting.setVisibility(View.GONE);
                btn_addList.show();
            }

            @Override
            public void onFailure(Call<List<ListData>> call, Throwable t) {
                Log.wtf("RETROFIT", t);
                Snackbar.make(view, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                ProgressBar waiting = findViewById(R.id.waiting);
                waiting.setVisibility(view.GONE);
            }
        });
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
            refreshList();
            return true;
        }

        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}