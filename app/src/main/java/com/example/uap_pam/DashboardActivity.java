package com.example.uap_pam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Tanaman> tanamanList;
    private TanamanAdapter adapter;
    private Button btnTambah;
    private EditText etNama, etHarga, etDeskripsi;
    private DatabaseReference databaseRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = findViewById(R.id.rvItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference("tanaman").child(uid);

        btnTambah = findViewById(R.id.btnTambah);

        btnTambah.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TambahActivity.class);
            startActivity(intent);
        });

        tanamanList = new ArrayList<>();
        adapter = new TanamanAdapter(tanamanList, new TanamanAdapter.TanamanListener() {
            @Override
            public void onEdit(Tanaman tanaman) {
                showEditDialog(tanaman);
            }

            @Override
            public void onDelete(String id) {
                databaseRef.child(id).removeValue()
                        .addOnSuccessListener(aVoid -> Toast.makeText(DashboardActivity.this, "Tanaman dihapus", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(DashboardActivity.this, "Gagal menghapus", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onDetail(Tanaman tanaman) {
                Intent intent = new Intent(DashboardActivity.this, DetailActivity.class);
                intent.putExtra("id", tanaman.getId());
                intent.putExtra("nama", tanaman.getNama());
                intent.putExtra("harga", tanaman.getHarga());
                intent.putExtra("deskripsi", tanaman.getDeskripsi());
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tanamanList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Tanaman tanaman = ds.getValue(Tanaman.class);
                    tanamanList.add(tanaman);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void tambahTanaman(String nama, int harga, String deskripsi) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("tanaman");

            String id = databaseRef.push().getKey();

            Tanaman tanaman = new Tanaman(id, nama, harga, deskripsi);
            databaseRef.child(id).setValue(tanaman)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Tanaman berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal menambahkan tanaman: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }


    private void showEditDialog(Tanaman tanaman) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.item_update, null);

        EditText editNama = view.findViewById(R.id.etNama);
        EditText editHarga = view.findViewById(R.id.etHarga);
        EditText editDeskripsi = view.findViewById(R.id.etDeskripsi);

        editNama.setText(tanaman.getNama());
        editHarga.setText(String.valueOf(tanaman.getHarga()));
        editDeskripsi.setText(tanaman.getDeskripsi());

        builder.setView(view);
        builder.setTitle("Edit Tanaman");
        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String namaBaru = editNama.getText().toString();
            double hargaBaru = Double.parseDouble(editHarga.getText().toString());
            String deskripsiBaru = editDeskripsi.getText().toString();

            Tanaman updated = new Tanaman(tanaman.getId(), namaBaru, hargaBaru, deskripsiBaru);
            databaseRef.child(updated.getId()).setValue(updated)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Gagal update", Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }
}


