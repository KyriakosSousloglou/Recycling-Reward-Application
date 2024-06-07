package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdministratorLoginFragment extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_login_fragment);

        usernameEditText = findViewById(R.id.username_text_administrator);
        passwordEditText = findViewById(R.id.password_administrator);
        loginButton = findViewById(R.id.button_login_fragment_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        final String username = usernameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();


        if (username.isEmpty()) {
            Toast.makeText(AdministratorLoginFragment.this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(AdministratorLoginFragment.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.2.3/recycling/administrator_login.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    writer.write("username=" + username + "&password=" + password);
                    writer.flush();
                    writer.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    final StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonResponse = new JSONObject(stringBuilder.toString());
                                String status = jsonResponse.getString("status");
                                String message = jsonResponse.getString("message");
                                Toast.makeText(AdministratorLoginFragment.this, message, Toast.LENGTH_SHORT).show();

                                if (status.equals("success")) {
                                    String name = jsonResponse.getString("name");
                                    String surname = jsonResponse.getString("surname");
                                    Intent intent = new Intent(AdministratorLoginFragment.this, AdministratorProfileFragment.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("surname", surname);;
                                    startActivity(intent);
                                    // Handle successful login (e.g., open a new activity)
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(AdministratorLoginFragment.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AdministratorLoginFragment.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }
}