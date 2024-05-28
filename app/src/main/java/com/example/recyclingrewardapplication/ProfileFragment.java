package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ProfileFragment extends AppCompatActivity {
    private ImageView avatar;
    private Button logOut_btn;
    private Button form_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_fragment);
        avatar = findViewById(R.id.avatar);
        avatar.setImageResource(R.drawable.avatar_image);
        logOut_btn = findViewById(R.id.log_out_button);
        form_btn = findViewById(R.id.recording_form_button);
    }

    public void onClickLogOut(View v){
        Intent intent = new Intent(ProfileFragment.this, MainActivity.class);
        startActivity(intent);
    }
    public void onClickForm(View v){
        Intent intent = new Intent(ProfileFragment.this, FormFragment.class);
        startActivity(intent);
    }
}