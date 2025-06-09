package com.example.uap_pam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TambahActivity extends AppCompatActivity {

    private EditText etNama, etHarga, etDeskripsi;
    private Button btnTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_tambah);

        etNama = findViewById(R.id.etNama);
        etHarga = findViewById(R.id.etHarga);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        btnTambah = findViewById(R.id.btnTambah);

        btnTambah.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String hargaStr = etHarga.getText().toString().trim();
            String deskripsi = etDeskripsi.getText().toString().trim();

            if (nama.isEmpty() || hargaStr.isEmpty() || deskripsi.isEmpty()) {
                Toast.makeText(this, "Isi semua kolom!", Toast.LENGTH_SHORT).show();
                return;
            }

            int harga = Integer.parseInt(hargaStr);
            simpanTanaman(nama, harga, deskripsi);
        });
    }

    private void simpanTanaman(String nama, int harga, String deskripsi) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("users").child(userId).child("tanaman");

        String id = dbRef.push().getKey();
        Tanaman tanaman = new Tanaman(id, nama, harga, deskripsi);

        dbRef.child(id).setValue(tanaman)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish(); // kembali ke halaman sebelumnya
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal simpan: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
