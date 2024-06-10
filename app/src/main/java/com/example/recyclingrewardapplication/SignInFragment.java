package com.example.recyclingrewardapplication;

import static com.example.recyclingrewardapplication.MainActivity.iPv4Address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignInFragment extends AppCompatActivity {

    private EditText username_txt, password_txt, email_txt, phone_txt,name_txt,surname_txt;
    private Button button_signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_fragment);
        name_txt = findViewById(R.id.name_sign_In);
        surname_txt = findViewById(R.id.surname_sign_In);
        username_txt = findViewById(R.id.username_signIn);
        password_txt = findViewById(R.id.password_signIn);
        email_txt = findViewById(R.id.email_signIn);
        phone_txt = findViewById(R.id.phone_signIn);
        button_signIn = findViewById(R.id.button_signIn);


        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_txt.getText().toString().trim();
                String surname = surname_txt.getText().toString().trim();     // το trim() διαγράφει τα κενά δεξιά αριστερά της λέξης
                String username = username_txt.getText().toString().trim();
                String password = password_txt.getText().toString().trim();
                String phone = phone_txt.getText().toString().trim();
                String email = email_txt.getText().toString().trim();
                new RegisterTask().execute(name,surname,username,password, email, phone);

            }
        });
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {
        // το πρώτο string αναφέρεται στα ορίσματα που θα πάρει (6)
        //επειδη δεν χρησιμοποιείται η συνάρτηση Progress της AsyncTask η δεύτερη παράμετρος είναι void
        // το τρίτο string είναι το μήνυμα που θα επιστρέψει η RegisterTask σε περίπτωση επιτυχημένης|αποτυχημένης καταχώρησης
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String surname = params[1];
            String username = params[2];
            String password = params[3];
            String email = params[4];
            String phone = params[5];
            String url = "http://"+iPv4Address+"/recycling/register.php"; // Αντικαταστήστε με την πραγματική διεύθυνση του PHP script

            try {
                //Δημιουργείται μια σύνδεση URL και ορίζεται η μέθοδος αιτήματος ως "POST".
                URL registerUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) registerUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Αποστολή δεδομένων στον server
                // δημιουργεί μια συμβολοσειρά με κωδικοποιημένα δεδομένα URL (URL-encoded data) που θα σταλούν ως το σώμα (body) του HTTP POST αιτήματος

                // παράδειγμα κωδικοποίησης &name=John&surname=Doe&username=johndoe&password=password123&email=john.doe%40example.com&phone=1234567890

                String postData =
                        "&name=" + URLEncoder.encode(name, "UTF-8") +
                                "&surname=" + URLEncoder.encode(surname, "UTF-8") +
                                "&username=" + URLEncoder.encode(username, "UTF-8") +
                                "&password=" + URLEncoder.encode(password, "UTF-8") +
                                "&email=" + URLEncoder.encode(email, "UTF-8")+
                                "&phone=" + URLEncoder.encode(phone, "UTF-8");

                //αποστέλλεται στον διακομιστή μέσω του σώματος του HTTP POST αιτήματος
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                outputStream.close();

                // Ανάγνωση της απάντησης από τον server η οποία αποθηκεύεται σε ένα StringBuilder
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Επιστροφή της απάντησης ως συμβολοσειρά
                return response.toString();
            } catch (IOException e) {    // αν δεν έχει γίνει σωστά η σύνδεση θα επιστρέψει null και θα εμφανίσει το μήνυμα στην 123
                e.printStackTrace();     // αλλιώς έχει γίνει σωστά η σύνδεση και εμφανίζει το μήνυμα της καταχώρησης 119
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {  // ανήκει στην AsyncTask και τρέχει αυτόματα με το που τελειώσει η doInBackground
            if (result != null) {
                Toast.makeText(SignInFragment.this, result, Toast.LENGTH_SHORT).show();  //εμφάνιση μηνύματος καταχώρησης
                Intent intent= new Intent(SignInFragment.this,MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignInFragment.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackButtonClick(View view) { // κουμπί για επιστροφή στο ακριβώς προηγούμενο activity
        onBackPressed();
    }
}