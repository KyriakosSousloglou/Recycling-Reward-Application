package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ProfileFragment extends AppCompatActivity {
    private ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_fragment);
        avatar = findViewById(R.id.avatar);
        avatar.setImageResource(R.drawable.avatar_image);
    }
}