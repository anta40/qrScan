package com.example.ahmadmaulana.qrscan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanResultActivity extends AppCompatActivity {

    private RecyclerView recView;
    private ProductAdapter mAdapter;
    private DBHelper db;
    private List<Product> productList;
    private Product selectedProduct;
    private Button btnCheckout;
    private Product prod;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        recView = (RecyclerView) findViewById(R.id.recycler_view);
        productList = new ArrayList<>();

        db = new DBHelper(this);
        productList.addAll(db.getAllProducts());

        btnCheckout = (Button) findViewById(R.id.btn_checkout);

        mAdapter = new ProductAdapter(this, productList, new ProductAdapter.MyClickListener() {
            @Override
            public void onIncrement(View v, int position) {
                prod = productList.get(position);
                int jumlah = prod.getJumlah();
                jumlah += 1;
                prod.setJumlah(jumlah);
                db.setJumlahProduk(prod.getId(), jumlah);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDecrement(View v, int position) {
                prod = productList.get(position);
                int jumlah = prod.getJumlah();
                if (jumlah > 0){
                    jumlah = jumlah - 1;
                    prod.setJumlah(jumlah);
                    db.setJumlahProduk(prod.getId(), jumlah);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onDelete(View v, int position) {
                prod = productList.get(position);
                db.hapusProduk(prod.getId());
                productList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recView.setLayoutManager(mLayoutManager);
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recView.setAdapter(mAdapter);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Product prod = productList.get(0);

                try {
                    JSONObject jo = new JSONObject();
                    jo.put("id_produk",""+prod.getId());
                    jo.put("tanggal_order",prod.getDate());
                    jo.put("id_lokasi",prod.getLocation());
                    jo.put("qty", prod.getJumlah());
                    jo.put("harga_jual",prod.getHargaJual());
                    jo.put("harga_beli",prod.getHargaBeli());

                    String data = jo.toString();
                    new CheckoutTask(prod.getId(), prod.getDate(), prod.getHargaJual(), prod.getHargaBeli(),
                            prod.getJumlah(), prod.getLocation()).execute();
                }
                catch (JSONException je){
                    je.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class CheckoutTask extends AsyncTask<String, Void, String>{

        private String responseServer;
        private String dataToBePosted;
        private int id, hjual, hbeli, qty, id_lok;
        private String tgl;

        public CheckoutTask(int id, String tgl, int hjual, int hbeli, int qty, int id_lok ){
            this.id = id;
            this.tgl = tgl;
            this.hjual = hjual;
            this.hbeli = hbeli;
            this.qty = qty;
            this.id_lok = id_lok;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            responseServer = "";
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient httpClient = new OkHttpClient();

            //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dataToBePosted);

            RequestBody reqBody = new FormBody.Builder()
                    .add("id_produk", ""+id)
                    .add("tanggal_order", tgl)
                    .add("harga_jual", ""+hjual)
                    .add("harga_beli", ""+hbeli)
                    .add("qty", ""+qty)
                    .add("id_lokasi", ""+id_lok)
                    .build();

            Request httpRequest = new Request.Builder()
                    .url(URLConfig.API_ORDER)
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

            if (isOK(s)){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScanResultActivity.this);
                alertDialogBuilder.setTitle("qrScan");
                alertDialogBuilder
                        .setMessage("Response: "+s+"\nCheckout berhasil")
                        .setCancelable(true)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                db.clearTable();
                                Intent iii = new Intent(ScanResultActivity.this, MainActivity.class);
                                iii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(iii);
                            }
                        });

                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            }
            else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScanResultActivity.this);
                alertDialogBuilder.setTitle("qrScan");
                alertDialogBuilder
                        .setMessage("Respons: "+s+"\nGagal melakukan checkout")
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

    private boolean isOK(String input) {
        return input.toLowerCase().contains("\"success\"");
    }
}
