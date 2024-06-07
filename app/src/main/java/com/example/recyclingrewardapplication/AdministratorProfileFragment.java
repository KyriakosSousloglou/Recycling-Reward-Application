package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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

public class AdministratorProfileFragment extends AppCompatActivity {
    private Button logOut_btn;
    private TextView name_txt, surname_txt, bestRecyclers;
    private String username;
    private static final int FORM_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_profile_fragment);
        name_txt = findViewById(R.id.name_textView);
        surname_txt = findViewById(R.id.surnname_textView);
        name_txt.setText(getIntent().getStringExtra("name"));
        surname_txt.setText(getIntent().getStringExtra("surname"));
        username = getIntent().getStringExtra("username");
        bestRecyclers = findViewById(R.id.bestRecyclers);

        String htmlString = "<u>Best Recyclers</u>";
        bestRecyclers.setText(Html.fromHtml(htmlString));


        // Κλήση της AsyncTask για να πάρουμε τα στατιστικά του χρήστη
        //new StatisticsFragment.GetTotalPointsTask().execute(username);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == FORM_REQUEST_CODE && resultCode == RESULT_OK) {
//            new StatisticsFragment.GetTotalPointsTask().execute(username);
//        }
//    }
//
//    private class GetTotalPointsTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            String username = params[0];
//
//            try {
//                URL url = new URL("http://192.168.2.3/recycling/statistics.php");
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoOutput(true);
//
//                OutputStream os = httpURLConnection.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
//
//                writer.write(data);
//                writer.flush();
//                writer.close();
//                os.close();
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//                StringBuilder response = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    response.append(line);
//                }
//                reader.close();
//
//                return response.toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                if (jsonObject.getString("status").equals("success")) {
//                    String plastic = jsonObject.getString("plastic");
//                    String glass = jsonObject.getString("glass");
//                    String aluminium = jsonObject.getString("aluminium");
//                    String paper = jsonObject.getString("paper");
//
//                    paper_txt.setText(paper);
//                    plastic_txt.setText(plastic);
//                    aluminium_txt.setText(aluminium);
//                    glass_txt.setText(glass);
//                    achievement_txt.setText(achievements);
//
//
//                } else {
//                    Toast.makeText(StatisticsFragment.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(StatisticsFragment.this, "Error parsing JSON", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
    public void onClickLogOut(View v) {
        Intent intent = new Intent(AdministratorProfileFragment.this, MainActivity.class);
        startActivity(intent);
    }
}
