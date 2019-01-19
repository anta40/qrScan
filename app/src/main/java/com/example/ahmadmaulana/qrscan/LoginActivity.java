package com.example.ahmadmaulana.qrscan;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText Nama;
    private EditText Password;
    private Button Login;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.drop_down, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Nama = (EditText) findViewById(R.id.etNama);
        Password = (EditText) findViewById(R.id.etPassword);
        Login = (Button) findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  validate(Nama.getText().toString(),
              //          Password.getText().toString());

               new LoginTask(Nama.getText().toString(), Password.getText().toString()).execute();
            }
        });

    }

    private void validate(String userNama, String userPassword) {

        if (userNama.equals("Admin") && userPassword.equals("12345")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            counter--;

            if (counter == 0) {
                Login.setEnabled(false);
            }
        }
    }

    class LoginTask extends AsyncTask<String, Void, String>{

        private String responseServer;
        private String email, password;

        public LoginTask(String email, String password){
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            responseServer = "";
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient httpClient = new OkHttpClient();

            RequestBody reqBody = new FormBody.Builder()
                    .add("email",email)
                    .add("password",password)
                    .build();

            Request httpRequest = new Request.Builder()
                    .url("http://dapuromiyago.hijitech.com/public/api/login")
                    .addHeader("Accept","application/json")
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    .addHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjlhZjU2MDcyODk1YzY2NDg2MjQ3YmZhODU3MTNjMWY3ZWJmZDdkN2ZiMGUwYjJiNzkzYTY2ZmY2YjlhZjhhYmM3YjNlYTI2ZmY4ZjcyY2E4In0.eyJhdWQiOiIxIiwianRpIjoiOWFmNTYwNzI4OTVjNjY0ODYyNDdiZmE4NTcxM2MxZjdlYmZkN2Q3ZmIwZTBiMmI3OTNhNjZmZjZiOWFmOGFiYzdiM2VhMjZmZjhmNzJjYTgiLCJpYXQiOjE1NDc1MzgyNjEsIm5iZiI6MTU0NzUzODI2MSwiZXhwIjoxNTc5MDc0MjYxLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.xe3w305a9QrJbNGcW87Ndv3-V6-yTCFvUBR2CuFHzMDP33TbbXiHEGmnjdwsly1w-oY-El3AMHCjFA8StxJ_2DRrrNRCJzYf34MoYT53y79ZdzviKd6E8UtTkrJJeahCRZwgt8XXJA-oGYZu8jNTE7dfH7nN6i0stK1CIAF6Aah77g3G5Rjt17KMZ16fHCcDnZevvLdRncGIWQtL8KSI2YbddEQbXKIqFxFE1LPWE4wD9TwEZW-uJjeLlPb_7lrNYkOV7bKM8272Vr5VDdC5asdewhdf9afhxHdozbKOqblKbhoOgy4VA6JhRLEPAqUONLEV5a_Z8RrZrvSduFHPv85NY4VRuTmpFVIFlzKn_8HkmqEbuzq3WPlX8682X3xxvp31y-LC4sFrutNUCfnXgg4NJBvpqh3033RLaMlxphT5vOwkIa5UDIfy4Y3GJ5PzcaAG3ev1z9HAGR52wO94HasGpcIMGyuOkrgezxnZAshyZTMvvDtdBk74_pednsxDJ4eDg_WjAciEfZbB9peTjI4gt7cF6oSt8FCBM_SOFWplJ0bgJjXukbZaKcq1UA1iSlABMzhUYecPACa24_OxG3xG4bExGsomwzvkaFj-HMl5GomqKGWeLM1EM75PEVOcSLtRGIlJyO_NDXJwFiS2uBCatlplT8IpYj-In3i4R0Q")
                    .post(reqBody)
                    .build();

            Response httpResponse = null;

            try {
                httpResponse = httpClient.newCall(httpRequest).execute();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }

            try {
                if (httpResponse != null){
                    responseServer = httpResponse.body().string();
                }
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }

            return responseServer;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(getApplicationContext(), "Result: "+s, Toast.LENGTH_SHORT).show();
        }
    }

}
