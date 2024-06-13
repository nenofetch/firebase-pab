package com.nenofetch.firebasepab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button btCreateDB;
    private Button btViewDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btCreateDB= findViewById(R.id.bt_createdata);
        btViewDB= findViewById(R.id.bt_viewdata);
        btCreateDB.setOnClickListener(view -> {
            //Kelas yang akan dijalankan ketika tombol Create/insert di Klik
            startActivity(DBCreateActivity.getActIntent(MainActivity.this));
        });
        //startActivity(DBReadActivity.getActIntent(MainActivity.this));
        btViewDB.setOnClickListener(view -> {
            //Kelas untuk View Data
            startActivity(DBReadActivity.getActIntent(MainActivity.this));
        });
    }
}