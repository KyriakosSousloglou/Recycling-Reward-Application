package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);
    }
    public void onButtonLoginFragmentLoginClick(View v){
        Intent intent = new Intent(LoginFragment.this, FormFragment.class);
        startActivity(intent);
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }
}