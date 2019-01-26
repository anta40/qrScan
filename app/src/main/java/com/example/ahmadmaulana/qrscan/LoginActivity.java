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
    private final String BASE_URL = "http://dapuromiyago.com/public/api/login";
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
           // Intent iii = new Intent(LoginActivity.this, MainActivity.class);
          //  iii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //iii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(iii);

            IntentIntegrator scanIntegrator = new IntentIntegrator(LoginActivity.this);
            scanIntegrator.setOrientationLocked(false);
            scanIntegrator.initiateScan();

        }

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
                    .url(BASE_URL)
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

            //Toast.makeText(getApplicationContext(), ">>> "+s, Toast.LENGTH_SHORT).show();

            if (isOK(s)) {
                try {
                    JSONObject jsObj = new JSONObject(s);
                    String token = jsObj.get("token").toString();
                    String location = spinner.getSelectedItem().toString();

                    session.createLoginSession(token, location);
                    //Intent iii = new Intent(LoginActivity.this, MainActivity.class);
                    //startActivity(iii);
                    //finish();
                    IntentIntegrator scanIntegrator = new IntentIntegrator(LoginActivity.this);
                    scanIntegrator.setOrientationLocked(false);
                    scanIntegrator.initiateScan();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null){
            String scanContent = scanningResult.getContents();
            String[] strs = scanContent.split("\\n");

            String imgUrl = strs[1].replace("FN:","");
            String location = strs[2].replace("FN:","");
            int price = Integer.parseInt(strs[3].replace("TEL;WORK;VOICE:",""));

            String slen = ""+imgUrl.length();
            Product2 prod = new Product2(slen, location, price);
            db.insertProduk2(prod);

            Intent iii = new Intent(LoginActivity.this, ScanResultActivity.class);
            startActivity(iii);
        }
    }

    private static boolean isOK(String input) {
        return input.contains("\"success\":\"success\"");
    }
}
