package com.example.ushopping.data;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.ushopping.R;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ushopping.R.layout.activity_add_product);
        EditText listname = findViewById(R.id.et_Addlist);
        ListView added_products = findViewById(R.id.lv_addedproduct);
        Button add_product = findViewById(R.id.btn_addproduct);
        Button save_list = findViewById(R.id.btn_saveList);
        EditText input_product = new EditText(this);
        EditText input_quantity = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }
}

