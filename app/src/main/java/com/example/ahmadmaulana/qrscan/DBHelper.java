package com.example.ahmadmaulana.qrscan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Andre Tampubolon (andre.tampubolon@idstar.co.id) on 1/23/2019.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dapuromiyago_db";

    private String CREATE_TABLE_CMD;

    public DBHelper(Context ctxt){
        super(ctxt, DATABASE_NAME, null, DATABASE_VERSION);
       // CREATE_TABLE_CMD = "CREATE TABLE IF NOT EXISTS tbl_produk(nama VARCHAR PRIMARY KEY, kategori VARCHAR, " +
         //       "hargaAwal INTEGER, hargaAkhir INTEGER, jumlah INTEGER);";

        CREATE_TABLE_CMD = "CREATE TABLE IF NOT EXISTS tbl_produk(url VARCHAR, lokasi VARCHAR, harga INTEGER);";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_produk");
        onCreate(db);
    }

    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tbl_produk", null, null);
        db.close();
    }

    public long insertProduk2(Product2 prod){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("url", prod.getUrl());
        values.put("lokasi", prod.getLokasi());
        values.put("harga", prod.getHarga());

        long id = db.insert("tbl_produk", null, values);
        db.close();

        return id;
    }


    public long insertProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama", product.getNama());
        values.put("kategori", product.getKategori());
        values.put("hargaAwal", product.getHargaAwal());
        values.put("hargaAkhir", product.getHargaAkhir());
        values.put("jumlah", product.getJumlah());

        long id = db.insert("tbl_produk", null, values);
        db.close();

        return id;
    }

    public void update(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * WHERE jumlah="+product.getJumlah();

        Cursor mCursor = db.rawQuery(query, null);

        if (mCursor != null) mCursor.moveToFirst();
        Product prod = new Product(mCursor.getString(0), mCursor.getString(1),
                mCursor.getInt(2), mCursor.getInt(3), mCursor.getInt(4));
        int jumlahBaru = prod.getJumlah() + 1;

        String newQuery = "UPDATE tbl_produk SET jumlah="+jumlahBaru+" WHERE nama='"+ product.getNama()+"'";
        db.execSQL(newQuery);

        mCursor.close();
        db.close();
    }

    public boolean isExist2(Product2 product){
        boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT url FROM tbl_produk WHERE nama='"+product.getUrl()+"'";
        Cursor mCursor = db.rawQuery(query, null);

        if (mCursor.getCount() > 0) result = true;

        mCursor.close();
        db.close();

        return result;
    }

    public boolean isExist(Product product){
        boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT nama FROM tbl_produk WHERE nama='"+product.getNama()+"'";
        Cursor mCursor = db.rawQuery(query, null);

        if (mCursor.getCount() > 0) result = true;

        mCursor.close();
        db.close();

        return result;
    }

    public List<Product2> getAllProducts2(){
        List<Product2> prods = new ArrayList<>();
        String query = "SELECT * FROM tbl_produk";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(query, null);

        if (mCursor.moveToFirst()){
            do {
                String url = mCursor.getString(mCursor.getColumnIndex("url"));
                String lokasi = mCursor.getString(mCursor.getColumnIndex("lokasi"));
                int harga = mCursor.getInt(mCursor.getColumnIndex("harga"));
                Product2 prod = new Product2(url, lokasi, harga);
                prods.add(prod);

            } while (mCursor.moveToNext());
        }

        db.close();

        return prods;
    }
}
