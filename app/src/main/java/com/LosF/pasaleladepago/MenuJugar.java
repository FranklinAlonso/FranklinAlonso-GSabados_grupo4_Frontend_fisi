package com.LosF.pasaleladepago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuJugar extends AppCompatActivity {
    Button PVP,PVE;
    ImageButton Volver;
    String id_;
    Codigo C;
    int sumador_id;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_jugar);
        Volver = findViewById(R.id.Volver);
        Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Volver();
            }
        });
        PVP = findViewById(R.id.PVP);
        PVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PVP();
            }
        });
        PVE = findViewById(R.id.PVE);
        PVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PVE();
            }
        });
    }
    private void Volver(){
        startActivity(new Intent(MenuJugar.this,Menu.class));
    }

    private void PVP(){
        crearPartida();
    }

    private void crearPartida(){
        Query colaPartidasSolas = mDatabase.child("Partida").orderByChild("numJugadores").equalTo("1").limitToFirst(1);
        colaPartidasSolas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){ //En caso ya exista partida libres jsjs
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        String id_Part = snapshot1.getKey();
                        mDatabase.child("Partida").child(id_Part).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Partida P = snapshot.getValue(Partida.class);
                                Player player2 = new Player(2,P.id_Partida);
                                P.Jugadores.add(player2);
                                P.setNumJugadores("2");
                                P.repatirCartas();
                                P.Turno = "1";
                                mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                                String[] arregloString = new String[2];
                                arregloString[0] = P.id_Partida;
                                arregloString[1] = "1";
                                startActivity(new Intent(MenuJugar.this,SalaEspera.class).putExtra("llave",arregloString));
                                //Se reparte las cartas -> P1 y P2 ya van a tener cartas en su mano
                                //El Player2 se van a Sala de espera
                                //startActivity(new Intent(MenuJugar.this,PartidaPVP.class).putExtra("llave",arregloString));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }else{ //En caso no exista partidas libres jsjs
                    mDatabase.child("Contador").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            id_ = snapshot.getValue(String.class); //Recupero dato de BD
                            //Creo Partida
                            Player player1 = new Player(1,id_);
                            Partida P = new Partida(id_,player1);
                            //Operaciones con los demas atributos de partida
                            Mazo M = new Mazo();
                            Mesa MU = new Mesa();
                            P.MesaUnica = MU;
                            M.Barajar();
                            P.MazoUnico=M;
                            P.cartasAperro();
                            mDatabase.child("Partida").child(id_).setValue(P);
                            //Sumo +1 al contador en BD
                            sumador_id = Integer.parseInt(id_);
                            sumador_id = sumador_id + 1;
                            id_ = String.valueOf(sumador_id);
                            mDatabase.child("Contador").setValue(id_); //Guardo nuevo dato en BD
                            String[] arregloString = new String[2];
                            arregloString[0] = P.id_Partida;
                            arregloString[1] = "0";
                            startActivity(new Intent(MenuJugar.this,SalaEspera.class).putExtra("llave",arregloString));
                            //Player1 se va a Sala de espera
                            //startActivity(new Intent(MenuJugar.this,PartidaPVP.class).putExtra("llave",arregloString));
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    private void PVE(){
        startActivity(new Intent(MenuJugar.this,DificultadesPVE.class));
    }
}