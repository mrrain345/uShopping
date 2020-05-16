package com.example.ushopping.data;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ushopping.R;
import com.example.ushopping.api.APIContext;
import com.example.ushopping.api.ProductListApi;
import com.example.ushopping.api.UsersAPI;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewListsActivity extends AppCompatActivity {

    Retrofit api;
    ArrayList<ListData> list_to_view = new ArrayList<>();
    String title = null;
    String date = null;
    UUID authorisation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ushopping.R.layout.activity_view_lists);
        ListView listView = findViewById(R.id.listView);
        Button btn_addList = findViewById(R.id.btn_addList);
        api = APIContext.getContext();
        authorisation = UUID.fromString("f7eb77b0-009e-4a4a-8c5e-788df73f3153");

        /*List a = new List("a","1",1);
        List b = new List("b", "2", 2);
        List c = new List("b", "2", 2);
        List d = new List("b", "2", 2);
        List e = new List("b", "2", 2);
        List f = new List("b", "2", 2);

        list_to_view.add(a);
        list_to_view.add(b);
        list_to_view.add(c);
        list_to_view.add(d);
        list_to_view.add(e);
        list_to_view.add(f);*/

        ListAdapter adapter = new ListAdapter(this, R.layout.adapter_view_list, list_to_view);
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);

        btn_addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("Add new list");
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ListSendData lsd = new ListSendData(title);
                        ProductListApi productList = api.create(ProductListApi.class);
                        Call<ListData> listCall = productList.post(authorisation, lsd);

                        listCall.enqueue(new Callback<ListData>() {
                            @Override
                            public void onResponse(Call<ListData> call, Response<ListData> response) {
                                ErrorData error = ErrorData.getError(response);

                                if (error == null) {
                                    ListData list = response.body();
                                    Log.d("PRODUCTLIST_POST_TEST", "Adding list, Title: " + list.title + ", CreatedAt: " + list.createdAt + "Id:" + list.id);
                                } else {
                                    Log.d("API_TEST", "createAccount: ERROR status: " + error.status + ", code: " + error.code + ", message: " + error.title);
                                }
                            }

                            @Override
                            public void onFailure(Call<ListData> call, Throwable t) {
                                Log.d("API_TEST", "createAccount: EXCEPTION: " + t.getMessage());
                                t.printStackTrace();
                            }
                        });


                        ListData create = new ListData(input.getText().toString(), LocalDate.now().toString(), 2);
                        list_to_view.add(create);
                        Log.d("Input", input.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        });

    }


}
