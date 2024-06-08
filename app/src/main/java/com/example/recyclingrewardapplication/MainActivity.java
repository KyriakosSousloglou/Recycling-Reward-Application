package com.example.recyclingrewardapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button sign_in_btn, login_btn_administrator, login_btn;
    public static final String iPv4Address = "192.168.2.3"; //change with your IPv4 Address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign_in_btn =findViewById(R.id.button_sign_in);
        login_btn = findViewById(R.id.button_login);

    }
    public void onClickSignIn(View v){
        Intent intent = new Intent(MainActivity.this, SignInFragment.class);
        startActivity(intent);
    }
    public void onClickLogin(View v){
        Intent intent = new Intent(MainActivity.this, LoginFragment.class);
        startActivity(intent);
    }

    public void onClickAdministrator(View v){
        Intent intent = new Intent(MainActivity.this, AdministratorLoginFragment.class);
        startActivity(intent);
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }
}