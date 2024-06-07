package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
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

public class StatisticsFragment extends AppCompatActivity {
    private String username, achievements;
    private TextView plastic_txt, paper_txt, glass_txt, aluminium_txt, name_txt,recycnling_txt, achievement_txt;
    private static final int FORM_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_fragment);

        username = getIntent().getStringExtra("username");
        plastic_txt = findViewById(R.id.pieces_plastic_quantity);
        paper_txt = findViewById(R.id.pieces_paper_quantity);
        aluminium_txt = findViewById(R.id.pieces_aluminium_quantity);
        glass_txt = findViewById(R.id.pieces_glass_quantity);
        name_txt = findViewById(R.id.name_of_user);
        name_txt.setText(getIntent().getStringExtra("name"));
        achievements = getIntent().getStringExtra("achievements");
        recycnling_txt = findViewById(R.id.keepRecycling);
        achievement_txt = findViewById(R.id.total_achievements_num);
        String htmlString = "<u><font color='#0000FF'>Keep Recycling!</font></u>";
        recycnling_txt.setText(Html.fromHtml(htmlString));


        // Κλήση της AsyncTask για να πάρουμε τα στατιστικά του χρήστη
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
                URL url = new URL("http://10.140.7.200/recycling/statistics.php");
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
                    String plastic = jsonObject.getString("plastic");
                    String glass = jsonObject.getString("glass");
                    String aluminium = jsonObject.getString("aluminium");
                    String paper = jsonObject.getString("paper");

                    paper_txt.setText(paper);
                    plastic_txt.setText(plastic);
                    aluminium_txt.setText(aluminium);
                    glass_txt.setText(glass);
                    achievement_txt.setText(achievements);


                } else {
                    Toast.makeText(StatisticsFragment.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(StatisticsFragment.this, "Error parsing JSON", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void onBackButtonClick(View view) {
        onBackPressed();
    }

}