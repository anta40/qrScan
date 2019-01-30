package com.example.ahmadmaulana.qrscan;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends AppCompatActivity {

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        db = new DBHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentIntegrator scanIntegrator = new IntentIntegrator(ScanActivity.this);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.initiateScan();
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

            Intent iii = new Intent(ScanActivity.this, ScanResultActivity.class);
            startActivity(iii);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
