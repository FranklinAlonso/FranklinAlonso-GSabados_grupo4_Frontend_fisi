package com.LosF.pasaleladepago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContratoPVE extends AppCompatActivity {
    Button Con, Sin;
    String[] pasarDato = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrato_pve);
        String var = getIntent().getStringExtra("llave");
        pasarDato[0] = var;

        Con = findViewById(R.id.Con);
        Con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarDato[1] = "Con";
                startActivity(new Intent(ContratoPVE.this,PartidaPVE.class).putExtra("llave",pasarDato));
            }
        });
        Sin = findViewById(R.id.Sin);
        Sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasarDato[1] = "Sin";
                startActivity(new Intent(ContratoPVE.this,PartidaPVE.class).putExtra("llave",pasarDato));
            }
        });
    }
}