package com.example.ahmadmaulana.qrscan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btnScan, btnLoginInfo, btnLogout;
    TextView tv;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnScan = (Button) findViewById(R.id.btn_scan);
        btnLoginInfo = (Button) findViewById(R.id.btn_info);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        tv = (TextView) findViewById(R.id.tv);

        session = new SessionManager(getApplicationContext());

        btnScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
              //  IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this, CheckoutActivity.class);
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.initiateScan();
            }

        });

        btnLoginInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HashMap<String, String> detail = session.getLoginDetail();
                String token = detail.get(session.KEY_TOKEN);
                String location = detail.get(session.KEY_LOCATION);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Login Info");
                alertDialogBuilder
                        .setMessage("Token: "+token+"\n"+"Location: "+location)
                        .setCancelable(true)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            }

        });

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("qrScan");
                alertDialogBuilder
                        .setMessage("Are you sure you want to log out?")
                        .setCancelable(true)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                session.logout();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            }

        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            tv.setText(scanContent);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Gagal membaca data. Ulangi lagi!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
