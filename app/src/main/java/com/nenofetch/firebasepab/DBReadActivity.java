package com.nenofetch.firebasepab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DBReadActivity extends AppCompatActivity {


    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Dosen> daftarDosen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_db_read);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        /**
         * Inisialisasi RecyclerView & komponennya
         */
        rvView = findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();


        database.child("dosen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarDosen = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Dosen
                     * Dan juga menyimpan primary key pada object Dosen
                     * untuk keperluan Edit dan Delete data
                     */
                    Dosen dosen = noteDataSnapshot.getValue(Dosen.class);
                    if (dosen != null) {
                        dosen.setKey(noteDataSnapshot.getKey());
                        /**
                         * Menambahkan object Dosen yang sudah dimapping
                         * ke dalam ArrayList
                         */
                        daftarDosen.add(dosen);
                    }
                }
                /**
                 * Inisialisasi adapter dan data Dosen dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                adapter = new AdapterDosenRecyclerView(daftarDosen, DBReadActivity.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                System.out.println(databaseError.getDetails() + " " + databaseError.getMessage());
            }
        });
    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, DBReadActivity.class);
    }
}