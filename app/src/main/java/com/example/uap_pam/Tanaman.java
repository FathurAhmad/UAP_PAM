package com.example.uap_pam;

public class Tanaman {
    private String id;
    private String nama;
    private double harga;
    private String deskripsi;
    public Tanaman() {

    }
    public Tanaman(String id, String nama, double harga, String deskripsi) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
    }
    public String getId() {
        return id;
    }
    public String getNama() {
        return nama;
    }
    public double getHarga() {
        return harga;
    }
    public String getDeskripsi() {
        return deskripsi;
    }
}
