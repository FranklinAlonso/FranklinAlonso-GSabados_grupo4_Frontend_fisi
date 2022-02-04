package com.LosF.pasaleladepago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PartidaPVP extends AppCompatActivity {
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida_pvp);
        String[] codigo = getIntent().getStringArrayExtra("llave");
        Query colaPartida = mDatabase.child("Partida").orderByChild("id_Partida").equalTo(codigo[0]).limitToFirst(1); //ACA ESTOY BUSCANDO Y GUARDANDO LA PARTIDA QUE TENGA LA MISMA "id_partida" CON EL codigo
        colaPartida.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        String id_Part = snapshot1.getKey(); //Aca ya tengo la partida especifica
                        mDatabase.child("Partida").child(id_Part).addListenerForSingleValueEvent(new ValueEventListener() { // Aca voy a retornar el objeto Partida
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                Partida P = snapshot.getValue(Partida.class); //Aca ya tengo mi objeto Partida
                                if (P.Jugadores.get(0).multiplicador == 0 && P.Jugadores.get(1).multiplicador == 0){
                                    if (codigo[1].equals("0")) {
                                        Intent i = new Intent(getApplicationContext(), Contrato.class).putExtra("llave", codigo);
                                        startActivity(i);
                                    } else {
                                        Intent i = new Intent(getApplicationContext(), EsperaContrato.class).putExtra("llave", codigo);
                                        startActivity(i);
                                    }
                                }else{
                                    //Se arma LOS MAZOS VISUALMENTE
                                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7, R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                                    for (int i = 0; i < P.Jugadores.get(Integer.parseInt(codigo[1])).mazoplayer.size(); i++) {
                                        setCartaVisual(P.Jugadores.get(Integer.parseInt(codigo[1])).mazoplayer.get(i), id[i]);
                                    }

                                    mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            mostrarMesa(P);
                                            ImageView imagenTurno = findViewById(R.id.turno);
                                            TextView txtGanado = findViewById(R.id.CartasGanadas);
                                            ImageView NoTurno = findViewById(R.id.espera);
                                            imagenTurno.setVisibility(View.INVISIBLE);
                                            //NoTurno.setVisibility(View.INVISIBLE);
                                            NoTurno.setVisibility(View.VISIBLE);
                                            //txtGanado.setText("Cartas Ganadas: " + P.Jugadores.get(Integer.parseInt(codigo[1])).mazoGanada.ganado.size());
                                            if(P.Jugadores.get(Integer.parseInt(codigo[1])).mazoGanada == null){
                                                txtGanado.setText("Cartas Ganadas: 0");
                                            }else{
                                                txtGanado.setText("Cartas Ganadas: " + P.Jugadores.get(Integer.parseInt(codigo[1])).mazoGanada.ganado.size());
                                            }
                                            if(P.Turno.equals("1") && codigo[1].equals("0")){
                                                imagenTurno.setVisibility(View.VISIBLE);
                                                NoTurno.setVisibility(View.INVISIBLE);
                                                selecYPonerCartaJugador(P,codigo[1]);
                                                if(P.Jugadores.get(0).tiroCarta.equals("S") && P.Repite.equals("2")) {
                                                    P.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                                                    mostrarMesa(P); //Muestro la carta del jugador 1
                                                    P.Jugadores.get(0).tiroCarta="N";
                                                    P.Repite = "1";
                                                }else if(P.Jugadores.get(0).tiroCarta.equals("S") && P.Repite.equals("1")){
                                                    P.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                                                    mostrarMesa(P); //Muestro la carta del jugador 1
                                                    P.Jugadores.get(0).tiroCarta="N";
                                                    P.Repite = "2";
                                                    /*if(P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada == null) {
                                                        P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                                        P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                        P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                    }else{
                                                        P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                        P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                    }*/
                                                    if(P.MesaUnica.comparando("1") == 0 || P.MesaUnica.comparando("1")  == 1){
                                                        if (P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada == null) {
                                                            P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                                            P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                            P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                        } else {
                                                            P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                            P.Jugadores.get(P.MesaUnica.comparando("1")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                        }
                                                    }else if(P.MesaUnica.comparando("1") == 2 || P.MesaUnica.comparando("2") == 3){
                                                        if (P.Jugadores.get(P.MesaUnica.comparando("1")-2).mazoGanada == null) {
                                                            P.Jugadores.get(P.MesaUnica.comparando("1")-2).mazoGanada = new MazoGanada();
                                                            P.Jugadores.get(1).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                            P.Jugadores.get(0).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                        } else {
                                                            P.Jugadores.get(1).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                            P.Jugadores.get(0).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                        }
                                                    }
                                                    //Ya esta hecho alv
                                                    P.MesaUnica.Tirada.remove(0);
                                                    P.MesaUnica.Tirada.remove(0);
                                                }
                                            }else if(P.Turno.equals("2") && codigo[1].equals("1")){
                                                //mostrarMesa(P);
                                                imagenTurno.setVisibility(View.VISIBLE);
                                                NoTurno.setVisibility(View.INVISIBLE);
                                                selecYPonerCartaJugador(P,codigo[1]);
                                                mostrarMesa(P);
                                                if(P.Jugadores.get(1).tiroCarta.equals("S") && P.Repite.equals("1")) {
                                                    P.Turno = "2"; //Jugador 2 tiró la carta y le da el turno al Jugador1
                                                    mostrarMesa(P);
                                                    P.Jugadores.get(1).tiroCarta="N";
                                                    P.Repite="2";
                                                    /*if(P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada == null) {
                                                        P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                                        P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                        P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                    }else{
                                                        P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                        P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                    }*/
                                                    if(P.MesaUnica.comparando("2") == 0 || P.MesaUnica.comparando("2")  == 1){
                                                        if (P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada == null) {
                                                            P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                                            P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                            P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                        } else {
                                                            P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                            P.Jugadores.get(P.MesaUnica.comparando("2")).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                        }
                                                    }else if(P.MesaUnica.comparando("2") == 2 || P.MesaUnica.comparando("2") == 3){
                                                        if (P.Jugadores.get(P.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                                            P.Jugadores.get(P.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                                            P.Jugadores.get(0).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                            P.Jugadores.get(1).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                        } else {
                                                            P.Jugadores.get(0).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(0));
                                                            P.Jugadores.get(1).mazoGanada.ganado.add(P.MesaUnica.Tirada.get(1));
                                                        }
                                                    }
                                                    //Ya esta hecho alv
                                                    P.MesaUnica.Tirada.remove(0);
                                                    P.MesaUnica.Tirada.remove(0);
                                                }else if(P.Jugadores.get(1).tiroCarta.equals("S") && P.Repite.equals("2")){
                                                    P.Turno = "1"; //Jugador 2 tiró la carta y le da el turno al Jugador1
                                                    mostrarMesa(P);
                                                    P.Jugadores.get(1).tiroCarta="N";
                                                    P.Repite="1";
                                                }
                                            }
                                            mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                                            if(P.Jugadores.get(0).mazoplayer.size() == 0 && P.Jugadores.get(1).mazoplayer.size() == 0){
                                                mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                                                comprobarFin(P.Jugadores.get(0).mazoGanada,P.Jugadores.get(1).mazoGanada);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    //resetPosition();
                                    //esconderPerro();
                                    //--------------------------------------------------/
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull  DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void setCartaVisual(Carta cardPlay, int id){
        ImageView cardPos=findViewById(id);

        cardPos.setImageResource(idCartaSeleccionada(cardPlay));
    }

    public int idCartaSeleccionada(Carta carta){
        int result=0;
        if(carta.simbolo.equals("A")){
            switch(carta.numero){
                case 0:{result=R.drawable.arc00; break;}
                case 1:{result= R.drawable.arc01; break;}
                case 2:{result= R.drawable.arc02; break;}
                case 3:{result= R.drawable.arc03; break;}
                case 4:{result= R.drawable.arc04; break;}
                case 5:{result= R.drawable.arc05; break;}
                case 6:{result= R.drawable.arc06; break;}
                case 7:{result= R.drawable.arc07; break;}
                case 8:{result= R.drawable.arc08; break;}
                case 9:{result= R.drawable.arc09; break;}
                case 10:{result= R.drawable.arc10; break;}
                case 11:{result= R.drawable.arc11; break;}
                case 12:{result= R.drawable.arc12; break;}
                case 13:{result= R.drawable.arc13; break;}
                case 14:{result= R.drawable.arc14; break;}
                case 15:{result= R.drawable.arc15; break;}
                case 16:{result= R.drawable.arc16; break;}
                case 17:{result= R.drawable.arc17; break;}
                case 18:{result= R.drawable.arc18; break;}
                case 19:{result= R.drawable.arc19; break;}
                case 20:{result= R.drawable.arc20; break;}
                case 21:{result= R.drawable.arc21; break;}
            }
        }else if(carta.simbolo.equals("T")){
            switch (carta.numero){
                case 1:{result= R.drawable.coins01; break;}
                case 2:{result= R.drawable.coins02; break;}
                case 3:{result= R.drawable.coins03; break;}
                case 4:{result= R.drawable.coins04; break;}
                case 5:{result= R.drawable.coins05; break;}
                case 6:{result= R.drawable.coins06; break;}
                case 7:{result= R.drawable.coins07; break;}
                case 8:{result= R.drawable.coins08; break;}
                case 9:{result= R.drawable.coins09; break;}
                case 10:{result= R.drawable.coins10; break;}
            }
        }else if(carta.simbolo.equals("C")){
            switch (carta.numero){
                case 1:{result= R.drawable.cups01; break;}
                case 2:{result= R.drawable.cups02; break;}
                case 3:{result= R.drawable.cups03; break;}
                case 4:{result= R.drawable.cups04; break;}
                case 5:{result= R.drawable.cups05; break;}
                case 6:{result= R.drawable.cups06; break;}
                case 7:{result= R.drawable.cups07; break;}
                case 8:{result= R.drawable.cups08; break;}
                case 9:{result= R.drawable.cups09; break;}
                case 10:{result= R.drawable.cups10; break;}
            }
        }else if(carta.simbolo.equals("E")){
            switch (carta.numero){
                case 1:{result= R.drawable.swords01; break;}
                case 2:{result= R.drawable.swords02; break;}
                case 3:{result= R.drawable.swords03; break;}
                case 4:{result= R.drawable.swords04; break;}
                case 5:{result= R.drawable.swords05; break;}
                case 6:{result= R.drawable.swords06; break;}
                case 7:{result= R.drawable.swords07; break;}
                case 8:{result= R.drawable.swords08; break;}
                case 9:{result= R.drawable.swords09; break;}
                case 10:{result= R.drawable.swords10; break;}
            }
        }else{
            switch (carta.numero){
                case 1:{result= R.drawable.wands01; break;}
                case 2:{result= R.drawable.wands02; break;}
                case 3:{result= R.drawable.wands03; break;}
                case 4:{result= R.drawable.wands04; break;}
                case 5:{result= R.drawable.wands05; break;}
                case 6:{result= R.drawable.wands06; break;}
                case 7:{result= R.drawable.wands07; break;}
                case 8:{result= R.drawable.wands08; break;}
                case 9:{result= R.drawable.wands09; break;}
                case 10:{result= R.drawable.wands10; break;}
            }
        }

        return result;
    }


    public void CartaP03(Carta a){
        ImageView lol = null;
        lol = findViewById(R.id.cardStageP03);
        lol.setImageResource(idCartaSeleccionada(a));
    }
    public void CartaP04(Carta a){
        ImageView lol = null;
        lol = findViewById(R.id.cardStageP04);
        lol.setImageResource(idCartaSeleccionada(a));
    }

    public void mostrarMesa(Partida P){
        ImageView stage,aux;
        if(/*P.Jugadores.get(0).tiroCarta.equals("S")*/P.MesaUnica.Tirada.size() == 1){
            //aux = retornarCartaIV(Integer.parseInt(P.Jugadores.get(0).cartaMesa));
            //stage=findViewById(R.id.cardStageP03);
            //stage.setImageDrawable(aux.getDrawable());
            //stage.setImageDrawable(salvacion(P.MesaUnica.Tirada.get(0)));
            CartaP03(P.MesaUnica.Tirada.get(0));
        }else if(/*P.Jugadores.get(1).tiroCarta.equals("S")*/P.MesaUnica.Tirada.size() == 2){
            //aux = retornarCartaIV(Integer.parseInt(P.Jugadores.get(0).cartaMesa));
            //stage=findViewById(R.id.cardStageP03);
            // stage.setImageDrawable(salvacion(P.MesaUnica.Tirada.get(0)));
            // stage.setImageDrawable(aux.getDrawable());
            //aux = retornarCartaIV(Integer.parseInt(P.Jugadores.get(1).cartaMesa));
            //=findViewById(R.id.cardStageP04);
            // stage.setImageDrawable(salvacion(P.MesaUnica.Tirada.get(1)));
            //stage.setImageDrawable(aux.getDrawable());
            //P.Jugadores.get(0).mazoplayer.remove(Integer.parseInt(P.Jugadores.get(0).cartaMesa)-1);
            //P.Jugadores.get(1).mazoplayer.remove(Integer.parseInt(P.Jugadores.get(1).cartaMesa)-1);
            CartaP03(P.MesaUnica.Tirada.get(0));
            CartaP04(P.MesaUnica.Tirada.get(1));
        }


    }

    public ImageView retornarCartaIV(int num) {
        ImageView aux = null;
        switch (num){
            case 1: aux = findViewById(R.id.card1); break;
            case 2: aux = findViewById(R.id.card2); break;
            case 3: aux = findViewById(R.id.card3); break;
            case 4: aux = findViewById(R.id.card4); break;
            case 5: aux = findViewById(R.id.card5); break;
            case 6: aux = findViewById(R.id.card6); break;
            case 7: aux = findViewById(R.id.card7); break;
            case 8: aux = findViewById(R.id.card8); break;
            case 9: aux = findViewById(R.id.card9); break;
            case 10: aux = findViewById(R.id.card10); break;
            case 11: aux = findViewById(R.id.card11); break;
            case 12: aux = findViewById(R.id.card12); break;
            case 13: aux = findViewById(R.id.card13); break;
        }
        return aux;
    }

    //Pone la carta seleccionada en el campo
    public void selecYPonerCartaJugador(Partida P, String codigoJugador){
        ImageView stage, card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13;

        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.card3);
        card4=findViewById(R.id.card4);
        card5=findViewById(R.id.card5);
        card6=findViewById(R.id.card6);
        card7=findViewById(R.id.card7);
        card8=findViewById(R.id.card8);
        card9=findViewById(R.id.card9);
        card10=findViewById(R.id.card10);
        card11=findViewById(R.id.card11);
        card12=findViewById(R.id.card12);
        card13=findViewById(R.id.card13);

        //stage=findViewById(R.id.cardStageP03);
        /*if(jugador1){
            stage=findViewById(R.id.cardStageP03);
        }else{
            stage=findViewById(R.id.cardStageP04);
        }*/
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(0));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "1";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(0);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card1.getDrawable());
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(1)); //Aqui guardo la carta
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "2";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(1);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card2.getDrawable());
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(2));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "3";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(2);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                //  stage.setImageDrawable(card3.getDrawable());
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(3));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "4";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(3);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card4.getDrawable());
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(4));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "5";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(4);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card5.getDrawable());
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(5));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "6";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(5);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card6.getDrawable());
            }
        });

        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(6));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "7";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(6);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card7.getDrawable());
            }
        });

        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(7));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "8";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(7);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card8.getDrawable());
            }
        });

        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(8));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "9";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(8);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card9.getDrawable());
            }
        });

        card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(9));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "10";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(9);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card10.getDrawable());
            }
        });

        card11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(10));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "11";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(10);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                // stage.setImageDrawable(card11.getDrawable());
            }
        });

        card12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(11));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "12";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(11);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                //  stage.setImageDrawable(card12.getDrawable());
            }
        });

        card13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Partida").child(P.getId_Partida()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Partida P = snapshot.getValue(Partida.class);
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(12));
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "13";
                        P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(12);
                        mDatabase.child("Partida").child(P.getId_Partida()).setValue(P);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.setVisibility(View.INVISIBLE);
                //  stage.setImageDrawable(card13.getDrawable());
            }
        });
    }
