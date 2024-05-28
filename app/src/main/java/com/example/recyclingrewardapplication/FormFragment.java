package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

public class FormFragment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fragment);
    }
    public void onClickAdd(View v){
        Toast.makeText(this, "Materials were registered successfully", Toast.LENGTH_LONG).show();
    }
    public void onBackButtonClick(View view) {
        onBackPressed();
    }

}
