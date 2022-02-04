package com.LosF.pasaleladepago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class muestraGanador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestra_ganador);
        String ganador = getIntent().getStringExtra("ganador");
        TextView tv;
        tv=findViewById(R.id.texto);
        tv.setText(ganador);
        Button Volver;
        Volver = findViewById(R.id.VolverMenu);
        Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(muestraGanador.this,Menu.class));
            }
        });
    }
}