/*
    public void resetPosition(){
        int [] id={R.id.cardStageP03, R.id.cardStageP04};
        ImageView iv;
        for(int i=0;i<id.length;i++){
            iv=findViewById(id[i]);
            iv.setImageResource(R.drawable.blank);
        }
    }
*/
    //Esconder slots extras
   /* public void esconderPerro(){
        int [] id={R.id.cardStageP01, R.id.cardStageP02, R.id.cardStageP05, R.id.cardStageP06};
        ImageView iv;
        for(int i=0;i<id.length;i++){
            iv=findViewById(id[i]);
            iv.setVisibility(View.INVISIBLE);
        }
    }*/

    public void comprobarFin(MazoGanada player1, MazoGanada player2){
        int p1=0, p2=0;
        String ganador;
        if(player1.ganado.size()+player2.ganado.size()==32){
            p1=player1.SumarPuntaje();
            p2= player2.SumarPuntaje();

            /*setContentView(R.layout.activity_popup);
            tv=findViewById(R.id.texto);*/
            if(p1>p2){
                //tv.setText("Gana el player 1");
                ganador = "Gana el player 1!";
                startActivity(new Intent(PartidaPVP.this,muestraGanador.class).putExtra("ganador",ganador));
            }else if (p1==p2){
                ganador = "Empate!";
                startActivity(new Intent(PartidaPVP.this,muestraGanador.class).putExtra("ganador",ganador));
                //tv.setText("Empate");
            }else{
                ganador = "Gana el player 2!";
                startActivity(new Intent(PartidaPVP.this,muestraGanador.class).putExtra("ganador",ganador));
                //tv.setText("Gana el player 2");
            }
        }
    }
}