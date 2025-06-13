package com.example.uap_pam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView tvNama, tvHarga, tvDeskripsi;
    Button btnUpdate;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        apiService = ApiClient.getClient().create(ApiService.class);

        tvNama = findViewById(R.id.tvNama);
        tvHarga = findViewById(R.id.tvHarga);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        btnUpdate = findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String harga = intent.getStringExtra("harga");
        String deskripsi = intent.getStringExtra("deskripsi");

        // Set data awal dari intent
        if (nama != null) tvNama.setText(nama);
        if (harga != null) {
            try {
                double hargaDouble = Double.parseDouble(harga);
                NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                tvHarga.setText(rupiahFormat.format(hargaDouble));
            } catch (NumberFormatException e) {
                tvHarga.setText("Rp 0");
            }
        }
        if (deskripsi != null) tvDeskripsi.setText(deskripsi);

        // Ambil data terbaru dari server
        if (nama != null) {
            apiService.getPlantByName(nama).enqueue(new Callback<TanamanSingleResponse>() {
                @Override
                public void onResponse(Call<TanamanSingleResponse> call, Response<TanamanSingleResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        TanamanSingleResponse singleResponse = response.body();
                        if (singleResponse.getData() != null) {
                            Tanaman tanaman = singleResponse.getData();
                            tvNama.setText(tanaman.getPlant_name());
                            tvDeskripsi.setText(tanaman.getDescription());

                            // Format harga
                            if (tanaman.getPrice() != null) {
                                try {
                                    double hargaDouble = Double.parseDouble(tanaman.getPrice());
                                    NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                                    tvHarga.setText(rupiahFormat.format(hargaDouble));
                                } catch (NumberFormatException e) {
                                    tvHarga.setText("Rp 0");
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<TanamanSingleResponse> call, Throwable t) {
                    // Tetap gunakan data dari intent jika gagal
                }
            });
        }

        btnUpdate.setOnClickListener(v -> {
            Intent intentUpdate = new Intent(DetailActivity.this, UpdateActivity.class);
            intentUpdate.putExtra("nama", nama);
            intentUpdate.putExtra("harga", harga);
            intentUpdate.putExtra("deskripsi", deskripsi);
            startActivity(intentUpdate);
        });
    }
}