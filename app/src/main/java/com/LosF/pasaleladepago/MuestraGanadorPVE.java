package com.LosF.pasaleladepago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MuestraGanadorPVE extends AppCompatActivity {
    String ganador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestra_ganador_pve);
        ganador = getIntent().getStringExtra("ganador");
        TextView txt = findViewById(R.id.txtganador);
        txt.setText(ganador);
        Button Volver;
        Volver = findViewById(R.id.btnMenu);
        Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MuestraGanadorPVE.this,Menu.class));
            }
        });
    }
}