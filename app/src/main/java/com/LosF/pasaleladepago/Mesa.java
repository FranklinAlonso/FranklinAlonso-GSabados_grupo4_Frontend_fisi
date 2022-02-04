package com.LosF.pasaleladepago;

import java.util.ArrayList;
public class Mesa {
    public ArrayList<Carta> Tirada;

    public Mesa(){
        Tirada = new ArrayList<Carta>();
    }

    public int comparando(String Ultimo){
        int result;
        Carta Mayor = this.Tirada.get(0);
        result = Mayor.Comparar(this.Tirada.get(1));
        if(Ultimo.equals("1") && result == 1){
            return 0;
        }else if(Ultimo.equals("1") && result == 0){
            return 1;
        }
        return result; //1 = gana el primero en tirar 0 = gana el segundo en tirar
    }

}