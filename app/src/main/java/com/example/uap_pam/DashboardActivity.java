package com.example.uap_pam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Tanaman> tanamanList;
    private TanamanAdapter adapter;
    private Button btnTambah;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        apiService = ApiClient.getClient().create(ApiService.class);

        recyclerView = findViewById(R.id.rvItem);
        tanamanList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnTambah = findViewById(R.id.btnTambah);
        btnTambah.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TambahActivity.class);
            startActivity(intent);
        });

        adapter = new TanamanAdapter(tanamanList, this);
        recyclerView.setAdapter(adapter);

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        Log.d("DashboardActivity", "Starting API call to: " + ApiClient.getClient().baseUrl() + "plant/all");

        // Menggunakan TanamanResponse sesuai dengan ApiService
        apiService.getAllPlants().enqueue(new Callback<TanamanResponse>() {
            @Override
            public void onResponse(Call<TanamanResponse> call, Response<TanamanResponse> response) {
                Log.d("DashboardActivity", "=== API RESPONSE DEBUG ===");
                Log.d("DashboardActivity", "Response code: " + response.code());
                Log.d("DashboardActivity", "Response message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    TanamanResponse tanamanResponse = response.body();
                    List<Tanaman> dataList = tanamanResponse.getData();

                    Log.d("DashboardActivity", "Response message: " + tanamanResponse.getMessage());
                    Log.d("DashboardActivity", "Data list: " + (dataList != null ? "not null" : "null"));

                    if (dataList != null) {
                        Log.d("DashboardActivity", "Data size: " + dataList.size());

                        tanamanList.clear();
                        tanamanList.addAll(dataList);

                        // Debug setiap item
                        for (int i = 0; i < tanamanList.size(); i++) {
                            Tanaman t = tanamanList.get(i);
                            if (t != null) {
                                Log.d("DashboardActivity", "Item " + i + ":");
                                Log.d("DashboardActivity", "  Nama: " + t.getPlant_name());
                                Log.d("DashboardActivity", "  Harga: " + t.getPrice());
                                Log.d("DashboardActivity", "  Deskripsi: " + t.getDescription());
                            } else {
                                Log.d("DashboardActivity", "Item " + i + " is null");
                            }
                        }

                        // Update UI di main thread
                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();

                            if (tanamanList.size() > 0) {
                                Toast.makeText(DashboardActivity.this,
                                        "Data berhasil dimuat: " + tanamanList.size() + " item",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DashboardActivity.this,
                                        "Data kosong dari server",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.e("DashboardActivity", "Data list is null");
                        runOnUiThread(() -> {
                            Toast.makeText(DashboardActivity.this,
                                    "Data dari server kosong",
                                    Toast.LENGTH_LONG).show();
                        });
                    }
                } else {
                    Log.e("DashboardActivity", "Response not successful");
                    Log.e("DashboardActivity", "Error code: " + response.code());
                    Log.e("DashboardActivity", "Error message: " + response.message());

                    // Log error body jika ada
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e("DashboardActivity", "Error body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e("DashboardActivity", "Error reading error body", e);
                    }

                    runOnUiThread(() -> {
                        Toast.makeText(DashboardActivity.this,
                                "Gagal memuat data. Kode error: " + response.code(),
                                Toast.LENGTH_LONG).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<TanamanResponse> call, Throwable t) {
                Log.e("DashboardActivity", "=== API CALL FAILED ===");
                Log.e("DashboardActivity", "Error message: " + t.getMessage());
                Log.e("DashboardActivity", "Error class: " + t.getClass().getSimpleName());
                Log.e("DashboardActivity", "Call URL: " + call.request().url().toString());
                t.printStackTrace();

                runOnUiThread(() -> {
                    Toast.makeText(DashboardActivity.this,
                            "Koneksi gagal: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    public void hapusTanaman(String name) {
        Log.d("DashboardActivity", "Attempting to delete: " + name);

        apiService.deletePlant(name).enqueue(new Callback<TanamanSingleResponse>() {
            @Override
            public void onResponse(Call<TanamanSingleResponse> call, Response<TanamanSingleResponse> response) {
                Log.d("DashboardActivity", "Delete response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    TanamanSingleResponse singleResponse = response.body();

                    if ("success".equals(singleResponse.getMessage())) {
                        runOnUiThread(() -> {
                            Toast.makeText(DashboardActivity.this, "Tanaman berhasil dihapus", Toast.LENGTH_SHORT).show();
                            loadData(); // refresh data
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(DashboardActivity.this, "Gagal menghapus tanaman", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Log.e("DashboardActivity", "Delete failed. Code: " + response.code());
                    runOnUiThread(() -> {
                        Toast.makeText(DashboardActivity.this, "Gagal menghapus. Kode: " + response.code(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<TanamanSingleResponse> call, Throwable t) {
                Log.e("DashboardActivity", "Delete network error", t);
                runOnUiThread(() -> {
                    Toast.makeText(DashboardActivity.this, "Koneksi gagal saat menghapus: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}