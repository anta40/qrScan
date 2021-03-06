package com.example.ahmadmaulana.qrscan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.se.omapi.Session;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btnScan, btnLoginInfo, btnLogout, btnCheck;
    TextView tv;
    SessionManager session;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnScan = (Button) findViewById(R.id.btn_scan);
       // btnLoginInfo = (Button) findViewById(R.id.btn_info);
        //btnLogout = (Button) findViewById(R.id.btn_logout);
        tv = (TextView) findViewById(R.id.tv);
      //  main_img = (ImageView) findViewById(R.id.main_img);
       // btnCheck = (Button) findViewById(R.id.btn_check);

        db = new DBHelper(this);

        session = new SessionManager(getApplicationContext());

        btnScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.initiateScan();
            }

        });

        /*
        btnCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent iii = new Intent(MainActivity.this, ScanResultActivity.class);
                startActivity(iii);
            }

        });
        */

        /*
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

                db.clearTable();
            }

        });
        */

        /*
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
        */
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String[] strs = scanContent.split("\\n");

            try {

                int id = Integer.parseInt(strs[1].replace("FN:",""));
                String url = strs[2].replace("FN:","");
                String nama = strs[3].replace("FN:","");
                int hargaJual = Integer.parseInt(strs[4].replace("FN:",""));
                int hargaBeli = Integer.parseInt(strs[5].replace("FN:",""));


                if (db.isExist(id)){
                    db.tambahJumlahProduk(id);
                }
                else {
                    HashMap<String, String> loginInfo = session.getLoginDetail();
                    int location = Integer.parseInt(loginInfo.get(SessionManager.KEY_LOCATION_ID));
                    db.insertProduct(new Product(id, url, nama, hargaJual, hargaBeli, 1, getCurrentDate(), location));
                }

                /*
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("QR Content");
                alertDialogBuilder
                        .setMessage("ID: "+id+"\n"+"URL: "+url+"\n"+"Nama: "+nama+"\n"+"Harga jual: "+hargaJual+"\n"+"Harga beli: "+hargaBeli+"\nRaw content: "+scanContent)
                        .setCancelable(true)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            */

                Intent iii = new Intent(MainActivity.this, ScanResultActivity.class);
                startActivity(iii);
            }
            catch (NumberFormatException nfe){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Exception");
                alertDialogBuilder
                        .setMessage(nfe.getMessage()+"\nRaw content:" +scanContent)
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
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Gagal membaca data. Ulangi lagi!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private String getCurrentDate(){
        String result = "";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        result = dateFormat.format(date);
        return result;
    }
}
