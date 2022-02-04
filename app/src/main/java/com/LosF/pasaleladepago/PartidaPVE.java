package com.LosF.pasaleladepago;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PartidaPVE extends AppCompatActivity {
    Player player = new Player(1, "partidaPVE");
    Partida partidaPVE = new Partida("partidaPVE", player);
    Player Siri = new Player(2,"partidaPVE");
    Mazo M = new Mazo();
    Mesa MU = new Mesa();
    String[] ingresoDatos = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida_pve);
        ImageView imagenTurno = findViewById(R.id.turno);
        ImageView NoTurno = findViewById(R.id.espera);
        ImageView siguiente = findViewById(R.id.SiguienteRonda);
        ingresoDatos = getIntent().getStringArrayExtra("llave"); //0 - dificultad / 1 - contrato
        //Debe salir para escoger contrato al player

        //Se coloca las cartas a Maquina y Player
        if(partidaPVE.partidaEmpezada.equals("0")) {
            partidaPVE.Jugadores.add(Siri);
            partidaPVE.setNumJugadores("2");
            partidaPVE.MesaUnica = MU;
            M.Barajar();
            partidaPVE.MazoUnico = M;
            partidaPVE.cartasAperro();
            partidaPVE.repatirCartas();
            partidaPVE.Turno = "1";
            if (ingresoDatos[1].equals("Con")) {
                partidaPVE.Jugadores.get(1).setMultiplicador(2);
                partidaPVE.Jugadores.get(0).setMultiplicador(1);
                partidaPVE.llevarPerro(0);
                partidaPVE.Jugadores.get(1).mazoGanada = new MazoGanada();
            } else {
                partidaPVE.Jugadores.get(0).setMultiplicador(3);
                partidaPVE.Jugadores.get(1).setMultiplicador(1);
                partidaPVE.Jugadores.get(0).mazoGanada = new MazoGanada();
                partidaPVE.llevarPerro(1);
            }
            partidaPVE.partidaEmpezada = "1";
        }
        int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
        for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
            setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
        }
        mostrarMesa(partidaPVE);
        imagenTurno.setVisibility(View.VISIBLE);
        NoTurno.setVisibility(View.INVISIBLE);
        siguiente.setVisibility(View.INVISIBLE);
        TextView txtGanado = findViewById(R.id.CartasGanadas);
        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
        selecYPonerCartaJugador(partidaPVE, "0");
    }

    public void cartasEnemigas(){
        int[] id = {R.id.cardEnemy1, R.id.cardEnemy2, R.id.cardEnemy3, R.id.cardEnemy4, R.id.cardEnemy5,
                R.id.cardEnemy6, R.id.cardEnemy7, R.id.cardEnemy8, R.id.cardEnemy9, R.id.cardEnemy10,
                R.id.cardEnemy11, R.id.cardEnemy12, R.id.cardEnemy13};
        for (int i = 12; i > partidaPVE.Jugadores.get(1).mazoplayer.size(); i--) {
            ImageView siguiente = findViewById(id[i]);
            siguiente.setVisibility(View.INVISIBLE);
        }
    }

    public void selecYPonerCartaJugador(Partida P, String codigoJugador){
        ImageView card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13, siguiente;
        ImageView imagenTurno = findViewById(R.id.turno);
        ImageView NoTurno = findViewById(R.id.espera);
        TextView textoGanada = findViewById(R.id.CartasGanadas);

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
        siguiente=findViewById(R.id.SiguienteRonda);

        //A duda
        ImageView refereeR=findViewById(R.id.refereeRight);
        ImageView refereeL=findViewById(R.id.refereeLeft);



        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A DUDA
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                int Posicion=2;
                ImageView cartaMesa1, cartaMesa2;
                cartaMesa1 = findViewById(R.id.cardStageP03);
                cartaMesa2 = findViewById(R.id.cardStageP04);
                cartaMesa1.setImageResource(R.drawable.blank);
                cartaMesa2.setImageResource(R.drawable.blank);

                P.MesaUnica.Tirada.remove(0);
                P.MesaUnica.Tirada.remove(0);

                P.Turno = "2";
                partidaPVE = P;
                Siri = P.Jugadores.get(1);
                textoGanada.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                if (partidaPVE.Jugadores.get(0).mazoGanada == null) {
                    textoGanada.setText("Cartas Ganadas: 0");
                }


                if(P.Jugadores.get(0).mazoplayer.size() == 0 && P.Jugadores.get(1).mazoplayer.size() == 0){
                    comprobarFin(P.Jugadores.get(0).mazoGanada,P.Jugadores.get(1).mazoGanada);
                    P.partidaEmpezada = "0";
                }

                if(P.partidaEmpezada.equals("1")) {
                    if(ingresoDatos[0].equals("Facil")){
                        Posicion = MetodoFacil();
                    }else if(ingresoDatos[0].equals("Normal")){
                        Posicion = MetodoNormal();
                    }else if(ingresoDatos[0].equals("Dificil")){
                        Posicion = MetodoInteligentePrimero();
                    }

                    P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                    System.out.println("Comenzo Siri");

                    imprimir_jugada_realizada_2(P); //A qui se imprime la jugada que se realizo
                    P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                    P.Jugadores.get(1).mazoplayer.remove(Posicion);
                    cartasEnemigas();
                    mostrarMesa(P);
                    partidaPVE = P;
                    Siri = P.Jugadores.get(1);
                    P.Turno = "1";
                    partidaPVE = P;
                    Siri = P.Jugadores.get(1);
                    System.out.println("llegooooooooooooooo-----");
                    imagenTurno.setVisibility(View.VISIBLE);
                    NoTurno.setVisibility(View.INVISIBLE);
                }
                v.setVisibility(View.INVISIBLE);
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);

                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(0));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "1";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(0);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    //A DUDA


                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";
                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }



                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(1));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "2";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(1);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(2));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "3";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(2);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(3));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "4";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(3);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(4));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "5";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(4);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(5));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "6";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(5);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(6));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "7";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(6);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(7));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "8";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(7);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(8));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "9";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(8);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));

                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(9));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "10";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(9);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(10));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "11";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(10);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(11));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "12";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(11);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");
                        cartasEnemigas();
                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        card13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Posicion = -2;
                refereeL.setVisibility(View.INVISIBLE);
                refereeR.setVisibility(View.INVISIBLE);
                if(partidaPVE.Turno.equals("1")) {
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).tiroCarta = "S";
                    P.MesaUnica.Tirada.add(P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.get(12));
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).cartaMesa = "13";
                    P.Jugadores.get(Integer.parseInt(codigoJugador)).mazoplayer.remove(12);
                    partidaPVE = P;
                    //v.setVisibility(View.INVISIBLE);
                    ImageView blanco;
                    int[] id = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5, R.id.card6, R.id.card7,
                            R.id.card8, R.id.card9, R.id.card10, R.id.card11, R.id.card12, R.id.card13};
                    for (int i = 0; i < 13; i++) {
                        blanco = findViewById(id[i]);
                        blanco.setImageResource(R.drawable.blank);
                        blanco.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < partidaPVE.Jugadores.get(0).mazoplayer.size(); i++) {
                        setCartaVisual(partidaPVE.Jugadores.get(0).mazoplayer.get(i), id[i]);
                        // System.out.println(partidaPVE.Jugadores.get(0).mazoplayer.size());
                    }
                    if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("2")) {
                        partidaPVE.Turno = "2"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1

                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "1";
                        imagenTurno.setVisibility(View.INVISIBLE);
                        NoTurno.setVisibility(View.VISIBLE);
                    } else if (partidaPVE.Jugadores.get(0).tiroCarta.equals("S") && partidaPVE.Repite.equals("1")) {
                        partidaPVE.Turno = "1"; //Jugador 1 jugó y se le cambia el turno
                        mostrarMesa(partidaPVE); //Muestro la carta del jugador 1
                        partidaPVE.Jugadores.get(0).tiroCarta = "N";
                        partidaPVE.Repite = "2";

                        if(partidaPVE.MesaUnica.comparando("1") == 0 || partidaPVE.MesaUnica.comparando("1")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("1")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("1")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("1") == 2 || partidaPVE.MesaUnica.comparando("1") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        ImageView cartaMesa1, cartaMesa2;
                        cartaMesa1 = findViewById(R.id.cardStageP03);
                        cartaMesa2 = findViewById(R.id.cardStageP04);
                        cartaMesa1.setImageResource(R.drawable.blank);
                        cartaMesa2.setImageResource(R.drawable.blank);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        partidaPVE.MesaUnica.Tirada.remove(0);
                        //SystemClock.sleep(3000);

                    }
                    //Aqui juega la maquina------------------------------------------------------------------>

                    if(P.Turno.equals("2")){
                        if(ingresoDatos[0].equals("Facil")){
                            Posicion = MetodoFacil();
                        }else if(ingresoDatos[0].equals("Normal")){
                            Posicion = MetodoInteligenteSegundo();//falta modificar
                        }else if(ingresoDatos[0].equals("Dificil")){
                            Posicion = MetodoInteligenteSegundo();
                        }
                        System.out.println("------------>Mesa Antes de que tire SIRI: " + P.MesaUnica.Tirada.get(0).simbolo + "; " + P.MesaUnica.Tirada.get(0).numero);
                        for(int i = 0; i < P.Jugadores.get(1).mazoplayer.size();i++){
                            System.out.println(P.Jugadores.get(1).mazoplayer.get(i).simbolo + P.Jugadores.get(1).mazoplayer.get(i).numero);
                        }
                        System.out.println("----> POSICION ESCOGIDA ES: " + Posicion);
                        P.Jugadores.get(1).tiroCarta = "S";
                        P.MesaUnica.Tirada.add(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion));
                        System.out.println("Comenzo el jugador");

                        imprimir_jugada_realizada(P); //A qui se imprime la jugada que se realizo
                        P.Jugadores.get(1).cartaMesa = String.valueOf(partidaPVE.Jugadores.get(1).mazoplayer.get(Posicion).numero);
                        P.Jugadores.get(1).mazoplayer.remove(Posicion);
                        cartasEnemigas();
                        mostrarMesa(P);
                        partidaPVE = P;
                        Siri = P.Jugadores.get(1);
                        //Espera que se compare y luego se mues

                        if(partidaPVE.MesaUnica.comparando("2") == 0 || partidaPVE.MesaUnica.comparando("2")  == 1){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            } else {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                                if(partidaPVE.MesaUnica.comparando("2")==0){
                                    refereeL.setVisibility(View.VISIBLE);
                                }else{
                                    refereeR.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(partidaPVE.MesaUnica.comparando("2") == 2 || partidaPVE.MesaUnica.comparando("2") == 3){
                            if (partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada == null) {
                                partidaPVE.Jugadores.get(partidaPVE.MesaUnica.comparando("2")-2).mazoGanada = new MazoGanada();
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            } else {
                                partidaPVE.Jugadores.get(0).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(0));
                                partidaPVE.Jugadores.get(1).mazoGanada.ganado.add(partidaPVE.MesaUnica.Tirada.get(1));
                            }
                        }
                        TextView txtGanado = findViewById(R.id.CartasGanadas);
                        txtGanado.setText("Cartas Ganadas: " + partidaPVE.Jugadores.get(0).mazoGanada.ganado.size());
                        mostrarMesa(partidaPVE);
                        siguiente.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void setCartaVisual(Carta cardPlay, int id){
        ImageView cardPos=findViewById(id);
        cardPos.setImageResource(idCartaSeleccionada(cardPlay));
        cardPos.setVisibility(View.VISIBLE);
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

    public void imprimir_jugada_realizada(Partida P){
        //Se imprime las cartas de la mesa
        System.out.println("--------------------------------");
        System.out.println("Cartas en la mesa");
        System.out.println("Se tienen en 0: "+P.MesaUnica.Tirada.get(0).simbolo+"; "+P.MesaUnica.Tirada.get(0).numero);
        System.out.println("Se tienen en 1: "+P.MesaUnica.Tirada.get(1).simbolo+"; "+P.MesaUnica.Tirada.get(1).numero);
        System.out.println("--------------------------------");
        System.out.println("El oponente tenia: ");
        for (int i=0; i < P.Jugadores.get(1).mazoplayer.size(); i++){
            System.out.println("Carta "+i+": "+P.Jugadores.get(1).mazoplayer.get(i).simbolo+"; "+P.Jugadores.get(1).mazoplayer.get(i).numero);
        }
        System.out.println("--------------------------------");
    }

    public void imprimir_jugada_realizada_2(Partida P){
        //Se imprime las cartas de la mesa
        System.out.println("--------------------------------");
        System.out.println("Cartas en la mesa");
        System.out.println("Se tienen en 0: "+P.MesaUnica.Tirada.get(0).simbolo+"; "+P.MesaUnica.Tirada.get(0).numero);
        System.out.println("--------------------------------");
        System.out.println("El oponente tenia: ");
        for (int i=0; i < P.Jugadores.get(1).mazoplayer.size(); i++){
            System.out.println("Carta "+i+": "+P.Jugadores.get(1).mazoplayer.get(i).simbolo+"; "+P.Jugadores.get(1).mazoplayer.get(i).numero);
        }
        System.out.println("--------------------------------");
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
        if(P.MesaUnica.Tirada.size() == 1){
            CartaP03(P.MesaUnica.Tirada.get(0));
        }else if(P.MesaUnica.Tirada.size() == 2){
            CartaP03(P.MesaUnica.Tirada.get(0));
            CartaP04(P.MesaUnica.Tirada.get(1));
        }
    }

    public void comprobarFin(MazoGanada player1, MazoGanada player2){
        int p1=0, p2=0;
        String ganador;
        if(player1.ganado.size()+player2.ganado.size()==32){
            p1=player1.SumarPuntaje();
            p2= player2.SumarPuntaje();

            if(ingresoDatos[1].equals("Sin")){
                p1+=7;
            }else{
                p2+=7;
            }

            System.out.println("El puntaje del primero es: "+p1);
            player1.ImprimrCartas();
            System.out.println("------");
            System.out.println("El puntaje del primero es: "+p2);
            player2.ImprimrCartas();
            /*setContentView(R.layout.activity_popup);
            tv=findViewById(R.id.texto);*/
            if(p1>p2){
                //tv.setText("Gana el player 1");
                ganador = "Gana el player 1!";
                startActivity(new Intent(PartidaPVE.this,MuestraGanadorPVE.class).putExtra("ganador",ganador));
            }else if (p1==p2){
                ganador = "Empate!";
                startActivity(new Intent(PartidaPVE.this,MuestraGanadorPVE.class).putExtra("ganador",ganador));
                //tv.setText("Empate");
            }else{
                ganador = "Gana el player 2!";
                startActivity(new Intent(PartidaPVE.this,MuestraGanadorPVE.class).putExtra("ganador",ganador));
                //tv.setText("Gana el player 2");
            }
        }
    }

    public int MetodoFacil(){
        Random r = new Random();
        int eleccion = r.nextInt(partidaPVE.Jugadores.get(1).mazoplayer.size());
        return eleccion;
    }

    public int MetodoNormal(){
        int[] index = new int[Siri.mazoplayer.size()];
        int[] puntaje = new int[Siri.mazoplayer.size()];
        int PosicionCarta = -1;
        int c = 0;
        if(tieneCarta("A",0) != -1){ //movimiendo de la maquina
            puntaje[c] = 0;
            index[c] = tieneCarta("A",0);
            c++;
        }
        if(tieneCarta("A",21) != -1){
            puntaje[c] = 100;
            index[c] = tieneCarta("A",21);
            c++;
        }
        if(tieneCarta("A",1) != -1){
            puntaje[c] = 1;
            index[c] = tieneCarta("A",1);
            c++;
        }
        if(ArcanaMenorValor() != -1){ //Ese valor Arcana vale para conocoer index de mi mazo
            puntaje[c] = 2;
            index[c] = ArcanaMenorValor();
            c++;
        }
        if(ArcanoMayorValor() != -1 && partidaPVE.Jugadores.get(1).mazoplayer.get(index[c-1]).numero!=partidaPVE.Jugadores.get(1).mazoplayer.get(ArcanoMayorValor()).numero){ //Ese valor Arcana vale para conocoer index de mi mazo
            puntaje[c] = 25;
            index[c] = ArcanoMayorValor();
            c++;
        }
        if(ExisteArcano() != -1){
            puntaje[c] = 10;
            index[c] = ExisteArcano();
            c++;
        }
        if(NormalMayor() != -1){
            puntaje[c] = 50;
            index[c] = NormalMayor();
            c++;
        }
        if(MenorNormal() != -1 && partidaPVE.Jugadores.get(1).mazoplayer.get(index[c-1]).numero!=partidaPVE.Jugadores.get(1).mazoplayer.get(MenorNormal()).numero){
            puntaje[c] = 30;
            index[c] = MenorNormal();
            c++;
        }
        if(ExisteNormal() != -1){
            puntaje[c] = 40;
            index[c] = ExisteNormal();
            c++;
        }
        //Debo obtener quien vale
        PosicionCarta = index[mayorPuntaje(puntaje,c)];

        return PosicionCarta;
    }

    public int MetodoInteligentePrimero(){ //Cuando Siri Comienza
        int[] index = new int[Siri.mazoplayer.size()];
        int[] puntaje = new int[Siri.mazoplayer.size()];
        int PosicionCarta = -1;
        int c = 0;
        if(tieneCarta("A",0) != -1){ //movimiendo de la maquina
            puntaje[c] = -100;
            index[c] = tieneCarta("A",0);
            c++;
        }
        if(tieneCarta("A",21) != -1){
            puntaje[c] = -50;
            index[c] = tieneCarta("A",21);
            c++;
        }
        if(tieneCarta("A",1) != -1){
            puntaje[c] = -100;
            index[c] = tieneCarta("A",1);
            c++;
        }
        if(ArcanaMenorValor() != -1){ //Ese valor Arcana vale para conocoer index de mi mazo
            puntaje[c] = -80;
            index[c] = ArcanaMenorValor();
            c++;
        }
        if(ArcanoMayorValor() != -1 && partidaPVE.Jugadores.get(1).mazoplayer.get(index[c-1]).numero!=partidaPVE.Jugadores.get(1).mazoplayer.get(ArcanoMayorValor()).numero){ //Ese valor Arcana vale para conocoer index de mi mazo
            puntaje[c] = -80;
            index[c] = ArcanoMayorValor();
            c++;
        }
        if(ExisteArcano() != -1){
            puntaje[c] = -80;
            index[c] = ExisteArcano();
            c++;
        }
        if(NormalMayor() != -1){
            puntaje[c] = -50;
            index[c] = NormalMayor();
            c++;
        }
        if(MenorNormal() != -1 && partidaPVE.Jugadores.get(1).mazoplayer.get(index[c-1]).numero!=partidaPVE.Jugadores.get(1).mazoplayer.get(MenorNormal()).numero){
            puntaje[c] = -70;
            index[c] = MenorNormal();
            c++;
        }
        if(ExisteNormal() != -1){
            puntaje[c] = -60;
            index[c] = ExisteNormal();
            c++;
        }
        //Debo obtener quien vale
        PosicionCarta = index[mayorPuntaje(puntaje,c)];

        return PosicionCarta;
    }

    public int MetodoInteligenteSegundo(){ //Cuando Siri juega segundo
        int PosicionCarta = -1;
        if(partidaPVE.MesaUnica.Tirada.get(0).simbolo.equals("A")) {
            if (partidaPVE.MesaUnica.Tirada.get(0).numero == 0 || partidaPVE.MesaUnica.Tirada.get(0).numero == 1
                    || partidaPVE.MesaUnica.Tirada.get(0).numero == 21) {
                if (partidaPVE.MesaUnica.Tirada.get(0).numero == 1) {
                    if (MayorPalo(partidaPVE.MesaUnica.Tirada.get(0)) != -1) {
                        PosicionCarta = MayorPalo(partidaPVE.MesaUnica.Tirada.get(0));
                    } else if (tieneCarta("A", 0) != -1) {
                        PosicionCarta = tieneCarta("A", 0);
                    } else if (menorMazo() != 1) {
                        PosicionCarta = menorMazo();
                    }
                } else if (partidaPVE.MesaUnica.Tirada.get(0).numero == 21) {
                    if (tieneCarta("A", 0) != -1) {
                        PosicionCarta = tieneCarta("A", 0);
                    } else if (menorMazo() != 1) {
                        PosicionCarta = menorMazo();
                    }
                } else if(partidaPVE.MesaUnica.Tirada.get(0).numero == 0) {
                    if(ArcanosExtremos()!=-1){
                        PosicionCarta = ArcanosExtremos();
                    }else if(cartaNumero(10)!= -1){
                        PosicionCarta = cartaNumero(10);
                    }else if(cartaNumero(9)!= -1){
                        PosicionCarta = cartaNumero(9);
                    }else if(cartaNumero(8)!= -1){
                        PosicionCarta = cartaNumero(8);
                    }else if(cartaNumero(7)!= -1){
                        PosicionCarta = cartaNumero(7);
                    }else if(menorMazo()!=-1){
                        PosicionCarta = menorMazo();
                    }else if(menorMazoArcano()!=-1){
                        PosicionCarta = menorMazoArcano();
                    }
                }
            }else{
                if(menorMazoArcanoX1()!=-1){
                    PosicionCarta = menorMazoArcanoX1();
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
        }else{
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 10){
                if(ArcanaMenorValor() != -1){
                    PosicionCarta = ArcanaMenorValor();
                }else if(tieneCarta("A", 1) != -1){
                    PosicionCarta = tieneCarta("A", 1);
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(ArcanoMayorValor() != -1){
                    PosicionCarta = ArcanoMayorValor();
                }else if(ExisteArcano() != -1){
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 9){
                if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10);
                }else if(tieneCarta("A", 1) != -1){
                    PosicionCarta = tieneCarta("A", 1);
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(ArcanaMenorValor() != -1){
                    PosicionCarta = ArcanaMenorValor();
                }else if(ArcanoMayorValor() != -1){
                    PosicionCarta = ArcanoMayorValor();
                }else if(ExisteArcano() != -1){
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 8){
                if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9);
                }else if(tieneCarta("A", 1) != -1){
                    PosicionCarta = tieneCarta("A", 1);
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(ArcanaMenorValor() != -1){
                    PosicionCarta = ArcanaMenorValor();
                }else if(ArcanoMayorValor() != -1){
                    PosicionCarta = ArcanoMayorValor();
                }else if(ExisteArcano() != -1){
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 7){
                if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8);
                }else if(tieneCarta("A", 1) != -1){
                    PosicionCarta = tieneCarta("A", 1);
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(ArcanaMenorValor() != -1){
                    PosicionCarta = ArcanaMenorValor();
                }else if(ArcanoMayorValor() != -1){
                    PosicionCarta = ArcanoMayorValor();
                }else if(ExisteArcano() != -1){
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 6){
                if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7);
                }else if(tieneCarta("A", 1) != -1){
                    PosicionCarta = tieneCarta("A", 1);
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(ArcanaMenorValor() != -1){
                    PosicionCarta = ArcanaMenorValor();
                }else if(ArcanoMayorValor() != -1){
                    PosicionCarta = ArcanoMayorValor();
                }else if(ExisteArcano() != -1){
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 5){
                if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6);
                }else if(tieneCarta("A", 1) != -1){
                    PosicionCarta = tieneCarta("A", 1);
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(ArcanaMenorValor() != -1){
                    PosicionCarta = ArcanaMenorValor();
                }else if(ArcanoMayorValor() != -1){
                    PosicionCarta = ArcanoMayorValor();
                }else if(ExisteArcano() != -1){
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 4){
                if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 5) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 5);
                }else if(tieneCarta("A", 1) != -1){
                    PosicionCarta = tieneCarta("A", 1);
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(ArcanaMenorValor() != -1){
                    PosicionCarta = ArcanaMenorValor();
                }else if(ArcanoMayorValor() != -1){
                    PosicionCarta = ArcanoMayorValor();
                }else if(ExisteArcano() != -1){
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 3){
                if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 5) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 5);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 4) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 4);
                }else if(tieneCarta("A", 1) != -1){
                    PosicionCarta = tieneCarta("A", 1);
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(ArcanaMenorValor() != -1){
                    PosicionCarta = ArcanaMenorValor();
                }else if(ArcanoMayorValor() != -1){
                    PosicionCarta = ArcanoMayorValor();
                }else if(ExisteArcano() != -1){
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 2){
                if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 5) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 5);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 4) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 4);
                }else if(tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 3) != -1){
                    PosicionCarta =  tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 3);
                }else if(tieneCarta("A", 1) != -1){
                    PosicionCarta = tieneCarta("A", 1);
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(ArcanaMenorValor() != -1){
                    PosicionCarta = ArcanaMenorValor();
                }else if(ArcanoMayorValor() != -1){
                    PosicionCarta = ArcanoMayorValor();
                }else if(ExisteArcano() != -1){
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
            if(partidaPVE.MesaUnica.Tirada.get(0).numero == 1) {
                System.out.println("ACA ES IGUAL A 1--------------");
                if (tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10) != -1) {
                    PosicionCarta = tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 10);
                } else if (tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9) != -1) {
                    PosicionCarta = tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 9);
                } else if (tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8) != -1) {
                    PosicionCarta = tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 8);
                } else if (tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7) != -1) {
                    PosicionCarta = tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 7);
                } else if (tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6) != -1) {
                    PosicionCarta = tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 6);
                } else if (tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 5) != -1) {
                    PosicionCarta = tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 5);
                } else if (tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 4) != -1) {
                    PosicionCarta = tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 4);
                } else if (tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 3) != -1) {
                    PosicionCarta = tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 3);
                } else if (tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 2) != -1) {
                    PosicionCarta = tieneCarta(partidaPVE.MesaUnica.Tirada.get(0).simbolo, 2);
                } else if (tieneCarta("A", 1) != -1) {
                    PosicionCarta = tieneCarta("A", 1);
                } else if (ArcanaMenorValor() != -1) {
                    PosicionCarta = ArcanaMenorValor();
                } else if (ArcanoMayorValor() != -1) {
                    PosicionCarta = ArcanoMayorValor();
                } else if (ExisteArcano() != -1) {
                    PosicionCarta = ExisteArcano();
                }else if(tieneCarta("A", 21) != -1){
                    PosicionCarta = tieneCarta("A", 21);
                }else if(tieneCarta("A", 0) != -1){
                    PosicionCarta = tieneCarta("A", 0);
                }else if(menorMazo() != -1){
                    PosicionCarta = menorMazo();
                }
            }
        }
        return PosicionCarta;
    }

    public int cartaNumero(int num){ //TE DEVUELVE una carta del mismo numero parametro pero no sea Arcana
        int index = -1;
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).numero == num && !Siri.mazoplayer.get(i).simbolo.equals("A")){
                index = i;
            }
        }
        return index;
    }
    public int ArcanosExtremos(){
        int index = -1;
        for(int i = 0; i<Siri.mazoplayer.size();i++){
            if(Siri.mazoplayer.get(i).numero == 21 || Siri.mazoplayer.get(i).numero == 1 || Siri.mazoplayer.get(i).numero == 0){
                index = i;
            }
        }
        return index;
    }

    public int menorMazoArcano(){
        int index = -1;
        int menor = 99;
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).numero < menor && Siri.mazoplayer.get(i).simbolo.equals("A")){
                menor = Siri.mazoplayer.get(i).numero;
                index = i;
            }
        }
        return index;
    }

    public int menorMazoArcanoX1(){ //PARA menor de las mayores de Arcanas - X+1 o X+n
        int index = -1;
        int menor = 69;
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).numero < menor && Siri.mazoplayer.get(i).simbolo.equals("A")
                    && Siri.mazoplayer.get(i).numero > partidaPVE.MesaUnica.Tirada.get(0).numero){
                menor = Siri.mazoplayer.get(i).numero;
                index = i;
            }
        }
        return index;
    }

    public int menorMazo(){ //Te da el menor de las cartas NORMALES sin importar el palo
        int index = -1;
        int menor = 99;
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).numero < menor && !Siri.mazoplayer.get(i).simbolo.equals("A")){
                menor = Siri.mazoplayer.get(i).numero;
                index = i;
            }
        }
        return index;
    }
    public int MayorPalo(Carta C){ //aca agarro la menor de las mayores, o sea X + 1
        int index = -1;
        int menor = 11;
        for(int i = 0; i<Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).simbolo.equals(C.simbolo) && Siri.mazoplayer.get(i).numero > C.numero){
                if(Siri.mazoplayer.get(i).numero < menor){
                    menor = Siri.mazoplayer.get(i).numero;
                    index = i;
                }
            }
        }
        return index;
    }



    public int tieneCarta(String simbolo, int numero){ //Retorna una carta especifica
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).simbolo.equals(simbolo) && Siri.mazoplayer.get(i).numero == numero){
                return i;
            }
        }
        return -1;
    }

    public int ArcanaMenorValor(){ //Devuelve Carta Arcana menor no extremo
        int Index = -1;
        int menor = 22;
        //System.out.println("Aquixxxx:" + Siri.mazoplayer.size());
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).simbolo.equals("A")){
                if(Siri.mazoplayer.get(i).numero < menor && Siri.mazoplayer.get(i).numero != 0 && Siri.mazoplayer.get(i).numero != 1
                        && Siri.mazoplayer.get(i).numero != 21){
                    menor = Siri.mazoplayer.get(i).numero;
                    Index = i;

                }
            }
        }
        return Index;
    }

    public int ArcanoMayorValor(){ //DA UNA CARTA MAYOR NO SEA EXTREMO Y QUE SEA ARCANO
        int Index = -1;
        int mayor = -1;
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).simbolo.equals("A")){
                if(Siri.mazoplayer.get(i).numero > mayor && Siri.mazoplayer.get(i).numero != 0 && Siri.mazoplayer.get(i).numero != 1
                        && Siri.mazoplayer.get(i).numero != 21){
                    mayor = Siri.mazoplayer.get(i).numero;
                    Index = i;
                }
            }
        }
        return Index;
    }

    public int ExisteArcano(){
        int Index = -1;
        int mayor = -1;
        int menor = 22;
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).simbolo.equals("A")){
                if(Siri.mazoplayer.get(i).numero > mayor && Siri.mazoplayer.get(i).numero != 0 && Siri.mazoplayer.get(i).numero != 1
                        && Siri.mazoplayer.get(i).numero != 21){
                    mayor = Siri.mazoplayer.get(i).numero;
                }
            }
        }
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).simbolo.equals("A")){
                if(Siri.mazoplayer.get(i).numero < menor && Siri.mazoplayer.get(i).numero != 0 && Siri.mazoplayer.get(i).numero != 1
                        && Siri.mazoplayer.get(i).numero != 21){
                    menor = Siri.mazoplayer.get(i).numero;
                }
            }
        }
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).simbolo.equals("A") && Siri.mazoplayer.get(i).numero != mayor
                    && Siri.mazoplayer.get(i).numero != menor && Siri.mazoplayer.get(i).numero != 21 && Siri.mazoplayer.get(i).numero != 0
                    && Siri.mazoplayer.get(i).numero != 1){
                Index = i;
            }
        }
        return Index;
    }

    public int NormalMayor(){
        int mayor = -1;
        int Index = -1;
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).numero > mayor && !Siri.mazoplayer.get(i).simbolo.equals("A")){
                mayor = Siri.mazoplayer.get(i).numero;
                Index = i;
            }
        }
        return Index;
    }

    public int MenorNormal(){
        int menor = 11;
        int Index = -1;
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).numero < menor && !Siri.mazoplayer.get(i).simbolo.equals("A")){
                menor = Siri.mazoplayer.get(i).numero;
                Index = i;
            }
        }
        return Index;
    }

    public int ExisteNormal(){
        int mayor = -1;
        int Index = -1;
        int menor = 11;
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).numero > mayor && !Siri.mazoplayer.get(i).simbolo.equals("A")){
                mayor = Siri.mazoplayer.get(i).numero;
            }
        }
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).numero < menor && !Siri.mazoplayer.get(i).simbolo.equals("A")){
                menor = Siri.mazoplayer.get(i).numero;
            }
        }
        for(int i = 0; i < Siri.mazoplayer.size(); i++){
            if(Siri.mazoplayer.get(i).numero != mayor && Siri.mazoplayer.get(i).numero != menor && !Siri.mazoplayer.get(i).simbolo.equals("A")){
                Index = i;
            }
        }
        return Index;
    }

    public int mayorPuntaje(int[] Puntajes, int c){
        int mayor = -500;
        int index = -1;
        for(int i = 0; i < c; i++){
            if(Puntajes[i] > mayor){
                mayor = Puntajes[i];
                index = i;
            }
        }
        return index;
    }
}