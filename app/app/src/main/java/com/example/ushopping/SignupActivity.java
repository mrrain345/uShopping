package com.example.ushopping;

import android.os.Bundle;

import com.example.ushopping.api.APIContext;
import com.example.ushopping.data.ErrorData;
import com.example.ushopping.data.SignUpData;
import com.example.ushopping.data.UserData;
import com.example.ushopping.api.UsersAPI;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupActivity extends AppCompatActivity {

    Retrofit api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        EditText email = findViewById(R.id.field_email);
        EditText password = findViewById(R.id.field_password);

        email.setText(bundle.getString("email"));
        password.setText(bundle.getString("password"));

        api = APIContext.getContext();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }

    public void createAccount(View view) {
        EditText username = findViewById(R.id.field_username);
        EditText email = findViewById(R.id.field_email);
        EditText password = findViewById(R.id.field_password);
        EditText cpassword = findViewById(R.id.field_cpassword);

        SignUpData data = new SignUpData();
        data.username = username.getText().toString();
        data.email = email.getText().toString();
        data.password = password.getText().toString();
        data.confirmPassword = cpassword.getText().toString();

        UsersAPI users = api.create(UsersAPI.class);
        Call<UserData> userCall = users.post(data);

        userCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (ErrorData.show(response, view)) return;

                UserData user = response.body();
                Log.d("API_TEST", "createAccount: id: " + user.id + ", username: " + user.username);
                Snackbar.make(view, "Account created", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.wtf("RETROFIT", t);
                Snackbar.make(view, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
