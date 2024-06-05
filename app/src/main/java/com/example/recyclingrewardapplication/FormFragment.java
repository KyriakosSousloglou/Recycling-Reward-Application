package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.ArrayList;
import java.util.List;



public class FormFragment extends AppCompatActivity {
    private String username;
    private EditText plasticEditText, glassEditText, aluminiumEditText, paperEditText, generalWasteEditText;
    private Button submitButton;

    private int[] totals = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fragment);

        // Λάβετε το username από το Intent
        username = getIntent().getStringExtra("username");

        plasticEditText = findViewById(R.id.plastic_text);
        glassEditText = findViewById(R.id.glass_text);
        aluminiumEditText = findViewById(R.id.aluminium_text);
        paperEditText = findViewById(R.id.paper_text);
        generalWasteEditText = findViewById(R.id.general_waste_text);
        submitButton = findViewById(R.id.addBtn);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plastic = plasticEditText.getText().toString();
                String glass = glassEditText.getText().toString();
                String aluminium = aluminiumEditText.getText().toString();
                String paper = paperEditText.getText().toString();
                String generalWaste = generalWasteEditText.getText().toString();

                totals[0] += Integer.parseInt(plastic);
                totals[1] += Integer.parseInt(glass);
                totals[2] += Integer.parseInt(aluminium);
                totals[3] += Integer.parseInt(paper);
                totals[4] += Integer.parseInt(generalWaste);

                new UpdateDataTask().execute(username, String.valueOf(totals[0]), String.valueOf(totals[1]), String.valueOf(totals[2]), String.valueOf(totals[3]), String.valueOf(totals[4]));
            }
        });
    }

    private class UpdateDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String plastic = params[1];
            String glass = params[2];
            String aluminium = params[3];
            String paper = params[4];
            String generalWaste = params[5];

            try {
                URL url = new URL("http://192.168.1.51/recycling/form.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

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

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Επιστροφή της απάντησης ως συμβολοσειρά

                return response.toString();
            }
                catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
       protected void onPostExecute(String result) {
            Toast.makeText(FormFragment.this, result, Toast.LENGTH_LONG).show();
        }
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }

}