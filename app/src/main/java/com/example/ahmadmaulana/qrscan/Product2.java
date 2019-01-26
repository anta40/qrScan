package com.example.ahmadmaulana.qrscan;

/**
 * Created by Andre Tampubolon (andre.tampubolon@idstar.co.id) on 1/26/2019.
 */
public class Product2 {

    private String url, lokasi;
    private int harga;

    public Product2(String url, String lokasi, int harga){
        this.url = url;
        this.lokasi = lokasi;
        this.harga = harga;
    }

    public String getUrl() { return url; }
    public String getLokasi() { return lokasi; }
    public int getHarga() { return harga; }
}
