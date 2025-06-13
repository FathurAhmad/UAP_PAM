package com.example.uap_pam;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TanamanAdapter extends RecyclerView.Adapter<TanamanAdapter.TanamanViewHolder> {
    private List<Tanaman> tanamanList;
    private DashboardActivity activity;

    public TanamanAdapter(List<Tanaman> tanamanList, DashboardActivity activity) {
        this.tanamanList = tanamanList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TanamanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new TanamanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TanamanViewHolder holder, int position) {
        Tanaman tanaman = tanamanList.get(position);

        // Debug logging
        Log.d("TanamanAdapter", "Binding position: " + position);
        Log.d("TanamanAdapter", "Tanaman nama: " + tanaman.getPlant_name());
        Log.d("TanamanAdapter", "Tanaman harga: " + tanaman.getPrice());
        Log.d("TanamanAdapter", "Tanaman deskripsi: " + tanaman.getDescription());

        // Set nama tanaman
        if (tanaman.getPlant_name() != null && !tanaman.getPlant_name().isEmpty()) {
            holder.tvNama.setText(tanaman.getPlant_name());
        } else {
            holder.tvNama.setText("Nama tidak tersedia");
        }

        // Set harga dengan format Rupiah
        if (tanaman.getPrice() != null && !tanaman.getPrice().isEmpty()) {
            try {
                double harga = Double.parseDouble(tanaman.getPrice());
                NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                String hargaFormatted = rupiahFormat.format(harga);
                holder.tvHarga.setText(hargaFormatted);
            } catch (NumberFormatException e) {
                holder.tvHarga.setText("Rp 0");
            }
        } else {
            holder.tvHarga.setText("Rp 0");
        }

        // Set click listener untuk tombol hapus
        holder.btnHapus.setOnClickListener(v -> {
            if (tanaman.getPlant_name() != null && !tanaman.getPlant_name().isEmpty()) {
                activity.hapusTanaman(tanaman.getPlant_name());
            }
        });

        // Set click listener untuk tombol detail
        holder.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtra("nama", tanaman.getPlant_name());
            intent.putExtra("harga", tanaman.getPrice());
            intent.putExtra("deskripsi", tanaman.getDescription());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tanamanList != null ? tanamanList.size() : 0;
    }

    public static class TanamanViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama;
        public TextView tvHarga;
        public Button btnDetail;
        public Button btnHapus;

        public TanamanViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
    }
}