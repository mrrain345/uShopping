package com.example.ushopping;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.ushopping.api.APICall;
import com.example.ushopping.api.APIContext;
import com.example.ushopping.api.SessionAPI;
import com.example.ushopping.data.LoginData;
import com.example.ushopping.data.SessionPostData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void login(View view) {
        EditText email = findViewById(R.id.field_email);
        EditText password = findViewById(R.id.field_password);

        LoginData loginData = new LoginData(
                email.getText().toString(),
                password.getText().toString()
        );

        SessionAPI api = APIContext.createAPI(SessionAPI.class);
        Call<SessionPostData> call = api.post(loginData);

        APIContext.makeCall(view, call, data -> {
            APIContext.login(this,  data.session);
            APIContext.spawnActivity(this, MainActivity.class);

        }).error(error -> {
            if (error.status == 404) Snackbar.make(view, "Bad email or password", Snackbar.LENGTH_LONG).show();
            else Snackbar.make(view, error.title, Snackbar.LENGTH_LONG).show();

        }).call();
    }

    public void signup(View view) {
        EditText email = findViewById(R.id.field_email);
        EditText password = findViewById(R.id.field_password);

        Intent signupIntent = new Intent(this, SignupActivity.class);
        signupIntent.putExtra("email", email.getText().toString());
        signupIntent.putExtra("password", password.getText().toString());
        startActivity(signupIntent);
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }
}
