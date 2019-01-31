package com.example.ahmadmaulana.qrscan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Spinner spinner;
    SessionManager session;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.drop_down, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        db = new DBHelper(this);

        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()){
            Intent iii = new Intent(LoginActivity.this, MainActivity.class);
            iii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            iii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(iii);
        }

        Nama = (EditText) findViewById(R.id.etNama);
        Password = (EditText) findViewById(R.id.etPassword);
        Login = (Button) findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new LoginTask(Nama.getText().toString(), Password.getText().toString()).execute();

               /*
               if (Nama.getText().toString().equals("dangridho@ymail.com") && Password.getText().toString().equals("dangdo")) {
                    session.createLoginSession("dangridho@ymail.com", "dangdo");
                    Intent iii = new Intent(LoginActivity.this, MainActivity.class);
                    iii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    iii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(iii);
                }
                */
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
                    .url(URLConfig.API_LOGIN)
                    .addHeader("Accept","application/json")
                    .addHeader("Content-Type","application/json")
                    .addHeader("Authorization",PrefUtil.AUTH_BEARER)
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

            if (isOK(s)) {
                try {
                    JSONObject jsObj = new JSONObject(s);
                    String token = jsObj.get("token").toString();
                    String location_id = ""+spinner.getSelectedItemPosition();

                    session.createLoginSession(token, location_id);
                    Intent iii = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(iii);
                    finish();
                }
                catch (JSONException je) {
                    je.printStackTrace();
                }
            }
            else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                alertDialogBuilder.setTitle("qrScan");
                alertDialogBuilder
                        .setMessage("Not authorised")
                        .setCancelable(true)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            }

        }
    }



    private static boolean isOK(String input) {
        return input.contains("\"success\":\"success\"");
    }
}
