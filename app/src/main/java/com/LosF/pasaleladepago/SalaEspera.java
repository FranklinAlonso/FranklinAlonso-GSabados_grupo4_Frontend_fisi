package com.LosF.pasaleladepago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SalaEspera extends AppCompatActivity {
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_espera);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String[] codigo = getIntent().getStringArrayExtra("llave");
        Query colaPartida = mDatabase.child("Partida").orderByChild("id_Partida").equalTo(codigo[0]).limitToFirst(1);
        colaPartida.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String id_Part = snapshot1.getKey();
                    mDatabase.child("Partida").child(id_Part).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Partida P = snapshot.getValue(Partida.class);
                            if (P.numJugadores.equals("2")) {
                                startActivity(new Intent(SalaEspera.this, PartidaPVP.class).putExtra("llave", codigo));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}