package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginFragment extends AppCompatActivity {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);
        txt = findViewById(R.id.notRegister);
        txt.setClickable(true);
    }
    public void onButtonLoginFragmentLoginClick(View v){
        Intent intent = new Intent(LoginFragment.this, ProfileFragment.class);
        startActivity(intent);
    }

    public void onClickNotRegister(View v){
        Intent intent = new Intent(LoginFragment.this, SignInFragment.class);
        startActivity(intent);
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }
}