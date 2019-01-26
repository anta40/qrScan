package com.example.ahmadmaulana.qrscan;

/**
 * Created by Andre Tampubolon (andre.tampubolon@idstar.co.id) on 1/23/2019.
 */
public class Product {
    private String nama, kategori;
    private int hargaAwal, hargaAkhir, jumlah;

    public Product(String nama, String kategori, int hargaAwal, int hargaAkhir, int jumlah){
        this.nama = nama;
        this.kategori = kategori;
        this.hargaAwal = hargaAwal;
        this.hargaAkhir = hargaAkhir;
        this.jumlah = jumlah;
    }

    public String getNama(){ return nama; }
    public String getKategori() { return kategori; }
    public int getHargaAwal() { return hargaAwal; }
    public int getHargaAkhir() { return hargaAkhir; }
    public int getJumlah() { return jumlah; }

}


