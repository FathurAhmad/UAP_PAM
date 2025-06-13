package com.example.uap_pam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {

    private EditText etNama, etHarga, etDeskripsi;
    private Button btnTambah;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_tambah);
        apiService = ApiClient.getClient().create(ApiService.class);

        etNama = findViewById(R.id.etNama);
        etHarga = findViewById(R.id.etHarga);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        btnTambah = findViewById(R.id.btnTambah);

        btnTambah.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String hargaStr = etHarga.getText().toString().trim();
            String deskripsi = etDeskripsi.getText().toString().trim();

            // Validasi input
            if (nama.isEmpty()) {
                Toast.makeText(this, "Nama tanaman tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (hargaStr.isEmpty()) {
                Toast.makeText(this, "Harga tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (deskripsi.isEmpty()) {
                Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            // Membuat TanamanRequest sesuai dengan ApiService
            TanamanRequest request = new TanamanRequest(nama, deskripsi, hargaStr);

            apiService.createPlant(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(TambahActivity.this, "Tanaman berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        finish(); // Kembali ke dashboard
                    } else {
                        Toast.makeText(TambahActivity.this, "Gagal menambahkan tanaman. Kode: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(TambahActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}