package com.LosF.pasaleladepago;
import java.util.ArrayList;
public class Partida {
    public ArrayList<Player> Jugadores;
    public Mesa MesaUnica = new Mesa();
    public ArrayList<Carta> Perro = new ArrayList<Carta>();
    public Mazo MazoUnico;
    public String id_Partida;
    public String numJugadores;
    public String Turno;
    public String partidaEmpezada;
    public String Repite;
    public Partida(String nombre, Player P) {
        id_Partida = nombre;
        numJugadores = "1";
        MazoUnico=null;
        Jugadores = new ArrayList<Player>();
        Jugadores.add(P);
        Turno = "0";
        partidaEmpezada = "0";
        Repite = "2";
    }

    public Partida(){}

    public void Contrato(int eleccion){
        Jugadores.get(0).setMultiplicador(eleccion);
    }

    public void vencedor(int vencedor){
        for(int i = 0; i<Jugadores.size(); i++){
            if(Jugadores.get(i).getTurno() == vencedor){
                Jugadores.get(i).agregarArray(MesaUnica);
            }
        }
    }

    public String getId_Partida() {
        return id_Partida;
    }

    public void setNumJugadores(String numJugadores) {
        this.numJugadores = numJugadores;
    }

    public void repatirCartas(){
        for(int i=0;i<13;i++){
            Jugadores.get(0).mazoplayer.add(MazoUnico.Mazo.get(0));
            MazoUnico.Mazo.remove(0);
            Jugadores.get(1).mazoplayer.add(MazoUnico.Mazo.get(0));
            MazoUnico.Mazo.remove(0);
        }

    }

    public void cartasAperro(){
        for(int i = 0; i<6;i++){
            this.Perro.add(MazoUnico.Mazo.get(0));
            this.MazoUnico.Mazo.remove(0);
        }
    }

    public void llevarPerro(int p){
        MazoGanada MG=new MazoGanada();
        Jugadores.get(p).mazoGanada=MG;
        Player pj = Jugadores.get(p);
        for(int i=0; i<6;i++) {
            Jugadores.get(p).mazoGanada.ganado.add(Perro.get(0));
            Perro.remove(0);
        }
    }

    /*public ArrayList<Carta> cartasPerro() {
        ArrayList<Carta> perro = new ArrayList<Carta>();
        for (int i=0;i<6;i++){
            perro.add(Mazo.get(0));
            Mazo.remove(0);
        }
        return perro;
    }*/
}
