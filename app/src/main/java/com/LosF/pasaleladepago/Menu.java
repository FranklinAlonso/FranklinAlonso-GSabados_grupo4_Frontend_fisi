package com.LosF.pasaleladepago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends AppCompatActivity {
    Button Jugar, ComoJugar, Salir, Compra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Jugar = findViewById(R.id.Jugar);
        Jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Jugar();
            }
        });
        Compra = findViewById(R.id.Tienda);
        Compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comprar();
            }
        });
        ComoJugar = findViewById(R.id.ComoJugar);
        ComoJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComoJugar();
            }
        });
        Salir = findViewById(R.id.Salir);
        Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Salir();
            }
        });
    }
    private void Jugar(){
        startActivity(new Intent(Menu.this,MenuJugar.class));
    }
    private void Comprar(){ startActivity(new Intent(Menu.this,Tienda.class)); }//Checkout
    private void ComoJugar(){
        startActivity(new Intent(Menu.this,Tutorial1.class));
    }
    private void Salir(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}