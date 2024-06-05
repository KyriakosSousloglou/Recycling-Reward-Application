package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FormFragment extends AppCompatActivity {
    private String username;
    private EditText plasticEditText, glassEditText, aluminiumEditText, paperEditText, generalWasteEditText;
    private Button submitButton;
    private int[] totals = new int[5];
    private static final String TAG = "FormFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fragment);

        // Λήψη του username από το Intent
        username = getIntent().getStringExtra("username");

        plasticEditText = findViewById(R.id.plastic_text);
        glassEditText = findViewById(R.id.glass_text);
        aluminiumEditText = findViewById(R.id.aluminium_text);
        paperEditText = findViewById(R.id.paper_text);
        generalWasteEditText = findViewById(R.id.general_waste_text);
        submitButton = findViewById(R.id.addBtn);

        // Φόρτωση των προηγούμενων πόντων από τα SharedPreferences
        loadPreviousPoints();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add button clicked");
                try {
                    String plastic = plasticEditText.getText().toString();
                    String glass = glassEditText.getText().toString();
                    String aluminium = aluminiumEditText.getText().toString();
                    String paper = paperEditText.getText().toString();
                    String generalWaste = generalWasteEditText.getText().toString();

                    if (plastic.isEmpty() || glass.isEmpty() || aluminium.isEmpty() || paper.isEmpty() || generalWaste.isEmpty()) {
                        Toast.makeText(FormFragment.this, "All fields are required", Toast.LENGTH_LONG).show();
                        return;
                    }

                    totals[0] += Integer.parseInt(plastic);
                    totals[1] += Integer.parseInt(glass);
                    totals[2] += Integer.parseInt(aluminium);
                    totals[3] += Integer.parseInt(paper);
                    totals[4] += Integer.parseInt(generalWaste);

                    Log.d(TAG, "New totals: " + totals[0] + ", " + totals[1] + ", " + totals[2] + ", " + totals[3] + ", " + totals[4]);

                    new UpdateDataTask().execute(username, String.valueOf(totals[0]), String.valueOf(totals[1]), String.valueOf(totals[2]), String.valueOf(totals[3]), String.valueOf(totals[4]));
                } catch (NumberFormatException e) {
                    Toast.makeText(FormFragment.this, "Please enter valid numbers", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "NumberFormatException: " + e.getMessage());
                }
            }
        });
    }

    private void loadPreviousPoints() {
        SharedPreferences sharedPref = getSharedPreferences("RecyclingPrefs", Context.MODE_PRIVATE);
        totals[0] = sharedPref.getInt("plastic", 0);
        totals[1] = sharedPref.getInt("glass", 0);
        totals[2] = sharedPref.getInt("aluminium", 0);
        totals[3] = sharedPref.getInt("paper", 0);
        totals[4] = sharedPref.getInt("generalWaste", 0);
        Log.d(TAG, "Loaded previous points: " + totals[0] + ", " + totals[1] + ", " + totals[2] + ", " + totals[3] + ", " + totals[4]);
    }

    private void savePoints() {
        SharedPreferences sharedPref = getSharedPreferences("RecyclingPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("plastic", totals[0]);
        editor.putInt("glass", totals[1]);
        editor.putInt("aluminium", totals[2]);
        editor.putInt("paper", totals[3]);
        editor.putInt("generalWaste", totals[4]);
        editor.apply();
        Log.d(TAG, "Saved points: " + totals[0] + ", " + totals[1] + ", " + totals[2] + ", " + totals[3] + ", " + totals[4]);
    }

    private class UpdateDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "UpdateDataTask started");
        }

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String plastic = params[1];
            String glass = params[2];
            String aluminium = params[3];
            String paper = params[4];
            String generalWaste = params[5];

            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://192.168.1.10/recycling/form.php");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("plastic", "UTF-8") + "=" + URLEncoder.encode(plastic, "UTF-8") + "&" +
                        URLEncoder.encode("glass", "UTF-8") + "=" + URLEncoder.encode(glass, "UTF-8") + "&" +
                        URLEncoder.encode("aluminium", "UTF-8") + "=" + URLEncoder.encode(aluminium, "UTF-8") + "&" +
                        URLEncoder.encode("paper", "UTF-8") + "=" + URLEncoder.encode(paper, "UTF-8") + "&" +
                        URLEncoder.encode("general_waste", "UTF-8") + "=" + URLEncoder.encode(generalWaste, "UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                Log.d(TAG, "Data sent: " + data);

                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                Log.d(TAG, "Server response: " + response.toString());
                return response.toString();
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
                e.printStackTrace();
                return "Error: " + e.getMessage();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing reader: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "UpdateDataTask completed with result: " + result);
            Toast.makeText(FormFragment.this, result, Toast.LENGTH_LONG).show();

            // Αποθήκευση των ενημερωμένων πόντων
            savePoints();

            // Υπολογισμός συνολικών πόντων
            int totalPoints = totals[0] + totals[1] + totals[2] + totals[3] + totals[4];

            Log.d(TAG, "Total points calculated: " + totalPoints);

            // Αποθήκευση συνολικών πόντων σε SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("RecyclingPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("totalPoints", totalPoints);
            editor.apply();

            Log.d(TAG, "Total points saved: " + totalPoints);

            // Αποστολή δεδομένων στην ProfileFragment
            Intent intent = new Intent(FormFragment.this, ProfileFragment.class);
            intent.putExtra("username", username); // Στέλνουμε και το username για να το χρησιμοποιήσει η ProfileFragment
            startActivity(intent);
        }
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }
}
