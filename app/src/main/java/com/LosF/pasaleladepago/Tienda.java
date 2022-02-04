package com.LosF.pasaleladepago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Tienda extends AppCompatActivity {
    ImageButton b1, b2, b3, b4;

    public RecyclerView ViewProductos;
    private DatabaseReference ProductosRef;
    RecyclerView.LayoutManager LayoutManager;
    List<Productos> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);
        ProductosRef= FirebaseDatabase.getInstance().getReference().child("Productos");
        //init();
        b1 = findViewById(R.id.btnComprar);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B1();
            }
        });
        b2 = findViewById(R.id.btnComprar2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B2();
            }
        });
        b3 = findViewById(R.id.btnComprar3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B3();
            }
        });
        b4 = findViewById(R.id.btnComprar4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B4();
            }
        });
    }
    private void B1(){
        String a[] = new String[2];
        a[0] = "Hombre Araña";
        a[1] = "uno";
        startActivity(new Intent(Tienda.this,CheckoutActivity.class).putExtra("carta",a));
    }
    private void B2(){
        String a[] = new String[2];
        a[0] = "Batman";
        a[1] = "dos";
        startActivity(new Intent(Tienda.this,CheckoutActivity.class).putExtra("carta",a));
    }
    private void B3(){
        String a[] = new String[2];
        a[0] = "Elsa";
        a[1] = "tres";
        startActivity(new Intent(Tienda.this,CheckoutActivity.class).putExtra("carta",a));
    }
    private void B4(){
        String a[] = new String[2];
        a[0] = "Venom";
        a[1] = "cuatro";
        startActivity(new Intent(Tienda.this,CheckoutActivity.class).putExtra("carta",a));
    }

}