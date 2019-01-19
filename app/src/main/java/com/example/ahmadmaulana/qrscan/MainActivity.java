package com.example.ahmadmaulana.qrscan;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
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

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnScan = (Button) findViewById(R.id.btn_scan);
        tv = (TextView) findViewById(R.id.tv);

        btnScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
              //  IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this, CheckoutActivity.class);
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.initiateScan();
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
