package com.LosF.pasaleladepago;
import java.util.ArrayList;
public class Mano {
    private ArrayList<Carta> listaCartas = new ArrayList<Carta>();

    public Carta SeleccionarCarta(int valor, String palo){
        for (int i=0; i<=listaCartas.size(); i++){
            if(listaCartas.get(i).numero==valor && listaCartas.get(i).simbolo.equals(palo)){
                return listaCartas.get(i);
            }
        }
        return null;
    }

}
