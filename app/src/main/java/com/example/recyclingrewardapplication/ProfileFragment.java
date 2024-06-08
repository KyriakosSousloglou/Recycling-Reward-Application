package com.example.recyclingrewardapplication;

import static com.example.recyclingrewardapplication.MainActivity.iPv4Address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ProfileFragment extends AppCompatActivity {
    private ImageView avatar;
    private Button logOut_btn, form_btn;
    private TextView name_txt, surname_txt, totalpoints_txt, remaining_points_txt;
    private String username, achievements;
    private ProgressBar progressBar;
    private static final int FORM_REQUEST_CODE = 1;

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
        totalpoints_txt = findViewById(R.id.total_points_num);
        remaining_points_txt = findViewById(R.id.remaining_points_num);
        progressBar = findViewById(R.id.progressbar);
        name_txt.setText(getIntent().getStringExtra("name"));
        surname_txt.setText(getIntent().getStringExtra("surname"));
        username = getIntent().getStringExtra("username");

        // Κλήση της AsyncTask για να πάρουμε το total_points
        new GetTotalPointsTask().execute(username);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FORM_REQUEST_CODE && resultCode == RESULT_OK) {
            new GetTotalPointsTask().execute(username);
        }
    }

    private class GetTotalPointsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];

            try {
                URL url = new URL("http://"+iPv4Address+"/recycling/profile.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("status").equals("success")) {
                    String totalPoints = jsonObject.getString("total_points");
                    String points_left = jsonObject.getString("points_left");

                    achievements = jsonObject.getString("achievements");
                    totalpoints_txt.setText(totalPoints);
                    remaining_points_txt.setText(points_left);
                    if(Integer.parseInt(achievements)>=1)
                        Toast.makeText(ProfileFragment.this, "Congratulation, you have earned " + Integer.parseInt(achievements) + " achievement!", Toast.LENGTH_LONG).show();

                    if(Integer.parseInt(totalPoints) <= 200)
                        progressBar.setProgress(Integer.parseInt(totalPoints));
                    else
                        progressBar.setProgress(Integer.parseInt(totalPoints) % 200);

                } else {
                    Toast.makeText(ProfileFragment.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ProfileFragment.this, "Error parsing JSON", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickLogOut(View v) {
        Intent intent = new Intent(ProfileFragment.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickStatistics(View v) {
        Intent intent = new Intent(ProfileFragment.this, StatisticsFragment.class);
        intent.putExtra("username", username);
        intent.putExtra("name", name_txt.getText().toString());
        intent.putExtra("achievements",achievements);
        startActivityForResult(intent, FORM_REQUEST_CODE);
    }

    public void onClickForm(View v) {
        Intent intent = new Intent(ProfileFragment.this, FormFragment.class);
        intent.putExtra("username", username);
        startActivityForResult(intent, FORM_REQUEST_CODE);
    }

}
