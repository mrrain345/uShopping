package com.example.ushopping;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void login(View view) {
    }

    public void signup(View view) {
        EditText email = findViewById(R.id.field_email);
        EditText password = findViewById(R.id.field_password);

        Intent signupIntent = new Intent(this, SignupActivity.class);
        signupIntent.putExtra("email", email.getText().toString());
        signupIntent.putExtra("password", password.getText().toString());
        startActivity(signupIntent);
    }
}
