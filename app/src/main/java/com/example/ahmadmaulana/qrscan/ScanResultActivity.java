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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private Button btnCheckout, btnBuyMore, btnLogout;
    private Product prod;
    private SessionManager session;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result_2);
        recView = (RecyclerView) findViewById(R.id.recycler_view);
        productList = new ArrayList<>();

        db = new DBHelper(this);
        productList.addAll(db.getAllProducts());

        session = new SessionManager(getApplicationContext());

        btnCheckout = (Button) findViewById(R.id.btn_checkout);
        btnBuyMore = (Button) findViewById(R.id.btn_buy_more);
        btnLogout = (Button) findViewById(R.id.btn_logout);

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

        btnBuyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScanResultActivity.this);
                alertDialogBuilder.setTitle("Omiyago");
                alertDialogBuilder.setMessage("Keluar dari aplikasi?");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("YA",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                       session.logout();
                    }
                });
                alertDialogBuilder.setNegativeButton("TIDAK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                       dialog.dismiss();
                    }
                });

                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                JSONArray ja = new JSONArray();
                JSONObject jjj = new JSONObject();
                int total_harga_jual = 0;
                int total_harga_beli = 0;
                HashMap<String, String> login = session.getLoginDetail();

                try {
                    for (Product prod:productList){
                        JSONObject jo = new JSONObject();
                        jo.put("id_produk",""+prod.getId());
                        jo.put("qty", ""+prod.getJumlah());
                        jo.put("harga_jual",""+prod.getHargaJual());
                        jo.put("harga_beli",""+prod.getHargaBeli());

                        total_harga_jual += prod.getHargaJual();
                        total_harga_beli += prod.getHargaBeli();

                        ja.put(jo);
                    }

                    jjj.put("tanggal_order", getCurrentDate());
                    jjj.put("id_lokasi", login.get(SessionManager.KEY_LOCATION_ID));
                    jjj.put("total_harga_jual", ""+total_harga_jual);
                    jjj.put("total_harga_beli", ""+total_harga_beli);
                    jjj.put("order_detail", ja);

                    new CheckoutTask2(jjj.toString()).execute();


                    /*
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScanResultActivity.this);
                    alertDialogBuilder.setTitle("qrScan");
                    alertDialogBuilder
                            .setMessage("JSON: "+jjj.toString())
                            .setCancelable(true)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                   dialog.dismiss();
                                }
                            });

                    AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();
                    */
                }
                catch (JSONException je){

                }

                /*
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
                */
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class CheckoutTask2 extends AsyncTask<String, Void, String>{

        private String jsonData;
        private String responseServer;

        public CheckoutTask2(String jsonData){
            this.jsonData = jsonData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            responseServer = "";
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient httpClient = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request httpRequest = new Request.Builder()
                    .url(URLConfig.API_ORDER)
                    .addHeader("Accept","application/json")
                    .addHeader("Content-Type","application/json")
                    .addHeader("Authorization",PrefUtil.AUTH_BEARER)
                    .post(body)
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
                alertDialogBuilder.setTitle("Omiyago");
                alertDialogBuilder
                        .setMessage("Checkout berhasil")
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
                alertDialogBuilder.setTitle("Omiyago");
                alertDialogBuilder
                        .setMessage("Gagal melakukan checkout")
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
                alertDialogBuilder.setTitle("Omiyago");
                alertDialogBuilder
                        .setMessage("Checkout berhasil")
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
                alertDialogBuilder.setTitle("Omiyago");
                alertDialogBuilder
                        .setMessage("Gagal melakukan checkout: "+s)
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

    private String getCurrentDate(){
        String result = "";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        result = dateFormat.format(date);
        return result;
    }
}
