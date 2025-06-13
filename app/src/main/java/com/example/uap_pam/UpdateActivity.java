package com.example.uap_pam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {
    EditText etNama, etHarga, etDeskripsi;
    Button btnSimpan;
    ApiService apiService;
    String originalNama; // Untuk menyimpan nama asli untuk API call

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_update);
        apiService = ApiClient.getClient().create(ApiService.class);

        etNama = findViewById(R.id.etNama);
        etHarga = findViewById(R.id.etHarga);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        btnSimpan = findViewById(R.id.btnSimpan);

        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String harga = intent.getStringExtra("harga");
        String deskripsi = intent.getStringExtra("deskripsi");

        originalNama = nama; // Simpan nama asli

        // Set data awal
        if (nama != null) etNama.setText(nama);
        if (harga != null) etHarga.setText(harga);
        if (deskripsi != null) etDeskripsi.setText(deskripsi);

        // Ambil data terbaru dari server - Perbaikan: gunakan getPlantByName
        if (nama != null) {
            apiService.getPlantByName(nama).enqueue(new Callback<TanamanSingleResponse>() {
                @Override
                public void onResponse(Call<TanamanSingleResponse> call, Response<TanamanSingleResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        TanamanSingleResponse singleResponse = response.body();
                        if (singleResponse.getData() != null) {
                            Tanaman tanaman = singleResponse.getData();
                            etNama.setText(tanaman.getPlant_name());
                            etHarga.setText(tanaman.getPrice());
                            etDeskripsi.setText(tanaman.getDescription());
                        }
                    }
                }

                @Override
                public void onFailure(Call<TanamanSingleResponse> call, Throwable t) {
                    // Tetap gunakan data dari intent jika gagal
                }
            });
        }

        btnSimpan.setOnClickListener(v -> {
            String namaBaru = etNama.getText().toString().trim();
            String hargaBaru = etHarga.getText().toString().trim();
            String deskripsiBaru = etDeskripsi.getText().toString().trim();

            // Validasi input
            if (namaBaru.isEmpty()) {
                Toast.makeText(this, "Nama tanaman tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (hargaBaru.isEmpty()) {
                Toast.makeText(this, "Harga tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (deskripsiBaru.isEmpty()) {
                Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perbaikan: Membuat objek Tanaman menggunakan constructor yang tersedia
            Tanaman updatedPlant = new Tanaman(namaBaru, deskripsiBaru, hargaBaru);

            // Gunakan nama asli untuk path parameter
            apiService.updatePlant(originalNama, updatedPlant).enqueue(new Callback<TanamanSingleResponse>() {
                @Override
                public void onResponse(Call<TanamanSingleResponse> call, Response<TanamanSingleResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        TanamanSingleResponse singleResponse = response.body();
                        if ("success".equals(singleResponse.getMessage()) || singleResponse.getData() != null) {
                            Toast.makeText(UpdateActivity.this, "Tanaman berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            finish(); // kembali ke dashboard
                        } else {
                            Toast.makeText(UpdateActivity.this, "Gagal update tanaman", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UpdateActivity.this, "Gagal update. Kode: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TanamanSingleResponse> call, Throwable t) {
                    Toast.makeText(UpdateActivity.this, "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}