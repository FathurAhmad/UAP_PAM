package com.example.uap_pam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView tvNama, tvHarga, tvDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        tvNama = findViewById(R.id.tvNama);
        tvHarga = findViewById(R.id.tvHarga);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);

        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        double harga = intent.getDoubleExtra("harga", 0);
        String deskripsi = intent.getStringExtra("deskripsi");

        tvNama.setText(nama);
        tvHarga.setText(String.valueOf(harga));
        tvDeskripsi.setText(deskripsi);
    }
}
