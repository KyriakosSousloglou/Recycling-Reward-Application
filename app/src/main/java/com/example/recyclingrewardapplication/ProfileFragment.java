package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends AppCompatActivity {
    private ImageView avatar;
    private Button logOut_btn, form_btn;
    private TextView name_txt, surname_txt, totalpoints;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_fragment);

        avatar = findViewById(R.id.avatar);
        avatar.setImageResource(R.drawable.avatar_image);
        logOut_btn = findViewById(R.id.log_out_button);
        form_btn = findViewById(R.id.recording_form_button);
        name_txt = findViewById(R.id.name_textView);
        surname_txt = findViewById(R.id.surnname_textView);
        totalpoints = findViewById(R.id.total_points_num);

        name_txt.setText(getIntent().getStringExtra("name"));
        surname_txt.setText(getIntent().getStringExtra("surname"));
        username = getIntent().getStringExtra("username");

        // Λήψη των συνολικών πόντων από τα SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("RecyclingPrefs", Context.MODE_PRIVATE);
        int totalPoints = sharedPref.getInt("totalPoints", 0);
        totalpoints.setText(String.valueOf(totalPoints));
    }

    public void onClickLogOut(View v){
        Intent intent = new Intent(ProfileFragment.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickForm(View v){
        Intent intent = new Intent(ProfileFragment.this, FormFragment.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
