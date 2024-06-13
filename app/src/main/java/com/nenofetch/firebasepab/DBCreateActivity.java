package com.nenofetch.firebasepab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DBCreateActivity extends AppCompatActivity {
    private DatabaseReference database;

    private Button btSubmit;
    private EditText etNik;
    private EditText etNama;
    private Spinner etJa;

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, DBCreateActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_db_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNik = findViewById(R.id.nik);
        etNama = findViewById(R.id.nama_dosen);
        etJa = findViewById(R.id.spinnerJA);
        btSubmit = findViewById(R.id.bt_submit);

        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().getReference();

        //Final Update
        final Dosen dosen = (Dosen) getIntent().getSerializableExtra("data");
        if (dosen != null) {
            //ini untuk update
            etNik.setText(dosen.getNik());
            etNama.setText(dosen.getNama());
            etJa.getSelectedItem().toString();
            btSubmit.setOnClickListener(view -> {
                dosen.setNik(etNik.getText().toString());
                dosen.setNama(etNama.getText().toString());
                dosen.setJa(etJa.getSelectedItem().toString());
                updateDosen(dosen);
            });
        } else {
            btSubmit.setOnClickListener(view -> {
                if (!isEmpty(etNik.getText().toString()) && !isEmpty(etNama.getText().toString())) {
                    submitDosen(new Dosen(etNik.getText().toString(), etNama.getText().toString(), etJa.getSelectedItem().toString()));
                } else {
                    Snackbar.make(findViewById(R.id.bt_submit), "Data Dosen tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etNama.getWindowToken(), 0);
            });
        }
    }

    private boolean isEmpty(String s) {

        return TextUtils.isEmpty(s);
    }

    private void updateDosen(Dosen dosen) {
        // Update Dosen
        database.child("dosen").child(dosen.getKey()).setValue(dosen).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(findViewById(R.id.bt_submit), "Data Berhasil di Update", Snackbar.LENGTH_LONG).setAction("OKE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }).show();
            }
        });
    }

    private void submitDosen(Dosen dosen) {
        database.child("dosen").push().setValue(dosen).addOnSuccessListener(this, aVoid -> {
            etNik.setText("");
            etNama.setText("");
            etJa.setSelection(0);
            Toast.makeText(DBCreateActivity.this, "Data Dosen berhasil ditambahkan", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Snackbar.make(findViewById(R.id.bt_submit), "Gagal menambahkan data", Snackbar.LENGTH_LONG).show();
        });
    }
}