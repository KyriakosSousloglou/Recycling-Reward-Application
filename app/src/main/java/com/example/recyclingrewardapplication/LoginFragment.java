package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class LoginFragment extends AppCompatActivity {

    TextView txt;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);

        usernameEditText = findViewById(R.id.username_text);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
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

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginFragment.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.2.7/recycling/login.php");
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

                                Toast.makeText(LoginFragment.this, message, Toast.LENGTH_SHORT).show();

                                if (status.equals("success")) {
                                    Intent intent = new Intent(LoginFragment.this, ProfileFragment.class);
                                    startActivity(intent);
                                    // Handle successful login (e.g., open a new activity)
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(LoginFragment.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginFragment.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }












//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_fragment);
//        txt = findViewById(R.id.notRegister);
//        txt.setClickable(true);
//    }
//    public void onClickLogin(View v){
//        Intent intent = new Intent(LoginFragment.this, ProfileFragment.class);
//        startActivity(intent);
//    }
//
//    public void onClickNotRegister(View v){
//        Intent intent = new Intent(LoginFragment.this, SignInFragment.class);
//        startActivity(intent);
//    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }
}