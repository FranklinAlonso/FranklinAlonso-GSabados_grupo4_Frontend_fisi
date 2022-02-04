package com.LosF.pasaleladepago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DificultadesPVE extends AppCompatActivity {
    Button Dif,Nor,Fac;
    String pasarDato;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dificultades_pve);
        Fac = findViewById(R.id.Facil);
        Fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarDato = "Facil";
                startActivity(new Intent(DificultadesPVE.this,ContratoPVE.class).putExtra("llave",pasarDato));
            }
        });
        Nor = findViewById(R.id.Normal);
        Nor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarDato = "Normal";
                startActivity(new Intent(DificultadesPVE.this,ContratoPVE.class).putExtra("llave",pasarDato));
            }
        });
        Dif = findViewById(R.id.Dificil);
        Dif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarDato = "Dificil";
                startActivity(new Intent(DificultadesPVE.this,ContratoPVE.class).putExtra("llave",pasarDato));
            }
        });
    }
}