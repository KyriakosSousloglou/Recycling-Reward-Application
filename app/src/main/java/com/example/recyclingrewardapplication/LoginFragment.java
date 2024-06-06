package com.example.recyclingrewardapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);


        TextView notRegister = findViewById(R.id.notRegister);
        String htmlString = "<u><font color='#0000FF'>Click here to sign in</font></u>";
        notRegister.setText(Html.fromHtml(htmlString));



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


        if (username.isEmpty()) {
            Toast.makeText(LoginFragment.this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(LoginFragment.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.140.9.171/recycling/login.php");
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
                                    String name = jsonResponse.getString("name");
                                    String surname = jsonResponse.getString("surname");
                                    Intent intent = new Intent(LoginFragment.this, ProfileFragment.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("surname", surname);
                                    intent.putExtra("username",username);
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

    public void onClickNotRegister(View v) {
        Intent intent = new Intent(LoginFragment.this, SignInFragment.class);
        startActivity(intent);
    }

    public void onBackButtonClick(View view) {
        onBackPressed();
    }
}