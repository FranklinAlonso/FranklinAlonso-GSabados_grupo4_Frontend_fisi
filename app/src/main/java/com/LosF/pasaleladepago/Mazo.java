package com.LosF.pasaleladepago;

import java.util.ArrayList;

public class Mazo {

    public ArrayList<Carta> Mazo;

    public Mazo(){
        Mazo = new ArrayList<Carta>();
        for(int i=1; i <= 10; i++) {
            Carta C = new Carta("T", i, false); //T, E, C, D, A
            this.Mazo.add(C);
            C = new Carta("E", i, false); //T, E, C, D, A
            this.Mazo.add(C);
            C = new Carta("C", i, false); //T, E, C, D, A
            this.Mazo.add(C);
            C = new Carta("D", i, false); //T, E, C, D, A
            this.Mazo.add(C);
        }
        for(int i=0; i < 22; i++) {
            if(i == 0 || i == 1 || i == 21) {
                Carta C = new Carta("A", i, true); //T, E, C, D, A
                this.Mazo.add(C);
            }else{
                Carta C = new Carta("A", i, false); //T, E, C, D, A
                this.Mazo.add(C);
            }
        }
    }

    public void Barajar(){
        int i = 0, cantidad = 62, rango = 62;
        int arreglo[] = new int[cantidad];
        ArrayList<Carta> aux = new ArrayList<Carta>();
        arreglo[i] = (int)(Math.random()*rango);
        for(i=1; i < cantidad; i++){
            arreglo[i]=(int)(Math.random()*rango);
            for(int j = 0; j<i;j++){
                if(arreglo[i] == arreglo[j]){
                    i--;
                }
            }
        }
        for(int k=0; k<cantidad; k++){
            aux.add(this.Mazo.get(arreglo[k]));
        }
        this.Mazo = aux;
    }
}
