package com.example.ushopping;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.ushopping.api.APIContext;
import com.example.ushopping.api.ListMembersAPI;
import com.example.ushopping.api.ProductsAPI;
import com.example.ushopping.data.IdData;
import com.example.ushopping.data.ProductData;
import com.example.ushopping.data.UserData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;

public class ListMembersAdapter extends ArrayAdapter<UserData> {

    private Context mContext;
    int mResource;
    UUID listId;

    public ListMembersAdapter(Context context, int resource, UUID listId, ArrayList<UserData> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        this.listId = listId ;
    }

    @SuppressLint("ShowToast")
    public View getView(int position, View convertView, ViewGroup parent) {

        UserData user = getItem(position);
        UUID id = user.id;
        String username = user.username;
        String email = user.email;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.firstLine);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.secondLine);

        tvName.setText(username);
        tvEmail.setText(email);

        View listView = parent.findViewById(R.id.lv_addmember);

        Button menu_btn = convertView.findViewById(R.id.menu_btn);
        View finalConvertView = convertView;
        menu_btn.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete?");
            Context context = view.getContext();

            builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                ListMembersAPI api = APIContext.createAPI(ListMembersAPI.class);
                Call<UserData> call = api.delete(listId, id, APIContext.getSession(context));

                APIContext.makeCall(listView, call, data -> {
                    remove(getItem(position));
                    notifyDataSetChanged();

                    UUID userId = APIContext.getUserId(parent.getContext());
                    if (id.compareTo(userId) == 0) {
                        APIContext.spawnActivity(mContext, MainActivity.class);
                    }
                }).call();

            }).setNegativeButton("No", null).show();
        });

        return convertView;
    }
}
