package com.nenofetch.firebasepab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterDosenRecyclerView extends RecyclerView.Adapter<AdapterDosenRecyclerView.ViewHolder> {
    private final ArrayList<Dosen> daftarDosen;
    private final Context context;

    public AdapterDosenRecyclerView(ArrayList<Dosen> dosens, Context ctx) {
        daftarDosen = dosens;
        context = ctx;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_namadosen);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dosen, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final String name = daftarDosen.get(position).getNama();
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read detail data
            }
        });

        holder.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Delete and update data
                // Update
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button editButton = dialog.findViewById(R.id.bt_edit_data);
                Button delButton = dialog.findViewById(R.id.bt_delete_data);

// Aksi tombol edit di klik
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        context.startActivity(DBCreateActivity.getActIntent((Activity) context)
                                .putExtra("data", daftarDosen.get(position)));
                    }
                });

// Aksi button delete di klik
                delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Delete data
                        String idToDelete = daftarDosen.get(position).getKey(); // Asumsikan setiap item memiliki ID unik
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("dosen").child(idToDelete);

                        databaseReference.removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Menghapus data dari daftar lokal dan memperbarui tampilan
                                dialog.dismiss();
                                daftarDosen.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, daftarDosen.size());
                                Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


                return true;
            }
        });

        holder.tvTitle.setText(name);
    }

    @Override
    public int getItemCount() {
        return daftarDosen.size();
    }
}
