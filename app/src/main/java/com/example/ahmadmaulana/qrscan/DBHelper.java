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

        CREATE_TABLE_CMD = "CREATE TABLE IF NOT EXISTS tbl_produk(id_produk INTEGER, url VARCHAR, nama VARCHAR, " +
                "harga_jual INTEGER, harga_beli INTEGER, jumlah INTEGER, tanggal VARCHAR, lokasi INTEGER);";
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
        values.put("id_produk", product.getId());
        values.put("url", product.getUrl());
        values.put("nama", product.getNama());
        values.put("harga_jual", product.getHargaJual());
        values.put("harga_beli", product.getHargaBeli());
        values.put("jumlah", product.getJumlah());
        values.put("tanggal", product.getDate());
        values.put("lokasi", product.getLocation());

        long id = db.insert("tbl_produk", null, values);
        db.close();

        return id;
    }

    public void setJumlahProduk(int id, int jumlah){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE tbl_produk SET jumlah = "+jumlah +" WHERE id_produk = "+id;
        db.execSQL(query);
        db.close();
    }

    public void tambahJumlahProduk(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE tbl_produk SET jumlah = jumlah + 1 WHERE id_produk = "+id;
        db.execSQL(query);
        db.close();
    }

    public void kurangiJumlahProduk(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE tbl_produk SET jumlah = jumlah - 1 WHERE id_produk = "+id;
        db.execSQL(query);
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

    public boolean isExist(int id){
        boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT nama FROM tbl_produk WHERE id_produk="+id;
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

    public List<Product> getAllProducts(){
        List<Product> prods = new ArrayList<>();
        String query = "SELECT * FROM tbl_produk";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(query, null);

        if (mCursor.moveToFirst()){
            do {
                String url = mCursor.getString(mCursor.getColumnIndex("url"));
                String nama = mCursor.getString(mCursor.getColumnIndex("nama"));
                int jumlah= mCursor.getInt(mCursor.getColumnIndex("jumlah"));
                int id = mCursor.getInt(mCursor.getColumnIndex("id_produk"));
                int hargaJual = mCursor.getInt(mCursor.getColumnIndex("harga_jual"));
                int hargaBeli = mCursor.getInt(mCursor.getColumnIndex("harga_beli"));
                String date = mCursor.getString(mCursor.getColumnIndex("tanggal"));
                int loc = mCursor.getInt(mCursor.getColumnIndex("lokasi"));
                Product prod = new Product(id, url, nama,  hargaJual, hargaBeli, jumlah, date, loc);
                prods.add(prod);

            } while (mCursor.moveToNext());
        }

        db.close();

        return prods;
    }
}
