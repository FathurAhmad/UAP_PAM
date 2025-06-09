package com.example.uap_pam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

//public class TanamanAdapter extends RecyclerView.Adapter<TanamanAdapter.TargetViewHolder> {
//    private List<Tanaman> tanamanList;
//    private DashboardActivity activity;
//
//
//    public TanamanAdapter(List<Tanaman> tanamanList, DashboardActivity activity) {
//        this.tanamanList = tanamanList;
//        this.activity = activity;
//    }
//
//    @NonNull
//    @Override
//    public TargetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item, parent, false);
//        return new TargetViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TargetViewHolder holder, int position) {
//        Tanaman tanaman = tanamanList.get(position);
//        holder.btnHapus.setOnClickListener(v -> activity.tanamanDelete(tanaman.getId()));
//        holder.tvNama.setText(tanaman.getNama());
//        holder.tvHarga.setText(String.valueOf(tanaman.getHarga()));
//        holder.tvDeskripsi.setText(tanaman.getDeskripsi());
//        holder.btnDetail.setOnClickListener(v -> {
//            Intent intent = new Intent(activity, DetailActivity.class);
//        });
//    }
//
//    public class TargetViewHolder extends RecyclerView.ViewHolder {
//        public TextView tvNama;
//        public TextView tvHarga;
//        public TextView tvDeskripsi;
//        public Button btnDetail;
//        public Button btnHapus;
//
//
//        public TargetViewHolder(View itemView) {
//            super(itemView);
//            tvNama = itemView.findViewById(R.id.tvNama);
//            tvHarga = itemView.findViewById(R.id.tvHarga);
//            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
//            btnDetail = itemView.findViewById(R.id.btnDetail);
//            btnHapus = itemView.findViewById(R.id.btnHapus);
//        }
//    }
//
//
//
//
//
//    @Override
//    public int getItemCount() {
//        return tanamanList.size();
//    }
//}

public class TanamanAdapter extends RecyclerView.Adapter<TanamanAdapter.ViewHolder> {

    private List<Tanaman> list;
    private TanamanListener listener;

    public interface TanamanListener {
        void onEdit(Tanaman tanaman);
        void onDelete(String id);
        void onDetail(Tanaman tanaman);
    }

    public TanamanAdapter(List<Tanaman> list, TanamanListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvDeskripsi;
        Button btnUpdate, btnDelete, btnDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnHapus);
        }
    }

    @Override
    public TanamanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TanamanAdapter.ViewHolder holder, int position) {
        Tanaman t = list.get(position);
        holder.tvNama.setText(t.getNama());
        holder.tvHarga.setText(String.valueOf(t.getHarga()));
        holder.tvDeskripsi.setText(t.getDeskripsi());
        holder.btnUpdate.setOnClickListener(v -> listener.onEdit(t));
        holder.btnDetail.setOnClickListener(v -> listener.onDetail(t));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(t.getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


