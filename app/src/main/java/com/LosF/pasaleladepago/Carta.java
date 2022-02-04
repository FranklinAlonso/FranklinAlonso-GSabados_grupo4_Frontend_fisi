package com.LosF.pasaleladepago;

public class Carta {
    public String simbolo;
    public int numero;
    public boolean extremo;

    public Carta(String simb, int num, boolean ext) {
        this.simbolo = simb;
        this.extremo = ext;
        this.numero = num;
    }

    public Carta(){}


    public boolean isExtremo() {
        return extremo;
    }

    public int Comparar( Carta C){
        int ganador=0; // 1= Jugador en turno 0 = Jugador no en turno
        if(this.simbolo.equals("A")){
            if(this.numero == 0){
                ganador = 2;
            }else if(this.simbolo.equals(C.simbolo)){
                if(C.numero == 0){
                    ganador = 3;
                }else if(this.numero < C.numero){
                    ganador = 1;
                }
            }
        }else{
            if(C.simbolo.equals("A")){
                if(C.numero == 0){
                    ganador = 3;
                }else{
                    ganador = 1;
                }
            }else if(this.simbolo.equals(C.simbolo)){
                if(this.numero < C.numero){
                    ganador = 1;
                }
            }
        }
        return ganador;
    }



}
