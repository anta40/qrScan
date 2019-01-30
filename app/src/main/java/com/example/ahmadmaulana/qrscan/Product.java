package com.example.ahmadmaulana.qrscan;

/**
 * Created by Andre Tampubolon (andre.tampubolon@idstar.co.id) on 1/23/2019.
 */
public class Product {
    private String nama, url;
    private int idProduk, hargaJual, hargaBeli, jumlah;

    public Product(int idProduk, String url, String nama, int hargaJual, int hargaBeli, int jumlah){
        this.idProduk = idProduk;
        this.url = url;
        this.nama = nama;
        this.hargaJual = hargaJual;
        this.hargaBeli = hargaBeli;
        this.jumlah = jumlah;
    }

    public int getId(){ return idProduk; }
    public String getNama(){ return nama; }
    public int getHargaJual() { return hargaJual; }
    public int getHargaBeli() { return hargaBeli; }
    public int getJumlah() { return jumlah; }
    public String getUrl() { return url; }
}


