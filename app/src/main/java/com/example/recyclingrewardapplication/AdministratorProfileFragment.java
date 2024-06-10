package com.example.recyclingrewardapplication;

import static com.example.recyclingrewardapplication.MainActivity.iPv4Address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdministratorProfileFragment extends AppCompatActivity {
    // ακολουθείται η ίδια διαδικασία με την ProfileFragment
    private Button logOut_btn;
    private TextView name_txt, surname_txt, bestRecyclers, plastic_user, glass_user, paper_user, aluminium_user, achievement_user, total_points_user;
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
        plastic_user = findViewById(R.id.plastic_user);
        glass_user = findViewById(R.id.user_glass);
        aluminium_user = findViewById(R.id.aluminium_user);
        paper_user = findViewById(R.id.paper_user);
        achievement_user = findViewById(R.id.achievement_user);
        total_points_user = findViewById(R.id.points_user);

        String htmlString = "<u>Best Recyclers</u>";
        bestRecyclers.setText(Html.fromHtml(htmlString));

        // Κλήση της AsyncTask για να πάρουμε τα στατιστικά του χρήστη
        new GetTotalPointsTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FORM_REQUEST_CODE && resultCode == RESULT_OK) {
            new GetTotalPointsTask().execute();
        }
    }

    private class GetTotalPointsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://"+iPv4Address+"/recycling/best_recycler.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");

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
                    String plastic_num = jsonObject.getString("plastic_num");
                    String glass = jsonObject.getString("glass");
                    String glass_num = jsonObject.getString("glass_num");
                    String aluminium = jsonObject.getString("aluminium");
                    String aluminium_num = jsonObject.getString("aluminium_num");
                    String paper = jsonObject.getString("paper");
                    String paper_num = jsonObject.getString("paper_num");
                    String achievements = jsonObject.getString("achievements");
                    String achievements_num = jsonObject.getString("achievements_num");
                    String total_points = jsonObject.getString("total points");
                    String total_points_num = jsonObject.getString("total_points_num");

                    paper_user.setText(paper+"("+paper_num+")");
                    plastic_user.setText(plastic+"("+plastic_num+")");
                    aluminium_user.setText(aluminium+"("+aluminium_num+")");
                    glass_user.setText(glass+"("+glass_num+")");
                    achievement_user.setText(achievements+"("+achievements_num+")");
                    total_points_user.setText(total_points+"("+total_points_num+")");



                } else {
                    Toast.makeText(AdministratorProfileFragment.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AdministratorProfileFragment.this, "Error parsing JSON", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickLogOut(View v) {
        Intent intent = new Intent(AdministratorProfileFragment.this, MainActivity.class);
        startActivity(intent);
    }
}
