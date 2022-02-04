package com.LosF.pasaleladepago;

import java.util.ArrayList;

public class Player {
    public int turno;
    public int multiplicador;
    public ArrayList<Carta> mazoplayer= new ArrayList<Carta>();
    public MazoGanada mazoGanada;
    public String idPartida;
    public String tiroCarta;
    public String cartaMesa;

    public Player(int t, String idpart){
        this.turno = t;
        this.idPartida = idpart;
        mazoGanada = new MazoGanada();
        tiroCarta = "N";
        cartaMesa = "N";
    }

    public Player(){}

    public int getTurno() {
        return turno;
    }

    public int getmultiplicador() {
        return multiplicador;
    }

    public ArrayList<Carta> getMazoplayer() {
        return mazoplayer;
    }

    public void setMultiplicador(int multiplicador) {
        this.multiplicador = multiplicador;
    }

    public void agregarArray(Mesa M ){
        for(int i=0; i<= M.Tirada.size(); i++){
            mazoplayer.add(M.Tirada.get(i));
        }
    }

}
