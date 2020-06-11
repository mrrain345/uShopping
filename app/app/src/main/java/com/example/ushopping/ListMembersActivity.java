package com.example.ushopping;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ushopping.api.APIContext;
import com.example.ushopping.api.ListMembersAPI;
import com.example.ushopping.api.ProductListsAPI;
import com.example.ushopping.api.ProductsAPI;
import com.example.ushopping.data.IdData;
import com.example.ushopping.data.ProductData;
import com.example.ushopping.data.ProductListData;
import com.example.ushopping.data.UserData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;

public class ListMembersActivity extends AppCompatActivity {

    ListView membersListView;
    ListMembersAdapter listMembersAdapter;
    UUID listId;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_members);

        Bundle extras = getIntent().getExtras();
        listId = UUID.fromString(extras.getString("list_id"));
        title = extras.getString("list_title");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        membersListView = findViewById(R.id.lv_addmember);
        FloatingActionButton add_member = findViewById(R.id.btn_addmember);

        listMembersAdapter = new ListMembersAdapter(this, R.layout.adapter_list_members, listId, new ArrayList<UserData>());
        membersListView.setAdapter(listMembersAdapter);

        add_member.setOnClickListener(view -> {
            memberDialogBuilder(view).show();
        });

        setSpinner(true);
        loadMembers();
    }

    void setSpinner(boolean active) {
        ProgressBar waiting = findViewById(R.id.waiting);
        FloatingActionButton btn_addList = findViewById(R.id.btn_addmember);

        if (active) {
            waiting.setVisibility(View.VISIBLE);
            btn_addList.hide();
            listMembersAdapter.clear();
        } else {
            waiting.setVisibility(View.GONE);
            btn_addList.show();
        }
    }

    public void loadMembers(){
        View view = findViewById(R.id.lv_addmember);
        ListMembersAPI api = APIContext.createAPI(ListMembersAPI.class);
        Call<List<UserData>> listCall = api.get(listId, APIContext.getSession(this));

        APIContext.makeCall(view, listCall, data -> {
            listMembersAdapter.clear();
            listMembersAdapter.addAll(data);
            setSpinner(false);
        }).call();

    }

    public AlertDialog.Builder memberDialogBuilder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        builder.setTitle("Share");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Email");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            UserData userData = new UserData();
            userData.email = input.getText().toString();
            ListMembersAPI api = APIContext.createAPI(ListMembersAPI.class);
            Call<UserData> call = api.post(listId, userData, APIContext.getSession(this));

            APIContext.makeCall(view, call, data -> {
                listMembersAdapter.add(data);
            }).call();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            finish();
        });

        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_members, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) finish();

        if (id == R.id.action_list_members_refresh) {
            setSpinner(true);
            loadMembers();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
