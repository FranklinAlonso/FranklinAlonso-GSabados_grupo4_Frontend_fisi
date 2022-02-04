package com.LosF.pasaleladepago;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Contrato extends AppCompatActivity {
    Button Toma,ContratoSin,ContratonCon;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_contrato);
        DisplayMetrics dm = new DisplayMetrics();
        String[] codigo = getIntent().getStringArrayExtra("llave");
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.7));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        mDatabase.child("Partida").child(codigo[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Partida P = snapshot.getValue(Partida.class);
                if (codigo[1].equals("0")) {
                    ContratonCon = (Button) findViewById(R.id.contratocon); //multiplicador 2
                    ContratonCon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            P.Jugadores.get(1).setMultiplicador(2);
                            P.Jugadores.get(0).setMultiplicador(1);
                            //P1 se lleva el perro
                            P.llevarPerro(0);
                            P.Jugadores.get(1).mazoGanada = new MazoGanada();
                            System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*");
                            System.out.println(P.Jugadores.get(1).mazoGanada.ganado.size());
                            System.out.println(P.Jugadores.get(0).mazoGanada.ganado.size());
                            System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*");
                            //Se guarda Partida
                            mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                            finish();
                            //mDatabase.child("Partida").child(codigo[0]).child("Jugadores").child(codigo[1]).setValue(P1);
                        }
                    });
                    ContratoSin = (Button) findViewById(R.id.contratosin); //multiplicador 3
                    ContratoSin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            P.Jugadores.get(0).setMultiplicador(3);
                            P.Jugadores.get(1).setMultiplicador(1);
                            P.Jugadores.get(0).mazoGanada = new MazoGanada();
                            //P2 se lleva las cartas
                            P.llevarPerro(1);
                            //Se guarda Partida
                            System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*");
                            System.out.println(P.Jugadores.get(1).mazoGanada.ganado.size());
                            System.out.println(P.Jugadores.get(0).mazoGanada.ganado.size());
                            System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*");
                            mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                            finish();
                            //mDatabase.child("Partida").child(codigo[0]).child("Jugadores").child(codigo[1]).setValue(P1);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*

        ContratonCon = (Button) findViewById(R.id.contratocon); //multiplicador 2
                                ContratonCon.setVisibility(View.INVISIBLE);
                                ContratonCon.setEnabled(false);
                                ContratoSin = (Button) findViewById(R.id.contratosin); //multiplicador 3
                                ContratoSin.setVisibility(View.INVISIBLE);
                                ContratoSin.setEnabled(false);

        Toma = (Button) findViewById(R.id.toma); //multiplicador 1
        Toma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toma.setVisibility(View.INVISIBLE);
                Toma.setEnabled(false);
            }
        });
        ContratonCon = (Button) findViewById(R.id.contratocon); //multiplicador 2
        ContratonCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContratonCon.setVisibility(View.INVISIBLE);
                ContratonCon.setEnabled(false);
            }
        });
        ContratoSin = (Button) findViewById(R.id.contratosin); //multiplicador 3
        ContratoSin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContratoSin.setVisibility(View.INVISIBLE);
                ContratoSin.setEnabled(false);
            }
        });*/
    }

}