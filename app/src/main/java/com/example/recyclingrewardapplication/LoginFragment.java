package com.example.recyclingrewardapplication;

import static com.example.recyclingrewardapplication.MainActivity.iPv4Address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class LoginFragment extends AppCompatActivity {

    TextView notRegister;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);



        notRegister = findViewById(R.id.notRegister); // μορφοποίηση κειμένου "Click here to sign in"
        String htmlString = "<u>Click here to sign in</u>";
        notRegister.setText(Html.fromHtml(htmlString));



        usernameEditText = findViewById(R.id.username_text_administrator);
        passwordEditText = findViewById(R.id.password_administrator);
        loginButton = findViewById(R.id.button_login_fragment_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }  // αρχίζει η διαδικασία ελέγχου για να συνδεθεί
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
            //Δημιουργία νέου νήματος.
            // Η εργασία που θέλουμε να εκτελέσουμε τοποθετείται μέσα στη μέθοδο run() του Runnable.
            @Override
            public void run() {

                try {
                    //Δημιουργείται μια σύνδεση URL και ορίζεται η μέθοδος αιτήματος ως "POST".
                    URL url = new URL("http://"+iPv4Address+"/recycling/login.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    //Αποστολή δεδομένων στον διακομιστή μέσω του OutputStreamWriter
                    OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    writer.write("username=" + username + "&password=" + password);
                    writer.flush();
                    writer.close();
                    //Ανάγνωση της απάντησης από τον διακομιστή
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
                                //Το JSONObject είναι μια δομή δεδομένων που χρησιμοποιείται για να αναπαραστήσει τα δεδομένα JSON σε Java.
                                JSONObject jsonResponse = new JSONObject(stringBuilder.toString());
                                String status = jsonResponse.getString("status");  // παίρνει από την php τα δεδομένα των μεταβλητών status & message αντίστοιχα
                                String message = jsonResponse.getString("message");
                                Toast.makeText(LoginFragment.this, message, Toast.LENGTH_SHORT).show();

                                if (status.equals("success")) {
                                    String name = jsonResponse.getString("name");
                                    String surname = jsonResponse.getString("surname");
                                    Intent intent = new Intent(LoginFragment.this, ProfileFragment.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("surname", surname);
                                    intent.putExtra("username",username);
                                    startActivity(intent);  // μεταφέρει στο ProfileFragment τις μεταβλητές name,surname,username
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